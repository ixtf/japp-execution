package org.jzb.execution.domain.operator;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractEntity;
import org.jzb.weinxin.open.authorizeCode.OpenAuthorizeUnionInfoResponse;
import org.jzb.weixin.mp.authorizeCode.MpAuthorizeUnionInfoResponse;
import org.jzb.weixin.mp.unionInfo.MpUnionInfoResponse;

import javax.persistence.*;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_WEIXINOPERATOR")
@NamedQueries({
        @NamedQuery(name = "WeixinOperator.findByOperatorId", query = "SELECT o FROM WeixinOperator o WHERE o.operator.id=:operatorId"),
        @NamedQuery(name = "WeixinOperator.findByOpenid", query = "SELECT o FROM WeixinOperator o WHERE o.id=:openid"),
        @NamedQuery(name = "WeixinOperator.findByUnionid", query = "SELECT o FROM WeixinOperator o WHERE o.unionid=:unionid"),
})
public class WeixinOperator extends AbstractEntity {
    @ManyToOne
    protected Operator operator;
//    /**
//     * 发送微信消息时，指定的用户id
//     */
//    @Column(unique = true)
//    @NotBlank
//    private String openid;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    @NotBlank
    private String unionid;
    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    private int subscribe;
    private String nickname;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private int sex;
    @Column(length = 50)
    private String city;
    @Column(length = 50)
    private String country;
    @Column(length = 50)
    private String province;
    /**
     * 用户的语言，简体中文为zh_CN
     */
    @Column(length = 40)
    private String language;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * <p>
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    private String headimgurl;
    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    private long subscribe_time;

    public WeixinOperator(MpUnionInfoResponse res) {
        this.setUnionInfo(res);
    }

    public WeixinOperator() {
    }

    public void setUnionInfo(MpUnionInfoResponse res) {
        this.id = res.openid();
        this.unionid = res.unionid();
        this.subscribe = res.subscribe();
        this.headimgurl = res.headimgurl();
        this.nickname = res.nickname();
        this.sex = res.sex();
        this.city = res.city();
        this.country = res.country();
        this.province = res.province();
        this.language = res.language();
        this.subscribe_time = res.subscribe_time();
    }

    public void setUnionInfo(MpAuthorizeUnionInfoResponse res) {
        this.id = res.openid();
        this.unionid = res.unionid();
        this.headimgurl = res.headimgurl();
        this.nickname = res.nickname();
        this.sex = res.sex();
        this.city = res.city();
        this.country = res.country();
        this.province = res.province();
    }

    public void setUnionInfo(OpenAuthorizeUnionInfoResponse res) {
        this.id = res.openid();
        this.unionid = res.unionid();
        this.headimgurl = res.headimgurl();
        this.nickname = res.nickname();
        this.sex = res.sex();
        this.city = res.city();
        this.country = res.country();
        this.province = res.province();
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public long getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public boolean isSubscribe() {
        return this.subscribe != 0;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }
}
