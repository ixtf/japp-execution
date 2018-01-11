package org.jzb.execution.domain.extra;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractLogable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_EXAM")
public class Exam extends AbstractLogable {
    @NotBlank
    private String title;
    @JsonIgnore
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @Size(min = 1)
    private Collection<ExamItem> items;

    @JsonGetter
    @Transient
    public Collection<ExamQuestion> getExamQuestions() {
        List<ExamItem> sortedItems = Lists.newArrayList(items);
        Collections.sort(sortedItems);
        List<ExamQuestion> examQuestions = Lists.newArrayList();
        for (ExamItem item : sortedItems) {
            examQuestions.add(item.getExamQuestion());
        }
        return examQuestions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<ExamItem> getItems() {
        return items;
    }

    public void setItems(Collection<ExamItem> items) {
        this.items = items;
    }
}
