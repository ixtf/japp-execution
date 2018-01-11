package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.data.EvaluateField;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-9.
 */
@Entity
@Table(name = "T_EVALUATETEMPLATE")
@NamedQueries({
        @NamedQuery(name = "EvaluateTemplate.queryByCreatorId", query = "SELECT o FROM EvaluateTemplate o WHERE o.creator.id=:creatorId"),
})
public class EvaluateTemplate extends AbstractLogable {
    @NotBlank
    private String name;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @NotBlank
    private String fieldsString;

    public static EvaluateTemplate fromSaveString(String s) {
        try {
            return StringUtils.isBlank(s) ? null : MAPPER.readValue(s, EvaluateTemplate.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toSaveString() {
        try {
            ArrayNode fields = MAPPER.createArrayNode();
            for (EvaluateField field : CollectionUtils.emptyIfNull(getFields())) {
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
    public Collection<EvaluateField> getFields() throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, EvaluateField.class);
        return MAPPER.readValue(fieldsString, type);
    }

    public void setFields(Collection<EvaluateField> fields) throws JsonProcessingException {
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
