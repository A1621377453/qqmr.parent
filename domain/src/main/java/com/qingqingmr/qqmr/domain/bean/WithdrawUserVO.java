package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;

/**
 * 提现列表的记录
 *
 * @author ztl
 * @datetime 2018-07-10 09:57:20
 */
public class WithdrawUserVO extends WithdrawalUser {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 真实姓名
     */
    private String realityName;
    /**
     * 处理人用户名
     */
    private String name;

    public String getNickName() {
        return Security.decodeHex(this.nickName);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealityName() {
        return realityName;
    }

    public void setRealityName(String realityName) {
        this.realityName = realityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
