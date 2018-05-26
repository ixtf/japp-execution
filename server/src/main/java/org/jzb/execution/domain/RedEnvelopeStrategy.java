package org.jzb.execution.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：
 *
 * @author jzb 2018-04-03
 */
@Entity
@Table(name = "T_REDENVELOPESTRATEGY")
@NamedQueries({
        @NamedQuery(name = "RedEnvelopeStrategy.queryByPaymentMerchant", query = "SELECT o FROM RedEnvelopeStrategy o WHERE o.paymentMerchant=:paymentMerchant AND o.deleted=FALSE"),
})
public class RedEnvelopeStrategy extends AbstractLogable {
    @ManyToOne
    @NotNull
    private PaymentMerchant paymentMerchant;
    @NotBlank
    private String name;
    @Min(1)
    private int count;
    @Column(scale = 2)
    private BigDecimal min;
    private BigDecimal max;
    @Temporal(TemporalType.DATE)
    private Date expireDate;
    private String note;

    public PaymentMerchant getPaymentMerchant() {
        return paymentMerchant;
    }

    public void setPaymentMerchant(PaymentMerchant paymentMerchant) {
        this.paymentMerchant = paymentMerchant;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
