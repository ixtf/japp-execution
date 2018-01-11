package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by jzb on 17-4-30.
 */
public class ExamQuestionJoinCommand {
    @NotNull
    @Size(min = 1)
    private Set<EntityDTO> examQuestions;

    public Set<EntityDTO> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(Set<EntityDTO> examQuestions) {
        this.examQuestions = examQuestions;
    }
}
