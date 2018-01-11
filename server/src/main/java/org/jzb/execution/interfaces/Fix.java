package org.jzb.execution.interfaces;

import org.jzb.execution.domain.repository.*;
import org.jzb.weixin.mp.MpClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by jzb on 17-4-15.
 */
@Startup
@Singleton
public class Fix {
    @Inject
    protected EntityManager em;
    @Inject
    protected MpClient mpClient;
    @Inject
    protected UploadFileRepository uploadFileRepository;
    @Inject
    private Logger log;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private ChannelRepository channelRepository;
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;

    @PostConstruct
    void PostConstruct() {
        log.debug("=======================Fix===================");

        // Operator operator = new Operator();
        // operator.setId("N8nKbXJtvBfzarKt3piZ34");
        // operator.setName("麦田朱");
        // operator.setAvatar("http://wx.qlogo.cn/mmopen/ajNVdqHZLLApFycYRUWEspjXEW3UCp2zt1L9eE7LYK4LSyoXEntiaK0grCmdawgTFiaMtbWtgt6Sd4BD93qW0iceg/0");
        // Login login = new Login();
        // login.setLoginId("13957100995");
        // login.setLoginPassword(J.password("1"));
        // operatorRepository.save(operator, login);
        // WeixinOperator weixinOperator = new WeixinOperator();
        // weixinOperator.setOperator(operator);
        // weixinOperator.setId("oohBQwnqvCd_cZHeTW5uXQxzw6Ek");
        // weixinOperator.setUnionid("ogpzbsonAJE37y2rip-iQ-L6ishg");
        // em.merge(weixinOperator);
        //
        // operator = new Operator();
        // operator.setId("Jmg62HxQV2mzUGM8Qqnq1U");
        // operator.setName("金赵波");
        // operator.setAvatar("http://wx.qlogo.cn/mmopen/Q3auHgzwzM5KYkptsoHL2UBx9niaSQ0WjuicxglMIFqFsbticYSiaNb9OGk6RyaDWa8Vg40WBsp1vKuUlia1FrAkpNg/0");
        // login = new Login();
        // login.setLoginId("13456978427");
        // login.setLoginPassword(J.password("123456"));
        // operatorRepository.save(operator, login);
        // weixinOperator = new WeixinOperator();
        // weixinOperator.setOperator(operator);
        // weixinOperator.setId("oohBQwjH5QPqyNVFzf54MTMSBV0Y");
        // weixinOperator.setUnionid("ogpzbsnxiYxyYy4GYddXuWBgLEYs");
        // em.merge(weixinOperator);
        //
        // PaymentMerchant paymentMerchant = paymentMerchantRepository.find("W1SxU7M5eYJwzoPuJfyLQv");
        // if (paymentMerchant == null) {
        //     paymentMerchant = new PaymentMerchant();
        //     paymentMerchant.setId("W1SxU7M5eYJwzoPuJfyLQv");
        //     paymentMerchant.setSub_mch_id("1490476912");
        //     paymentMerchant.setName("算学宫");
        //     paymentMerchant._loginfo(operator);
        //     paymentMerchantRepository.save(paymentMerchant);
        // }
        //
        // Channel channel = new Channel();
        // channel.setId("XiGbVtkYRkSYtCzENwe7VC");
        // channel.setName("成长");
        // channelRepository.save(channel);
        // channel = new Channel();
        // channel.setId("XWPyFQkfBGZrdXGj4EAn2D");
        // channel.setName("常用模板下载");
        // channelRepository.save(channel);
        // channel = new Channel();
        // channel.setId("Qb1qeMgEE45j6Sdke51Fvd");
        // channel.setName("工作计划");
        // channelRepository.save(channel);
        // channel = new Channel();
        // channel.setId("MVyZm5eFvXdiwxSLcwgSYU");
        // channel.setName("健康生活");
        // channelRepository.save(channel);
        // channel = new Channel();
        // channel.setId("XJLZPxdHLTCL7mUWvV8sMr");
        // channel.setName("退休计划");
        // channelRepository.save(channel);
        // channel = new Channel();
        // channel.setId("V3GUbBCPtfT8TC8qX9Htr");
        // channel.setName("其他1");
        // channelRepository.save(channel);
    }
}
