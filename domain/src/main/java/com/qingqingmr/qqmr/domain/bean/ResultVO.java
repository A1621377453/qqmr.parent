package com.qingqingmr.qqmr.domain.bean;

/**
 * mapper.xml返回实体类
 * （方法和mapper.xml之间传递数据）
 * @author crn
 * @datetime 2018-07-11 20:16:13
 */
public class ResultVO {
    private Integer type;
    private Double amount;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
