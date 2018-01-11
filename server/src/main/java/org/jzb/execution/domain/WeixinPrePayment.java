package org.jzb.execution.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：
 * 微信预付凭证
 *
 * @author jzb
 * @create 2017-10-24
 */
@Embeddable
public class WeixinPrePayment implements Serializable {
    @Column(name = "wexin_prepay_appid")
    @NotBlank
    private String appid;
    @Column(name = "wexin_prepay_mch_id")
    @NotBlank
    private String mch_id;
    @Column(name = "wexin_prepay_sub_mch_id")
    @NotBlank
    private String sub_mch_id;
    @Column(name = "wexin_prepay_openid")
    @NotBlank
    private String openid;
    @Column(name = "wexin_prepay_total_fee", precision = 12, scale = 2)
    @NotNull
    @Min(1)
    private BigDecimal total_fee;
    @Column(name = "wexin_prepay_trade_type")
    @NotBlank
    private String trade_type;
    @Column(name = "wexin_prepay_prepay_id")
    @NotBlank
    private String prepay_id;
    @Column(name = "wexin_prepay_code_url")
    private String code_url;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public BigDecimal getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(BigDecimal total_fee) {
        this.total_fee = total_fee;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
