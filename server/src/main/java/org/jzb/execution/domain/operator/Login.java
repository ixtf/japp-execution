/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain.operator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_LOGIN")
@NamedQueries({
        @NamedQuery(name = "Login.findByLoginId", query = "SELECT o FROM Login o WHERE o.loginId=:loginId")
})
public class Login extends AbstractEntity {

    @OneToOne
    @PrimaryKeyJoinColumn
    private Operator operator;
    @Column(unique = true, length = 20)
    @NotBlank
    @Size(min = 1, max = 20)
    private String loginId;
    @JsonIgnore
    @XmlTransient
    private String loginPassword;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}