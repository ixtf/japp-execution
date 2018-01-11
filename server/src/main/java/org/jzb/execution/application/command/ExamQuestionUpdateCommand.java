package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by jzb on 17-4-15.
 */

public class ExamQuestionUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    private EntityDTO question;
    @NotNull
    private EntityDTO answer;
    private Set<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityDTO getQuestion() {
        return question;
    }

    public void setQuestion(EntityDTO question) {
        this.question = question;
    }

    public EntityDTO getAnswer() {
        return answer;
    }

    public void setAnswer(EntityDTO answer) {
        this.answer = answer;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}