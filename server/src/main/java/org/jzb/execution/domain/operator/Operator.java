/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain.operator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.security.Principal;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_OPERATOR")
public class Operator extends AbstractEntity {
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String name;
    private String nickName;
    private String avatar;

    public static boolean isAdmin(Principal principal) {
        return principal.getName().equals("N8nKbXJtvBfzarKt3piZ34") || principal.getName().equals("Jmg62HxQV2mzUGM8Qqnq1U");
    }

    @JsonIgnore
    public boolean isAdmin() {
        return id.equals("N8nKbXJtvBfzarKt3piZ34") || id.equals("Jmg62HxQV2mzUGM8Qqnq1U");
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "name='" + name + '\'' +
                '}';
    }
}
