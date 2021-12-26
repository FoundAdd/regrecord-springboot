package cn.hy.regrecordspringboot.bean.entity;

import java.util.Date;

public class Account {
    private Integer accountId;

    private String accountName;

    private String accountPassword;

    private String accountPhone;

    private String accountMail;

    private String accountNickName;

    private Date accountCreateTime;

    private String accountStatu = "100000";

    private Integer accountLevel = 0;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword == null ? null : accountPassword.trim();
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getAccountMail() {
        return accountMail;
    }

    public void setAccountMail(String accountMail) {
        this.accountMail = accountMail == null ? null : accountMail.trim();
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName == null ? null : accountNickName.trim();
    }

    public Date getAccountCreateTime() {
        return accountCreateTime;
    }

    public void setAccountCreateTime(Date accountCreateTime) {
        this.accountCreateTime = accountCreateTime;
    }

    public String getAccountStatu() {
        return accountStatu;
    }

    public void setAccountStatu(String accountStatu) {
        this.accountStatu = accountStatu == null ? null : accountStatu.trim();
    }

    public Integer getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(Integer accountLevel) {
        this.accountLevel = accountLevel == null ? null : accountLevel;
    }
}