package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */

public class ExamUpdateCommand implements Serializable {
    @NotBlank
    private String title;
    @NotNull
    @Size(min = 1)
    private List<EntityDTO> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<EntityDTO> getItems() {
        return items;
    }

    public void setItems(List<EntityDTO> items) {
        this.items = items;
    }

}