package org.jzb.execution.application;

import org.jzb.execution.domain.TaskNotice;

import java.security.Principal;

/**
 * Created by jzb on 17-7-6.
 */
public interface NoticeService {
    void schedule(Principal principal, TaskNotice taskNotice);
}
