package org.jzb.execution.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：
 * 微信支付凭证
 *
 * @author jzb
 * @create 2017-10-24
 */
@Embeddable
public class WeixinPayment implements Serializable {
    @Column(name = "wexin_pay_transaction_id")
    private String transaction_id;
    @Column(name = "wexin_pay_bank_type")
    private String bank_type;
    @Column(name = "wexin_pay_cash_fee", precision = 12, scale = 2)
    @Min(1)
    private BigDecimal cash_fee;
    @Column(name = "wexin_pay_time_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time_end;

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public BigDecimal getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(BigDecimal cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getTime_end() {
        return time_end;
    }

    public void setTime_end(Date time_end) {
        this.time_end = time_end;
    }
}
