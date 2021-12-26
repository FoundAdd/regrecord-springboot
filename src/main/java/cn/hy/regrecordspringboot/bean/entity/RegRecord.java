package cn.hy.regrecordspringboot.bean.entity;

import java.util.Date;

public class RegRecord {
    private Integer regId;

    private String regName;

    private String regAddress;

    private String regPhone;

    private String regPassword;

    private Date regCreateTime;

    private Date regModifyTime;

    private Integer accountId;

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName == null ? null : regName.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public String getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(String regPhone) {
        this.regPhone = regPhone;
    }

    public String getRegPassword() {
        return regPassword;
    }

    public void setRegPassword(String regPassword) {
        this.regPassword = regPassword == null ? null : regPassword.trim();
    }

    public Date getRegCreateTime() {
        return regCreateTime;
    }

    public void setRegCreateTime(Date regCreateTime) {
        this.regCreateTime = regCreateTime;
    }

    public Date getRegModifyTime() {
        return regModifyTime;
    }

    public void setRegModifyTime(Date regModifyTime) {
        this.regModifyTime = regModifyTime;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}