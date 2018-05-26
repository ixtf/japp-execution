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

/**
 * 描述：
 *
 * @author jzb 2018-04-03
 */
@Entity
@Table(name = "T_REDENVELOPEORGANIZATION")
@NamedQueries({
        @NamedQuery(name = "RedEnvelopeOrganization.queryByManager", query = "SELECT o FROM RedEnvelopeOrganization o WHERE :manager MEMBER OF o.managers AND o.deleted=FALSE"),
})
public class RedEnvelopeOrganization extends AbstractLogable {
    @NotBlank
    private String name;
    @ManyToMany
    @JoinTable(name = "T_REDENVELOPEORGANIZATION_T_MANAGER")
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

    public Set<Operator> getManagers() {
        return managers;
    }

    public void setManagers(Set<Operator> managers) {
        this.managers = managers;
    }
}
