package org.jzb.execution.application.internal;

import org.apache.commons.collections4.CollectionUtils;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.PlanService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.PlanDownloadCommand;
import org.jzb.execution.application.command.PlanUpdateCommand;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.PLAN_INVITE;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class PlanServiceImpl implements PlanService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ChannelRepository channelRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private FeedbackTemplateRepository feedbackTemplateRepository;
    @Inject
    private EvaluateTemplateRepository evaluateTemplateRepository;
    @Inject
    private ExperienceRepository experienceRepository;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private PlanFollowRepository planFollowRepository;
    @Inject
    private PlanContextDataRepository planContextDataRepository;
    @Inject
    private ChannelContextDataRepository channelContextDataRepository;
    @Inject
    private MpClient mpClient;

    private void checkUpdateAuth(Principal principal, String id) {
        Operator operator = operatorRepository.find(principal);
        Plan plan = planRepository.find(id);
        if (plan.isDeleted() || operator.isDeleted() || !plan.getCreator().equals(operator)) {
            throw new JNonAuthorizationError();
        }
    }

    @Override
    public Plan create(Principal principal, PlanUpdateCommand command) {
        Plan plan = save(principal, new Plan(), command);
        channelContextDataRepository.addPlanCount(plan.getChannel());
        return plan;
    }

    private Plan save(Principal principal, Plan o, PlanUpdateCommand command) {
        o.setChannel(channelRepository.find(command.getChannel().getId()));
        o.setName(command.getName());
        o.setAdUrl(command.getAdUrl());
        o.setNote(command.getNote());
        o.setTags(command.getTags());
        o.setShared(!command.isJustQrcode());

        AtomicInteger sortBy = new AtomicInteger(command.getItems().size() * 1000);
        Collection<PlanItem> items = command.getItems().stream()
                .map(dto -> {
                    PlanItem item = new PlanItem();
                    item.setId(J.uuid58());
                    item.setPlan(o);
                    item.setSortBy(sortBy.getAndAdd(-1000));
                    item.setTitle(dto.getTitle());
                    item.setContent(dto.getContent());
                    item.setTags(dto.getTags());
                    FeedbackTemplate feedbackTemplate = Optional.ofNullable(dto.getFeedbackTemplate())
                            .map(EntityDTO::getId)
                            .map(feedbackTemplateRepository::find)
                            .orElse(null);
                    item.setFeedbackTemplate(feedbackTemplate);
                    EvaluateTemplate evaluateTemplate = Optional.ofNullable(dto.getEvaluateTemplate())
                            .map(EntityDTO::getId)
                            .map(evaluateTemplateRepository::find)
                            .orElse(null);
                    item.setEvaluateTemplate(evaluateTemplate);
                    Collection<UploadFile> attachments = CollectionUtils.emptyIfNull(dto.getAttachments()).stream()
                            .map(EntityDTO::getId)
                            .distinct()
                            .map(uploadFileRepository::find)
                            .collect(Collectors.toSet());
                    item.setAttachments(attachments);
                    Collection<Experience> experiences = CollectionUtils.emptyIfNull(dto.getExperiences()).stream()
                            .map(EntityDTO::getId)
                            .distinct()
                            .map(experienceRepository::find)
                            .collect(Collectors.toSet());
                    item.setExperiences(experiences);
                    return item;
                })
                .collect(Collectors.toSet());
        o.setItems(items);
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return planRepository.save(o);
    }

    @Override
    public Plan update(Principal principal, String id, PlanUpdateCommand command) throws Exception {
        checkUpdateAuth(principal, id);
        Plan plan = planRepository.find(id);
        if (plan.isAudited())
            throw new JNonAuthorizationError();
        return save(principal, plan, command);
    }

    @Override
    public void delete(Principal principal, String id) {
        Plan plan = planRepository.find(id);
        if (plan.getCreator().getId().equals(principal.getName()))
            planRepository.delete(id);
    }

    @Override
    public PlanInvite inviteTicket(Principal principal, String id) throws Exception {
        checkUpdateAuth(principal, id);
        Plan plan = planRepository.find(id);
        if (!plan.isAudited()) {
            throw new JNonAuthorizationError();
        }
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(PLAN_INVITE.scene_id())
                .build()
                .call();
        PlanInvite invite = new PlanInvite();
        invite.setPlan(plan);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return planRepository.save(invite);
    }

    @Override
    public void publish(Principal principal, String id) throws Exception {
        checkUpdateAuth(principal, id);
        Operator operator = operatorRepository.find(principal);
        Plan plan = planRepository.find(id);
        if (!plan.isPublished()) {
            plan.setPublished(true);
            plan.setPublishDate(new Date());
            plan.setPublisher(operator);
            planRepository.save(plan);
        }
    }

    @Override
    public void unPublish(Principal principal, String id) throws Exception {
        checkUpdateAuth(principal, id);
        Plan plan = planRepository.find(id);
        if (plan.isPublished()) {
            plan.setAudited(false);
            plan.setPublished(false);
            planRepository.save(plan);
        }
    }

    @Override
    public PlanFollow follow(Principal principal, String id) throws Exception {
        Plan plan = planRepository.find(id);
        if (!plan.isAudited())
            throw new JNonAuthorizationError();
        Operator follower = operatorRepository.find(principal);
        return planFollowRepository.save(plan, follower);
    }

    @Override
    public void unFollow(Principal principal, String id) throws Exception {
        Plan plan = planRepository.find(id);
        if (!plan.isAudited())
            throw new JNonAuthorizationError();
        Operator follower = operatorRepository.find(principal);
        planFollowRepository.delete(plan, follower);
    }

    @Override
    public void download(Principal principal, String id, PlanDownloadCommand command) throws Exception {
        Plan plan = planRepository.find(id);
        if (!plan.isAudited())
            throw new JNonAuthorizationError();
        final Operator operator = operatorRepository.find(principal);
        TaskGroup taskGroup = Optional.ofNullable(command.getTaskGroup())
                .map(EntityDTO::getId)
                .map(taskGroupRepository::find)
                .orElse(null);
        plan.getItems().stream()
                .forEach(item -> {
                    Task task = new Task();
                    task.setPlan(plan);
                    task.setCharger(operator);
                    task.setTaskGroup(taskGroup);
                    task.setTitle(item.getTitle());
                    task.setContent(item.getContent());
                    task.setTags(item.getTags());
                    task.setAttachments(item.getAttachments());
                    task.setExperiences(item.getExperiences());
                    task.setFeedbackTemplateString(item.getFeedbackTemplateString());
                    task.setEvaluateTemplateString(item.getEvaluateTemplateString());
                    task._loginfo(operator);
                    taskRepository.save(task);
                });
        planContextDataRepository.addDownloadCount(plan);
        if (command.isFollow()) {
            follow(principal, id);
        }
    }
}
