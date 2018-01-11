package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.domain.data.Field;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-9.
 */
@Entity
@Table(name = "T_FEEDBACKTEMPLATE")
@NamedQueries({
        @NamedQuery(name = "FeedbackTemplate.queryByCreatorId", query = "SELECT o FROM FeedbackTemplate o WHERE o.creator.id=:creatorId"),
})
public class FeedbackTemplate extends AbstractLogable {
    @NotBlank
    private String name;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @NotBlank
    private String fieldsString;

    public static FeedbackTemplate fromSaveString(String s) {
        try {
            return J.isBlank(s) ? null : MAPPER.readValue(s, FeedbackTemplate.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toSaveString() {
        try {
            ArrayNode fields = MAPPER.createArrayNode();
            for (Field field : J.emptyIfNull(getFields())) {
                fields.add(MAPPER.getNodeFactory().pojoNode(field));
            }
            JsonNode node = MAPPER.createObjectNode()
                    .put("id", id)
                    .put("name", name)
                    .set("fields", fields);
            return MAPPER.writeValueAsString(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonGetter
    public Collection<Field> getFields() throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, Field.class);
        return MAPPER.readValue(fieldsString, type);
    }

    public void setFields(Collection<Field> fields) throws JsonProcessingException {
        this.fieldsString = MAPPER.writeValueAsString(fields);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldsString() {
        return fieldsString;
    }

    public void setFieldsString(String fieldsString) {
        this.fieldsString = fieldsString;
    }

}
