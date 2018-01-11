package org.jzb.execution.application.internal;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.Util;
import org.jzb.execution.application.EnlistFeedbackService;
import org.jzb.execution.application.command.EnlistFeedbackUpdateCommand;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.*;
import org.jzb.execution.exception.EnlistFeedbackAlreadyPaiedException;
import org.jzb.execution.exception.EnlistFeedbackLimitedException;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.pay.sl.PaySlClient;
import org.jzb.weixin.pay.sl.PaySlNotify;
import org.jzb.weixin.pay.sl.PaySlNotifyResult;
import org.jzb.weixin.pay.sl.unifiedorder.PaySlUnifiedorderResponse;
import org.jzb.weixin.pay.sl.util.PaySlConstant;
import org.slf4j.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jzb.execution.Constant.ROOT_URL;

@Singleton
public class EnlistFeedbackServiceImpl implements EnlistFeedbackService {
    @Inject
    private Logger log;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private WeixinOperatorRepository weixinOperatorRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private EnlistRepository enlistRepository;
    @Inject
    private EnlistFeedbackRepository enlistFeedbackRepository;
    @Inject
    private EnlistFeedbackPaymentRepository enlistFeedbackPaymentRepository;
    @Inject
    private MpClient mpClient;
    @Inject
    private PaySlClient paySlClient;

    @Override
    public EnlistFeedback create(Principal principal, String enlistId, EnlistFeedbackUpdateCommand command) throws Exception {
        final Enlist enlist = enlistRepository.find(enlistId);
        final List<EnlistFeedback> feedbacks = enlistFeedbackRepository.queryBy(enlist).collect(Collectors.toList());
        if (enlist.hasFeedbackLimit()) {
            if (feedbacks.size() >= enlist.getFeedbackLimit()) {
                throw new EnlistFeedbackLimitedException(enlist);
            }
        }
        EnlistFeedback enlistFeedback = feedbacks.stream()
                .filter(it -> it.getCreator().getId().equals(principal.getName()))
                .findFirst()
                .orElseGet(() -> {
                    final EnlistFeedback res = new EnlistFeedback();
                    res.setEnlist(enlist);
                    return res;
                });
        enlistFeedback = save(principal, enlistFeedback, command);
        // sendPrePayMsg(principal, enlistFeedback.getId());
        return enlistFeedback;
    }

    @Override
    public void sendPrePayMsg(Principal principal, String enlistFeedbackId) throws Exception {
        final EnlistFeedback enlistFeedback = enlistFeedbackRepository.find(enlistFeedbackId);
        final Enlist enlist = enlistFeedback.getEnlist();
        final PaymentMerchant paymentMerchant = enlist.getPaymentMerchant();
        final String urlTpl = ROOT_URL + "/weixin/pay/paymentMerchants/${paymentMerchantId}?enlistFeedbackId=${enlistFeedbackId}";
        final String next = J.strTpl(urlTpl, ImmutableMap.of("paymentMerchantId", paymentMerchant.getId(), "enlistFeedbackId", enlistFeedback.getId()));
        final String url = Util.authorizeUrl(next, mpClient);
        mpClient.msgTpl(weixinOperatorRepository.find(principal).getId())
                .template_id("1YBPn3OVFKQMFidoI4UD1mSusZj99cbxA8olh6pHRbY")
                .url(url)
                .data("first", "订单待支付提醒")
                .data("keyword1", "¥" + enlist.getMoney())
                .data("keyword2", enlist.getTitle())
                .data("remark", "点击进入付款")
                .call();
    }

    private EnlistFeedback save(Principal principal, EnlistFeedback o, EnlistFeedbackUpdateCommand command) {
        o.setNote(command.getNote());
        final Set<UploadFile> attachments = CollectionUtils.emptyIfNull(command.getAttachments())
                .stream()
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .collect(Collectors.toSet());
        o.setAttachments(attachments);
        o.setFieldsValue(command.getFieldsValue());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return enlistFeedbackRepository.save(o);
    }

    @Override
    public EnlistFeedback update(Principal principal, String id, EnlistFeedbackUpdateCommand command) throws Exception {
        final EnlistFeedback enlistFeedback = enlistFeedbackRepository.find(id);
        if (!enlistFeedback.getCreator().getId().equals(principal.getName())) {
            throw new JNonAuthorizationError();
        }
        return save(principal, enlistFeedback, command);
    }

    @Override
    public void delete(Principal principal, String id) throws Exception {
        final EnlistFeedback enlistFeedback = enlistFeedbackRepository.find(id);
        if (enlistFeedback.getPayment().isSuccessed()) {
            throw new EnlistFeedbackAlreadyPaiedException(enlistFeedback);
        }
        if (enlistFeedback.getEnlist().isManager(principal) || enlistFeedback.isSelf(principal)) {
            enlistFeedbackRepository.delete(id);
        }
        throw new JNonAuthorizationError();
    }

