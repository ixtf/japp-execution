package org.jzb.execution.domain.extra;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractLogable;
import org.jzb.execution.domain.UploadFile;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_EXAMQUESTION")
@NamedQueries({
        @NamedQuery(name = "ExamQuestion.countByLab", query = "SELECT COUNT(o) FROM ExamQuestion o WHERE o.lab=:lab AND o.deleted=FALSE "),
        @NamedQuery(name = "ExamQuestion.queryByLabId", query = "SELECT o FROM ExamQuestion o WHERE o.lab.id=:labId AND o.deleted=FALSE "),
        @NamedQuery(name = "ExamQuestion.queryByMyLab", query = "SELECT o FROM ExamQuestion o WHERE o.lab IS NULL AND o.creator.id=:creatorId AND o.deleted=FALSE "),
})
public class ExamQuestion extends AbstractLogable {
    @ManyToOne
    private ExamQuestionLab lab;
    @NotBlank
    private String name;
    @ManyToOne
    private UploadFile question;
    @ManyToOne
    private UploadFile answer;
    @ElementCollection
    private Collection<String> tags;

    public ExamQuestionLab getLab() {
        return lab;
    }

    public void setLab(ExamQuestionLab lab) {
        this.lab = lab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadFile getQuestion() {
        return question;
    }

    public void setQuestion(UploadFile question) {
        this.question = question;
    }

    public UploadFile getAnswer() {
        return answer;
    }

    public void setAnswer(UploadFile answer) {
        this.answer = answer;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }
}
