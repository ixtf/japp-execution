/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author jzb
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public abstract class WeixinInvite implements Serializable {
    @Id
    protected String ticket;
    protected Date generateDateTime = new Date();
    protected int expireSeconds = 2592000;

    public boolean isExpired() {
        long currMillis = System.currentTimeMillis();
        long millis = currMillis - generateDateTime.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return seconds < expireSeconds;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getGenerateDateTime() {
        return generateDateTime;
    }

    public void setGenerateDateTime(Date generateDateTime) {
        this.generateDateTime = generateDateTime;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
