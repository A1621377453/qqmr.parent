package com.qingqingmr.qqmr.base.bo;

import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;

/**
 * 分销规则的规则类
 *
 * @author ztl
 * @datetime 2018-07-24 19:25:01
 */
public class DistributionRuleBO {
    private String name;
    private Integer inviteFrom;
    private Integer inviteTo;
    private Integer from;
    private Integer to;
    private Integer per;
    private Integer bonus;
    private Integer type;
    private Integer role;

    public Integer getInviteFrom() {
        return inviteFrom;
    }

    public void setInviteFrom(Integer inviteFrom) {
        this.inviteFrom = inviteFrom;
    }

    public Integer getInviteTo() {
        return inviteTo;
    }

    public void setInviteTo(Integer inviteTo) {
        this.inviteTo = inviteTo;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("per:" + per + ",bonus:" + bonus);

        if (type != 0 && type == AwardTypeEnum.NUMBER_AWARD.code) {
            sb.append(",inviteFrom:" + inviteFrom + ",inviteTo:" + inviteTo + ",from:" + from + ",to:" + to);
        }
        sb.append("}");
        return sb.toString();
    }
}
