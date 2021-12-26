package cn.hy.regrecordspringboot.mapper;

import cn.hy.regrecordspringboot.bean.entity.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    Account selectPasswordByName(@Param("account_name") String userName);
    Account selectPasswordByMail(@Param("account_mail") String userMail);
    Account selectPasswordByPhone(@Param("account_phone") String userPhone);
    Account selectPasswordByPrimaryKey(@Param("account_id") String uid);
    Account selectByPrimaryKey(@Param("accountId") String uid);
    Account selectForTokenByPrimaryKey(@Param("accountId") String uid);
    Integer selectCountByAccountName(@Param("account_name") String userName);
    Integer selectByNick(@Param("account_nick_name") String nickname);

    Integer insert(@Param("account") Account account);

    Integer updatePasswordByPrimaryKey(@Param("account_password") String password, @Param("account_id") String uid);
    Integer updatePhoneByPrimaryKey(@Param("account_phone") String phone, @Param("account_id") String uid);
    Integer updateMailByPrimaryKey(@Param("account_mail") String mail, @Param("account_id") String uid);
    Integer updateNickNameByPrimaryKey(@Param("account_nick_name") String nickname, @Param("account_id") String uid);
}
