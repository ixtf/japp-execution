package org.jzb.execution.application.command;

import org.jzb.J;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;

/**
 * Created by jzb on 17-4-15.
 */

public class TaskNoticeUpdateCommand implements Serializable {
    @NotNull
    private Date noticeDate;
    @NotNull
    private Date noticeTime;
    private String content;
    private Collection<EntityDTO> receivers;

    public Date getNoticeDateTime() {
        LocalDate ld = J.localDate(noticeDate);
        LocalTime lt = J.localTime(noticeTime);
        return J.date(ld.atTime(lt));
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Collection<EntityDTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(Collection<EntityDTO> receivers) {
        this.receivers = receivers;
    }

}