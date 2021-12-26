package cn.hy.regrecordspringboot.service;

import cn.hy.regrecordspringboot.bean.entity.Account;

public interface AccountService {
    Account selectPasswordByName(String userName);
    Account selectPasswordByMail(String userMail);
    Account selectPasswordByPhone(String userPhone);
    Account selectPasswordByPrimaryKey(String uid);
    Account selectByPrimaryKey(String uid);
    Account selectForTokenByPrimaryKey(String uid);
    Integer selectCountByAccountName(String userName);
    Integer selectByNick(String nickname);

    Integer insert(Account account);

    Integer updatePasswordByPrimaryKey(String password, String uid);
    Integer updatePhoneByPrimaryKey(String phone, String uid);
    Integer updateMailByPrimaryKey(String mail, String uid);
    Integer updateNickNameByPrimaryKey(String nickname, String uid);
}
