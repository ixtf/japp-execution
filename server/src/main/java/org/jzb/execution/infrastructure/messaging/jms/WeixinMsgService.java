package org.jzb.execution.infrastructure.messaging.jms;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.execution.Util;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.*;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.MsgPushed;
import org.jzb.weixin.mp.unionInfo.MpUnionInfoResponse;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.jzb.execution.Constant.ROOT_URL;
import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.*;

/**
 * Created by jzb on 17-4-15.
 */
@Singleton
@AccessTimeout(value = 1, unit = TimeUnit.HOURS)
public class WeixinMsgService {
    @Inject
    private MpClient mpClient;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private ExamQuestionLabRepository examQuestionLabRepository;
    @Inject
    private WeixinOperatorRepository weixinOperatorRepository;
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;
    @Inject
    private EnlistRepository enlistRepository;
    @Inject
    private RedEnvelopeOrganizationRepository redEnvelopeOrganizationRepository;

    public void handle(MsgPushed msgPushed) throws Exception {
        if (msgPushed.isEvt_subscribe()) {
            handleSubscribe(msgPushed);
            helpMsg(msgPushed.getFromUserName());
            handleInvite(msgPushed);
        } else if (msgPushed.isEvt_unsubscribe()) {
            handleUnSubscribe(msgPushed);
        } else if (msgPushed.isEvt_SCAN()) {
            handleInvite(msgPushed);
        }
    }

    private void handleInvite(MsgPushed msgPushed) throws Exception {
        String ticket = msgPushed.getTicket();
        if (J.isBlank(ticket))
            return;
        Operator operator = handleSubscribe(msgPushed).getOperator();
        if (operator == null)
            return;
        int key = Optional.ofNullable(msgPushed.getEventKey())
                .map(ek -> {
                    String prefix = "qrscene_";
                    return ek.startsWith(prefix) ? ek.substring(prefix.length()) : ek;
                })
                .map(Integer::valueOf)
                .orElse(0);
        if (TASKS_INVITE.scene_id() == key) {
            TasksInvite invite = taskRepository.findTasksInviteByTicket(ticket);
            invite.getTasks().forEach(task -> {
                Collection<Operator> participants = Sets.newHashSet(operator);
                participants.addAll(J.emptyIfNull(task.getParticipants()));
                task.setParticipants(participants);
                taskRepository.save(task);
            });
            String content = "成功加入!";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        } else if (TASKS_FOLLOW_INVITE.scene_id() == key) {
            TasksFollowInvite invite = taskRepository.findTasksFollowInviteByTicket(ticket);
            invite.getTasks().forEach(task -> {
                Collection<Operator> followers = Sets.newHashSet(operator);
                followers.addAll(J.emptyIfNull(task.getFollowers()));
                task.setFollowers(followers);
                taskRepository.save(task);
            });
            String content = "管理员成功加入!";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        } else if (PLAN_INVITE.scene_id() == key) {
            final PlanInvite invite = planRepository.findPlanInviteByTicket(ticket);
            final Plan plan = invite.getPlan();
            String url = Util.getPlanShareDetailWxUrl(plan, mpClient);
            mpClient.msgKf(msgPushed.getFromUserName()).news()
                    .newArticle()
                    .title(plan.getName())
                    .description("请点击详情，选择下载")
                    .url(url)
                    .addArticle()
                    .call();
        } else if (EXAMQUESTIONLAB_INVITE.scene_id() == key) {
            ExamQuestionLabInvite invite = examQuestionLabRepository.findExamQuestionLabInviteByTicket(ticket);
            ExamQuestionLab lab = invite.getLab();
            Collection<Operator> operators = Sets.newHashSet(operator);
            operators.addAll(J.emptyIfNull(lab.getParticipants()));
            lab.setParticipants(operators);
            lab = examQuestionLabRepository.save(lab);
            String content = lab.getName() + "\n" + "成功加入!";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        } else if (REDENVELOPEORGANIZATION_INVITE.scene_id() == key) {
            final RedEnvelopeOrganizationInvite invite = redEnvelopeOrganizationRepository.findRedEnvelopeOrganizationInviteByTicket(ticket);
            RedEnvelopeOrganization redEnvelopeOrganization = invite.getRedEnvelopeOrganization();
            Set<Operator> managers = Sets.newHashSet(operator);
            managers.addAll(J.emptyIfNull(redEnvelopeOrganization.getManagers()));
            redEnvelopeOrganization.setManagers(managers);
            redEnvelopeOrganization = redEnvelopeOrganizationRepository.save(redEnvelopeOrganization);
            String content = "红包发放组织：" + redEnvelopeOrganization.getName() + "\n" + "成功加入!";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        }  else if (PAYMENTMERCHANT_INVITE.scene_id() == key) {
            final PaymentMerchantInvite invite = paymentMerchantRepository.findPaymentMerchantInviteByTicket(ticket);
            PaymentMerchant paymentMerchant = invite.getPaymentMerchant();
            Set<Operator> managers = Sets.newHashSet(operator);
            managers.addAll(J.emptyIfNull(paymentMerchant.getManagers()));
            paymentMerchant.setManagers(managers);
            paymentMerchant = paymentMerchantRepository.save(paymentMerchant);
            String content = "商户：" + paymentMerchant.getName() + "\n" + "成功加入!";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        } else if (ENLIST_INVITE.scene_id() == key) {
            final EnlistInvite invite = enlistRepository.findEnlistInviteByTicket(ticket);
            final Enlist enlist = invite.getEnlist();
            final PaymentMerchant paymentMerchant = enlist.getPaymentMerchant();
            final String urlTpl = ROOT_URL + "/enlists/progress?enlistId=${enlistId}";
            final String next = J.strTpl(urlTpl, ImmutableMap.of("enlistId", enlist.getId()));
            final String url = Util.authorizeUrl(next, mpClient);
            String content = "商户：" + paymentMerchant.getName() + "\n名称：" + enlist.getTitle() + "\n<a href=\"" + url + "\">点击查看和报名</a>";
            mpClient.msgKf(msgPushed.getFromUserName()).text().content(content).call();
        }
    }

