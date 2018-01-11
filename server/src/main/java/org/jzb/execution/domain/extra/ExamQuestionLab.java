package org.jzb.execution.domain.extra;

import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.domain.AbstractLogable;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_EXAMQUESTIONLAB")
@NamedQueries({
        @NamedQuery(name = "ExamQuestionLab.queryByParticipant", query = "SELECT o FROM ExamQuestionLab o WHERE :participant MEMBER OF o.participants AND o.deleted=FALSE "),
        @NamedQuery(name = "ExamQuestionLab.queryByCreator", query = "SELECT o FROM ExamQuestionLab o WHERE o.creator=:creator AND o.deleted=FALSE "),
})
public class ExamQuestionLab extends AbstractLogable {
    @NotBlank
    private String name;
    @ManyToMany
    @JoinTable(name = "T_EXAMQUESTIONLAB_T_PARTICIPANT")
    private Collection<Operator> participants;

    public boolean isManager(Principal principal) {
        Set<Operator> operators = Sets.newHashSet();
        operators.add(getCreator());
        operators.add(getModifier());
        operators.addAll(J.emptyIfNull(participants));
        return operators.parallelStream()
                .filter(it -> it.getId().equals(principal.getName()))
                .findFirst()
                .isPresent();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Operator> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<Operator> participants) {
        this.participants = participants;
    }
}
