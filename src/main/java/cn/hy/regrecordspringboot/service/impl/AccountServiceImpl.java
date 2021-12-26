package cn.hy.regrecordspringboot.service.impl;

import cn.hy.regrecordspringboot.bean.entity.Account;
import cn.hy.regrecordspringboot.mapper.AccountMapper;
import cn.hy.regrecordspringboot.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountMapper accountMapper;

    @Override
    public Account selectPasswordByName(String userName) {
        return accountMapper.selectPasswordByName(userName);
    }
    @Override
    public Account selectPasswordByMail(String userMail) {
        return accountMapper.selectPasswordByMail(userMail);
    }
    @Override
    public Account selectPasswordByPhone(String userPhone) {
        return accountMapper.selectPasswordByPhone(userPhone);
    }
    @Override
    public Account selectPasswordByPrimaryKey(String uid){
        return accountMapper.selectPasswordByPrimaryKey(uid);
    }

    @Override
    public Account selectByPrimaryKey(String uid){
        return accountMapper.selectByPrimaryKey(uid);
    }

    @Override
    public Account selectForTokenByPrimaryKey(String uid) {
        return accountMapper.selectForTokenByPrimaryKey(uid);
    }

    @Override
    public Integer selectCountByAccountName(String userName){
        return accountMapper.selectCountByAccountName(userName);
    }

    @Override
    public Integer selectByNick(String nickname) {
        return accountMapper.selectByNick(nickname);
    }

    @Override
    public Integer insert(Account account){
        return accountMapper.insert(account);
    }

    @Override
    public Integer updatePasswordByPrimaryKey(String password, String uid) {
        return accountMapper.updatePasswordByPrimaryKey(password, uid);
    }

    @Override
    public Integer updatePhoneByPrimaryKey(String phone, String uid) {
        return accountMapper.updatePhoneByPrimaryKey(phone, uid);
    }

    @Override
    public Integer updateMailByPrimaryKey(String mail, String uid) {
        return accountMapper.updateMailByPrimaryKey(mail, uid);
    }

    @Override
    public Integer updateNickNameByPrimaryKey(String nickname, String uid) {
        return accountMapper.updateNickNameByPrimaryKey(nickname, uid);
    }
}
