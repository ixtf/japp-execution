package org.jzb.execution.application.internal;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.ee.JEE;
import org.jzb.execution.Constant;
import org.jzb.execution.Util;
import org.jzb.execution.application.NoticeService;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.TaskNotice;
import org.jzb.execution.domain.TaskOperatorContextData;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.TaskNoticeRepository;
import org.jzb.execution.domain.repository.TaskOperatorContextDataRepository;
import org.jzb.execution.domain.repository.WeixinOperatorRepository;
import org.jzb.weixin.mp.MpClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by jzb on 17-7-6.
 */
@Startup
@Singleton
@AccessTimeout(value = 30, unit = TimeUnit.MINUTES)
public class NoticeServiceImpl implements NoticeService {
    @Inject
    private Logger log;
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private MpClient mpClient;
    @Inject
    private WeixinOperatorRepository weixinOperatorRepository;
    @Inject
    private TaskOperatorContextDataRepository taskOperatorContextDataRepository;
    @Inject
    private TaskNoticeRepository taskNoticeRepository;
    private Map<String, ScheduledFuture> taskNoticeScheduleMap = Maps.newConcurrentMap();

    private long getDelay(Date dateTime) {
        long minDelay = TimeUnit.SECONDS.toMillis(5);
        long delay = dateTime.getTime() - System.currentTimeMillis();
        return delay > minDelay ? delay : minDelay;
    }

    private void _Tx(Runnable fun) {
        try {
            UserTransaction userTransaction = JEE.getBean(UserTransaction.class);
            userTransaction.begin();
            try {
                fun.run();
                userTransaction.commit();
            } catch (Exception e) {
                log.error("", e);
                userTransaction.rollback();
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void schedule(Principal principal, TaskNotice taskNotice) {
        taskNoticeScheduleMap.compute(taskNotice.getId(), (k, v) -> {
            if (v != null) {
                cancel(v);
            }
            long delay = getDelay(taskNotice.getNoticeDateTime());
            return ses.schedule(() -> _Tx(() -> taskNoticeId(principal, k)), delay, TimeUnit.MILLISECONDS);
        });
    }

    private void taskNoticeId(Principal principal, String id) {
        TaskNotice taskNotice = taskNoticeRepository.find(id);
        if (taskNotice.isDeleted() || taskNotice.isNoticed()) {
            taskNoticeScheduleMap.remove(id);
            return;
        }
        Task task = taskNotice.getTask();
        if (task.isDeleted() || task.isFinish()) {
            taskNotice.setDeleted(true);
            taskNoticeRepository.save(taskNotice);
            taskNoticeScheduleMap.remove(id);
        }

        TaskGroup taskGroup = task.getTaskGroup();
        String taskGroupId = taskGroup == null ? "0" : taskGroup.getId();
        String url = Constant.ROOT_URL + "/taskProgress?taskGroupId=" + taskGroupId + "&taskId=" + task.getId();
        url = Util.authorizeUrl(url, mpClient);

        for (WeixinOperator weixinOperator : getReceivers(principal, taskNotice)) {
            log.debug("===================开始发送微信========================");
            Operator operator = weixinOperator.getOperator();
            TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
            String nickName = Optional.ofNullable(taskOperatorContextData)
                    .map(TaskOperatorContextData::getNickName)
                    .orElse(operator.getName());
            String keyword1 = Optional.ofNullable(taskNotice.getContent())
                    .filter(J::nonBlank)
                    .orElse("无");
            String keyword2 = LocalDate.now().toString();
            try {
                mpClient.msgTpl(weixinOperator.getId())
                        .template_id("5fGJIZ7V04mh05ZiPYEJVLjDgzFTXjdrTE-vFtYLQ40")
                        .url(url)
                        .data("first", "您好，" + nickName + "，您有“" + task.getTitle() + "”的待反馈任务")
                        .data("keyword1", keyword1)
                        .data("keyword2", keyword2)
                        .data("remark", "请点击详情进行处理")
                        .call();
            } catch (Exception e) {
                log.error("", e);
                //只打印错误日志，提醒还是标记为成功，并且从schedule中删除
                //TODO 把每一条发送的错误日志都记下，然后保存，这样可以把提醒标记为未成功，还剩余多少人没有提醒等等
            }
        }
        taskNotice.setNoticed(true);
        taskNoticeRepository.save(taskNotice);
        taskNoticeScheduleMap.remove(taskNotice.getId());
    }

    private Collection<WeixinOperator> getReceivers(Principal principal, TaskNotice taskNotice) {
        return Optional.ofNullable(taskNotice.getReceivers())
                .filter(J::nonEmpty)
                .orElseGet(() -> {
                    Task task = taskNotice.getTask();
                    Collection<Operator> operators = Sets.newHashSet(task.getCharger());
                    operators.addAll(J.emptyIfNull(task.getParticipants()));
                    operators.addAll(J.emptyIfNull(task.getFollowers()));
                    return operators;
                })
                .stream()
                .filter(operator -> !Objects.equals(operator.getId(), principal.getName()))
                .map(weixinOperatorRepository::find)
                .filter(Objects::nonNull)
                .filter(WeixinOperator::isSubscribe)
                .collect(Collectors.toSet());
    }

    private void cancel(ScheduledFuture sf) {
        Optional.ofNullable(sf)
                .filter(it -> !it.isCancelled() && !it.isDone())
                .ifPresent(it -> it.cancel(true));
    }

    @PostConstruct
    void PostConstruct() {
    }

    @PreDestroy
    void PreDestroy() {
    }
}
