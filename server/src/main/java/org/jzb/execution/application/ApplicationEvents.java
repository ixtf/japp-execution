package org.jzb.execution.application;


import org.jzb.execution.domain.event.EventType;

import java.security.Principal;

/**
 * Created by jzb on 16-7-5.
 */
public interface ApplicationEvents {

    void fireWeixinMsgPush(String xml) throws Exception;

    void fireCurd(Principal principal, Class clazz, String id, EventType eventType, Object command) throws Exception;

    void fireCurd(Principal principal, Class clazz, String id, EventType eventType) throws Exception;

//    void fireTaskReaded(Principal principal, String id) throws Exception;
//
//    void fireTaskNoticeCreated(Principal principal, TaskNotice taskNotice) throws Exception;
//
//    void fireTaskNoticeUpdated(Principal principal, TaskNotice taskNotice) throws Exception;
//
//    void fireTaskNoticeDeleted(Principal principal, String id) throws Exception;
}
