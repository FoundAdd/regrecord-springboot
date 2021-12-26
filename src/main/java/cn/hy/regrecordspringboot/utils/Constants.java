package cn.hy.regrecordspringboot.utils;

/***
 * 常量类 放置一些常量
 */
public class Constants {
    // 默认起始页
    public static final Integer DEFAULT_PAGE_NO = 1;
    // 默认页大小
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    // 手机验证码 发送 成功
    public static final String SMS_PHONE_SUCCESS = "100000";
    // 手机验证码 发送 失败
    public static final String SMS_PHONE_FAILD = "100001";
    // 手机验证码 验证 成功
    public static final String SMS_PHONE_VERIFY_SUCCESS = "100002";
    // 手机验证码 验证 失败
    public static final String SMS_PHONE_VERIFY_FAILD = "100003";
    // 邮件验证码 发送 成功
    public static final String SMS_MAIL_SUCCESS = "100010";
    // 邮件验证码 发送 失败
    public static final String SMS_MAIL_FAILD= "100011";
    // 邮件验证码 验证 成功
    public static final String SMS_MAIL_VERIFY_SUCCESS = "100012";
    // 邮件验证码 验证 失败
    public static final String SMS_MAIL_VERIFY_FAILD = "100013";

    // 登录成功
    public static final String LOGIN_SUCCESS = "110000";
    // 登录失败，密码错误
    public static final String LOGIN_PASSWORD_ERROR = "110001";
    // 登录失败，用户被ban
    public static final String LOGIN_ACCOUNT_BANED = "110002";
    // 登录失败，其他错误，联系管理员处理
    public static final String LOGIN_FAILD = "110003";
    // 登录失败，用户名不存在
    public static final String LOGIN_ACCOUNT_NOT_EXIST = "110004";

    // 注册成功
    public static final String REGISTER_SUCCESS = "120000";
    // 用户已存在
    public static final String REGISTER_ACCOUNT_EXIST = "120001";
    // 用户名格式不正确
    public static final String REGISTER_ACCOUNT_ERROR = "120002";
    // IP被ban
    public static final String REGISTER_IP_BANED = "120003";
    // 注册失败，其他错误
    public static final String REGISTER_FAILD = "120004";

    // 账户查询成功
    public static final String SELECT_SUCCESS = "130000";
    // 账户查询结果为空
    public static final String SELECT_FAILD_EMPTY = "130001";

    // token 校验成功
    public static final String TOKEN_VERIFY_SUCCESS = "140000";
    // token 校验失败
    public static final String TOKEN_VERIFY_FAILD = "140001";
    // token 过期
    public static final String TOKEN_VERIFY_EXPIRE = "140002";

    // 密码校验正确
    public static final String PASSWORD_VERIFY_SUCCESS = "150000";
    // 密码校验失败
    public static final String PASSWORD_VERIFY_FAILED = "150001";
    // 密码校验出错(其他错误)
    public static final String PASSWORD_VERIFY_ERROR = "150002";
    // 密码修改正确
    public static final String PASSWORD_MODIFY_SUCCESS = "150010";
    // 密码修改失败
    public static final String PASSWORD_MODIFY_FAILED = "150011";

    // 账户信息修改成功
    public static final String UPDATE_SUCCESS = "160010";
    // 账户信息修改失败
    public static final String UPDATE_FAILED = "160011";

    // 昵称可用
    public static final String NICK_PASS = "170000";
    // 昵称重复
    public static final String NICK_REPEAT = "170001";
    // 昵称不合法（特殊符号、敏感词等）
    public static final String NICK_ILLEGAL = "170002";

    // 记录查询成功
    public static final String RECORD_SELECT_SUCCESS = "200000";
    // 记录查询失败
    public static final String RECORD_SELECT_FAILD = "200001";

    // 记录删除成功
    public static final String RECORD_DELETE_SUCCESS = "200000";
    // 记录删除失败
    public static final String RECORD_DELETE_FAILD = "200001";
}
