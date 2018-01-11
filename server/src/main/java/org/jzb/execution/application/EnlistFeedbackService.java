package org.jzb.execution.application;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.command.EnlistFeedbackUpdateCommand;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.weixin.pay.sl.PaySlNotifyResult;

import java.security.Principal;
import java.util.Map;

/**
 * Created by jzb on 17-4-15.
 */
public interface EnlistFeedbackService {

    EnlistFeedback create(Principal principal, String enlistId, EnlistFeedbackUpdateCommand command) throws Exception;

    @Deprecated
    void sendPrePayMsg(Principal principal, String enlistFeedbackId) throws Exception;

    EnlistFeedback update(Principal principal, String id, EnlistFeedbackUpdateCommand command) throws Exception;

    void delete(Principal principal, String id) throws Exception;

    Map<String, String> weixinPaySl(Principal principal, @NotBlank String id) throws Exception;

    PaySlNotifyResult weixinPaySlNotify(String xmlStr) throws Exception;
}
