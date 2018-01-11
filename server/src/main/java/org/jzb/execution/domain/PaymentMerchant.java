package org.jzb.execution.domain;

import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_PAYMENTMERCHANT")
@NamedQueries({
        @NamedQuery(name = "PaymentMerchant.queryByManager", query = "SELECT o FROM PaymentMerchant o WHERE :manager MEMBER OF o.managers AND o.deleted=FALSE"),
})
public class PaymentMerchant extends AbstractLogable {
    @NotBlank
    private String name;
    @NotBlank
    private String sub_mch_id;
    private String sub_appid;
    @ManyToMany
    @JoinTable(name = "T_PAYMENTMERCHANT_T_MANAGER")
    private Set<Operator> managers;

    public boolean isManager(Operator operator) {
        return isManager(operator.id);
    }

    public boolean isManager(Principal principal) {
        return isManager(principal.getName());
    }

    private boolean isManager(String operatorId) {
        final HashSet<Operator> managers = Sets.newHashSet(this.getCreator(), this.getModifier());
        managers.addAll(J.emptyIfNull(this.managers));
        return managers.stream()
                .parallel()
                .filter(Objects::nonNull)
                .map(Operator::getId)
                .filter(id -> Objects.equals(id, operatorId))
                .findFirst()
                .isPresent();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public Set<Operator> getManagers() {
        return managers;
    }

    public void setManagers(Set<Operator> managers) {
        this.managers = managers;
    }
}
