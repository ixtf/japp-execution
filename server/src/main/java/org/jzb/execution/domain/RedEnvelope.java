package org.jzb.execution.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 描述：
 *
 * @author jzb 2018-04-03
 */
// @NamedQueries({
//         @NamedQuery(name = "RedEnvelope.queryByRedEnvelopeStrategy", query = "SELECT o FROM RedEnvelope o WHERE o.redEnvelopeStrategy=:redEnvelopeStrategy AND o.deleted=FALSE"),
// })
public class RedEnvelope extends AbstractLogable {
    @ManyToOne
    @NotNull
    private RedEnvelopeStrategy redEnvelopeStrategy;
    @Column(scale = 2)
    private BigDecimal amount;
    private boolean used;
    private String note;

    public RedEnvelopeStrategy getRedEnvelopeStrategy() {
        return redEnvelopeStrategy;
    }

    public void setRedEnvelopeStrategy(RedEnvelopeStrategy redEnvelopeStrategy) {
        this.redEnvelopeStrategy = redEnvelopeStrategy;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
