/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.J;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_ENLISTFEEDBACKPAYMENT")
@NamedQueries({
        @NamedQuery(name = "EnlistFeedbackPayment.findByOutTradeNo", query = "SELECT o FROM EnlistFeedbackPayment o WHERE o.out_trade_no=:out_trade_no AND o.deleted=FALSE "),
})
public class EnlistFeedbackPayment extends AbstractEntity {
    @JsonIgnore
    @PrimaryKeyJoinColumn
    @OneToOne
    @NotNull
    private EnlistFeedback enlistFeedback;
    @Column(unique = true, length = 36)
    private String out_trade_no;
    @JsonIgnore
    private WeixinPrePayment weixinPrePayment;
    private WeixinPayment weixinPayment;

    @JsonGetter
    public boolean isSuccessed() {
        return Optional.ofNullable(weixinPayment)
                .map(WeixinPayment::getTransaction_id)
                .map(J::nonBlank)
                .orElse(false);
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public EnlistFeedback getEnlistFeedback() {
        return enlistFeedback;
    }

    public void setEnlistFeedback(EnlistFeedback enlistFeedback) {
        this.enlistFeedback = enlistFeedback;
    }

    public WeixinPrePayment getWeixinPrePayment() {
        return weixinPrePayment;
    }

    public void setWeixinPrePayment(WeixinPrePayment weixinPrePayment) {
        this.weixinPrePayment = weixinPrePayment;
    }

    public WeixinPayment getWeixinPayment() {
        return weixinPayment;
    }

    public void setWeixinPayment(WeixinPayment weixinPayment) {
        this.weixinPayment = weixinPayment;
    }
}
