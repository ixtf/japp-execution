package org.jzb.execution.application.internal;

import org.apache.commons.collections4.CollectionUtils;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.EnlistService;
import org.jzb.execution.application.command.EnlistGenerateTaskCommand;
import org.jzb.execution.application.command.EnlistUpdateCommand;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.data.EnlistStatus;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.ENLIST_INVITE;

@Stateless
public class EnlistServiceImpl implements EnlistService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private FeedbackTemplateRepository feedbackTemplateRepository;
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;
    @Inject
    private EnlistRepository enlistRepository;
    @Inject
    private MpClient mpClient;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private EnlistFeedbackRepository enlistFeedbackRepository;

    @Override
    public Enlist create(Principal principal, String paymentMerchantId, EnlistUpdateCommand command) throws Exception {
        final PaymentMerchant paymentMerchant = paymentMerchantRepository.find(paymentMerchantId);
        if (!paymentMerchant.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        final Enlist enlist = new Enlist();
        enlist.setPaymentMerchant(paymentMerchant);
        enlist.setMoney(command.getMoney());
        return save(principal, enlist, command);
    }

    private Enlist save(Principal principal, Enlist o, EnlistUpdateCommand command) {
        o.setTitle(command.getTitle());
        o.setContent(command.getContent());
        o.setTags(command.getTags());
        o.setStartDate(command.getStartDate());
        o.setFeedbackLimit(command.getFeedbackLimit());
        final FeedbackTemplate feedbackTemplate = Optional.of(command.getFeedbackTemplate())
                .map(EntityDTO::getId)
                .map(feedbackTemplateRepository::find)
                .get();
        o.setFeedbackTemplate(feedbackTemplate);
        final Set<UploadFile> attachments = CollectionUtils.emptyIfNull(command.getAttachments())
                .stream()
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .collect(Collectors.toSet());
        o.setAttachments(attachments);
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return enlistRepository.save(o);
    }

    @Override
    public Enlist update(Principal principal, String id, EnlistUpdateCommand command) throws Exception {
        return save(principal, enlistRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) throws Exception {
        final Enlist enlist = enlistRepository.find(id);
        if (!enlist.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        enlistRepository.delete(id);
    }

    @Override
    public EnlistInvite inviteTicket(Principal principal, String id) throws Exception {
        final Enlist enlist = enlistRepository.find(id);
        if (!enlist.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(ENLIST_INVITE.scene_id())
                .build()
                .call();
        EnlistInvite invite = new EnlistInvite();
        invite.setEnlist(enlist);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return enlistRepository.save(invite);
    }

    @Override
    public void finish(Principal principal, String id) {
        Enlist enlist = enlistRepository.find(id);
        if (enlist.isDeleted() || enlist.getStatus() == EnlistStatus.FINISH) {
            return;
        }
        enlist.setStatus(EnlistStatus.FINISH);
        enlistRepository.save(enlist);
    }

    @Override
    public void restart(Principal principal, String id) {
        Enlist enlist = enlistRepository.find(id);
        if (enlist.isDeleted() || enlist.getStatus() == EnlistStatus.RUN) {
            return;
        }
        enlist.setStatus(EnlistStatus.RUN);
        enlistRepository.save(enlist);
    }

    @Override
    public Task generateTask(Principal principal, String id, EnlistGenerateTaskCommand command) {
        Task task = new Task();
        Operator operator = operatorRepository.find(principal);
        task.setCharger(operator);
        task._loginfo(operator);
        TaskGroup taskGroup = Optional.ofNullable(command.getTaskGroupName())
                .filter(J::nonBlank)
                .map(taskGroupName -> {
                    TaskGroup newTaskGroup = new TaskGroup();
                    newTaskGroup.setName(taskGroupName);
                    newTaskGroup._loginfo(operator);
                    return taskGroupRepository.save(newTaskGroup);
                })
                .orElseGet(() -> Optional.ofNullable(command.getTaskGroup())
                        .map(EntityDTO::getId)
                        .map(taskGroupRepository::find)
                        .orElse(null)
                );
        task.setTaskGroup(taskGroup);

        Enlist enlist = enlistRepository.find(id);
        task.setTitle(enlist.getTitle());

        final Set<Operator> participants = enlistFeedbackRepository.queryBy(enlist)
                .filter(enlistFeedback -> enlistFeedback.getPayment().isSuccessed())
                .map(EnlistFeedback::getCreator)
                .collect(Collectors.toSet());
        task.setParticipants(participants);
        return taskRepository.save(task);
    }
}
