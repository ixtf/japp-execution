package org.jzb.execution.domain.extra;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.execution.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_EXAMITEM")
public class ExamItem extends AbstractEntity implements Comparable<ExamItem> {
    @JsonIgnore
    @ManyToOne
    private Exam exam;
    @ManyToOne
    private ExamQuestion examQuestion;
    private int sortBy;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamQuestion getExamQuestion() {
        return examQuestion;
    }

    public void setExamQuestion(ExamQuestion examQuestion) {
        this.examQuestion = examQuestion;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public int compareTo(ExamItem o) {
        return Integer.compare(sortBy, o.sortBy);
    }
}
