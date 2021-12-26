package cn.hy.regrecordspringboot.controller;

import cn.hy.regrecordspringboot.bean.entity.Account;
import cn.hy.regrecordspringboot.bean.entity.RegRecord;
import cn.hy.regrecordspringboot.exception.InsertRecordException;
import cn.hy.regrecordspringboot.exception.SelectDataBaseException;
import cn.hy.regrecordspringboot.service.AccountService;
import cn.hy.regrecordspringboot.service.RecordService;
import cn.hy.regrecordspringboot.utils.Constants;
import cn.hy.regrecordspringboot.utils.TokenHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class RecordController {
    @Autowired
    AccountService accountService;
    @Autowired
    RecordService recordService;

    @RequestMapping(value = "/accountInfo/record")
    public ModelAndView recordPage() {
        return new ModelAndView("recordList");
    }

    @RequestMapping(value = "/accountInfo/record/{uid}", produces="application/json;charset=gbk")
    public String recordList(@PathVariable("uid") String uid, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("token") String token) {
        Account account = null;
        List<RegRecord> regRecords = null;
        List<Map> data = new ArrayList();
        Map<String, Object> map = new HashMap();
        if (uid != null) {  // 判断uid是否为空，防止非法访问
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            Integer recordSize = recordService.selectCount(uid);
            if (recordSize > 0) {
                regRecords = recordService.selectByAccountId(uid, (page-1) * limit, limit);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (RegRecord regRecord : regRecords) {
                    Map<String, Object> tmp = new HashMap();
                    tmp.put("reg_id", regRecord.getRegId().toString());
                    tmp.put("reg_name", regRecord.getRegName());
                    tmp.put("reg_address", regRecord.getRegAddress());
                    tmp.put("reg_phone", regRecord.getRegPhone());
                    tmp.put("reg_password", regRecord.getRegPassword());
                    tmp.put("reg_create_time", dateFormat.format(regRecord.getRegCreateTime()));
                    tmp.put("reg_modify_time", dateFormat.format(regRecord.getRegModifyTime()));

                    data.add(tmp);
                }

                map.put("code", Constants.RECORD_SELECT_SUCCESS);
                map.put("msg", "query success");
                map.put("count", recordSize);
                map.put("data", data);
            } else {
                map.put("code", Constants.RECORD_SELECT_FAILD);
                map.put("msg", "no result");
                map.put("count", 0);
            }
        } else {
            throw new SelectDataBaseException("查询列表失败, 用户 id 或 token 非法");
        }

        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/accountInfo/record/add",method = RequestMethod.PUT, produces = "application/json;charset=gbk")
    public String insertRecord(@RequestBody String data) {
        Account account = null;
        RegRecord regRecord;
        JSONObject jsonObject = JSON.parseObject(data);
        String uid = jsonObject.getString("uid");
        String token = jsonObject.getString("token");

        if (uid != null) {
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            regRecord = new RegRecord();
            regRecord.setRegName(jsonObject.getString("reg_name"));
            regRecord.setRegAddress(jsonObject.getString("reg_address"));
            regRecord.setRegPhone(jsonObject.getString("reg_phone"));
            regRecord.setRegPassword(jsonObject.getString("reg_password"));
            regRecord.setRegCreateTime(new Date());
            regRecord.setRegModifyTime(new Date());
            regRecord.setAccountId(Integer.valueOf(uid));

            if (recordService.insert(regRecord) < 1){
                throw new InsertRecordException("待添加的数据格式有误");
            } else {
                return "";
            }
        } else {
            throw new SelectDataBaseException("非法的用户id或token");
        }
    }

    @RequestMapping(value = "/accountInfo/record/modify",method = RequestMethod.PUT, produces = "application/json;charset=gbk")
    public String updateRecord(@RequestBody String data) {
        Account account = null;
        RegRecord regRecord;
        JSONObject jsonObject = JSON.parseObject(data);
        String uid = jsonObject.getString("uid");
        String token = jsonObject.getString("token");

        if (uid != null) {
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            regRecord = new RegRecord();
            regRecord.setRegId(jsonObject.getInteger("reg_id"));
            regRecord.setRegName(jsonObject.getString("reg_name"));
            regRecord.setRegAddress(jsonObject.getString("reg_address"));
            regRecord.setRegPhone(jsonObject.getString("reg_phone"));
            regRecord.setRegPassword(jsonObject.getString("reg_password"));
            regRecord.setRegModifyTime(new Date());
            regRecord.setAccountId(Integer.valueOf(uid));

            if (recordService.updateByPKAndFK(regRecord) < 1){
                throw new InsertRecordException("待修改的数据格式有误");
            } else {
                return "";
            }
        } else {
            throw new SelectDataBaseException("非法的用户id或token");
        }
    }

    @RequestMapping(value = "/accountInfo/record/del", method = RequestMethod.DELETE, produces = "application/json;charset=gbk")
    public String deleteRecord(@RequestBody String data) {
        Account account = null;
        JSONObject jsonObject = JSON.parseObject(data);
        String uid = jsonObject.getString("uid");
        String token = jsonObject.getString("token");

        if (uid != null) {
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            if (recordService.deleteByPKAndFK(jsonObject.getString("reg_id"), uid) < 1){
                throw new InsertRecordException("删除失败");
            } else {
                return null;
            }
        } else {
            throw new SelectDataBaseException("非法的用户id或token");
        }
    }

    @RequestMapping(value = "/accountInfo/record/delete", method = RequestMethod.DELETE, produces = "application/json;charset=gbk")
    public String deleteRecords(@RequestBody String data) {
        Account account = null;
        JSONObject jsonObject = JSON.parseObject(data);
        String uid = jsonObject.getString("uid");
        String token = jsonObject.getString("token");

        if (uid != null) {
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            List<Integer> records = jsonObject.getJSONArray("reg_id_arr").toJavaList(Integer.class);
            Integer delResult = recordService.deleteRecords(uid, records);
            Map<String, Object> response = new HashMap();
            if (delResult > 0) {
                response.put("code", Constants.RECORD_DELETE_SUCCESS);
                response.put("delNum", delResult);
            } else{
                response.put("code", Constants.RECORD_DELETE_FAILD);
            }
            return JSON.toJSONString(response);
        } else {
            throw new InsertRecordException("非法的用户id或token");
        }
    }
}