    @Override
    public Map<String, String> weixinPaySl(Principal principal, @NotBlank String id) throws Exception {
        final WeixinOperator weixinOperator = weixinOperatorRepository.find(principal);
        final EnlistFeedback enlistFeedback = enlistFeedbackRepository.find(id);
        if (!Objects.equals(enlistFeedback.getCreator(), weixinOperator.getOperator())) {
            throw new JNonAuthorizationError();
        }
        EnlistFeedbackPayment enlistFeedbackPayment = enlistFeedbackPaymentRepository.find(id);
        if (enlistFeedbackPayment != null) {
            if (enlistFeedbackPayment.isSuccessed()) {
                throw new EnlistFeedbackAlreadyPaiedException(enlistFeedback);
            }
        } else {
            enlistFeedbackPayment = new EnlistFeedbackPayment();
            enlistFeedbackPayment.setEnlistFeedback(enlistFeedback);
            enlistFeedbackPayment.setId(enlistFeedback.getId());
        }
        final WeixinPrePayment weixinPrePayment = new WeixinPrePayment();
        final Enlist enlist = enlistFeedback.getEnlist();
        final PaymentMerchant paymentMerchant = enlist.getPaymentMerchant();
        weixinPrePayment.setAppid(paySlClient.getAppid());
        weixinPrePayment.setMch_id(paySlClient.getMch_id());
        weixinPrePayment.setSub_mch_id(paymentMerchant.getSub_mch_id());
        weixinPrePayment.setOpenid(weixinOperator.getId());
        weixinPrePayment.setTotal_fee(enlist.getMoney());
        // 每次都生成新的订单进行支付，防止支付过期
        enlistFeedbackPayment.setOut_trade_no(J.uuid58());
        final PaySlUnifiedorderResponse res = paySlClient.unifiedorder()
                .sub_mch_id(weixinPrePayment.getSub_mch_id())
                .total_fee(weixinPrePayment.getTotal_fee())
                .openid(weixinPrePayment.getOpenid())
                .out_trade_no(enlistFeedbackPayment.getOut_trade_no())
                .body(enlist.getTitle())
                .notify_url(ROOT_URL + "/WeixinPayNotifyInterfaceServlet")
                .call();
        weixinPrePayment.setPrepay_id(res.prepay_id());
        weixinPrePayment.setTrade_type(res.trade_type());
        weixinPrePayment.setCode_url(res.code_url());
        enlistFeedbackPayment.setWeixinPrePayment(weixinPrePayment);
        enlistFeedbackPaymentRepository.save(enlistFeedbackPayment);
        enlistFeedback.setPayment(enlistFeedbackPayment);
        enlistFeedbackRepository.save(enlistFeedback);
        return res.jsapiPayData();
    }

    @Override
    public PaySlNotifyResult weixinPaySlNotify(String xmlStr) throws Exception {
        log.debug("=======================支付回调===================");
        log.debug(xmlStr);

        PaySlNotifyResult result = new PaySlNotifyResult();
        final boolean signValid = paySlClient.isSignValid(xmlStr, PaySlConstant.SignType.MD5);
        if (!signValid) {
            result.successed(false);
            result.setReturn_msg("签名错误！");
            return result;
        }
        final PaySlNotify paySlNotify = new PaySlNotify(xmlStr);
        if (!paySlNotify.isSuccessed()) {
            result.successed(false);
            result.setReturn_msg(paySlNotify.errMsg());
            return result;
        }
        EnlistFeedbackPayment enlistFeedbackPayment = enlistFeedbackPaymentRepository.findByOutTradeNo(paySlNotify.out_trade_no());
        if (enlistFeedbackPayment == null) {
            result.successed(false);
            result.setReturn_msg("【" + paySlNotify.out_trade_no() + "】，订单不存在！");
            return result;
        }
        if (enlistFeedbackPayment.isSuccessed()) { // 已经处理
            result.successed(true);
            return result;
        }
        final WeixinPrePayment prePayment = enlistFeedbackPayment.getWeixinPrePayment();
        if (prePayment.getTotal_fee().doubleValue() != paySlNotify.total_fee()) {
            result.successed(false);
            result.setReturn_msg("订单金额不一致！");
            return result;
        }
        WeixinPayment weixinPayment = new WeixinPayment();
        weixinPayment.setTransaction_id(paySlNotify.transaction_id());
        weixinPayment.setBank_type(paySlNotify.bank_type());
        weixinPayment.setCash_fee(BigDecimal.valueOf(paySlNotify.cash_fee()));
        weixinPayment.setTime_end(paySlNotify.time_end());
        enlistFeedbackPayment.setWeixinPayment(weixinPayment);
        enlistFeedbackPayment = enlistFeedbackPaymentRepository.save(enlistFeedbackPayment);
        sendPayMsg(enlistFeedbackPayment);
        result.successed(true);
        return result;
    }

    private void sendPayMsg(final EnlistFeedbackPayment enlistFeedbackPayment) throws Exception {
        final WeixinPrePayment weixinPrePayment = enlistFeedbackPayment.getWeixinPrePayment();
        final WeixinPayment weixinPayment = enlistFeedbackPayment.getWeixinPayment();
        final EnlistFeedback enlistFeedback = enlistFeedbackPayment.getEnlistFeedback();
        final Enlist enlist = enlistFeedback.getEnlist();
        final String urlTpl = ROOT_URL + "/enlists/progress?enlistId=${enlistId}";
        final String next = J.strTpl(urlTpl, ImmutableMap.of("enlistId", enlist.getId()));
        final String url = Util.authorizeUrl(next, mpClient);
        final DateFormat df = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss");
        mpClient.msgTpl(weixinPrePayment.getOpenid())
                .template_id("3dHSDw-_4fudZudRQzYG5vh_1NPzrEGzi3s9DUs8dDk")
                .url(url)
                .data("first", "支付成功通知")
                .data("keyword1", enlistFeedback.getCreator().getName())
                .data("keyword2", enlist.getTitle())
                .data("keyword3", "¥" + enlist.getMoney())
                .data("keyword4", df.format(weixinPayment.getTime_end()))
                .data("remark", "点击详情")
                .call();
    }
}