    public WeixinOperator handleSubscribe(MsgPushed msgPushed) throws Exception {
        final Operator operator;
        final MpUnionInfoResponse res = mpClient.unionInfo(msgPushed.getFromUserName()).call();
        WeixinOperator weixinOperator = Optional.ofNullable(weixinOperatorRepository.find(res.openid()))
                .orElse(weixinOperatorRepository.findByUnionid(res.unionid()));
        if (weixinOperator != null) {
            operator = weixinOperator.getOperator();
        } else {
            operator = new Operator();
            operator.setName(res.nickname());
            weixinOperator = new WeixinOperator();
        }
        operator.setDeleted(false);
        return operatorRepository.save(operator, weixinOperator, res);
    }

    private void helpMsg(String openid) throws Exception {
        String picUrl = "https://mmbiz.qlogo.cn/mmbiz_jpg/0CE4uD6SicXS7LhuGUfORTobyycXibhGGqQdaxaoSYH87bpxJyXXYUsr3VsicibDAYpqe51QeLwhngeznqDfHCE7pQ/0?wx_fmt=jpeg";
        mpClient.msgKf(openid)
                .news()
                .newArticle()
                .title("使用帮助")
                .title("微计划，使用说明")
                .url("http://mp.weixin.qq.com/s/N0IkdhNnsA4btY_aPrvebg")
                .picurl(picUrl)
                .addArticle()
                .call();
    }

    public void handleUnSubscribe(MsgPushed msgPushed) {
        Optional.ofNullable(weixinOperatorRepository.find(msgPushed.getFromUserName()))
                .ifPresent(weixinOperator -> {
                    Operator operator = weixinOperator.getOperator();
                    operator.setDeleted(true);
                    operatorRepository.save(operator);
                });
    }
}
