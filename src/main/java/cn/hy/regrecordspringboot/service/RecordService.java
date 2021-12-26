package cn.hy.regrecordspringboot.service;

import cn.hy.regrecordspringboot.bean.entity.RegRecord;

import java.util.List;

public interface RecordService {
    List<RegRecord> selectByAccountId(String uid, Integer index, Integer limit);

    Integer selectCount(String uid);

    Integer deleteRecords(String uid, List<Integer> reg_id_arr);

    Integer insert(RegRecord regRecord);

    Integer deleteByPKAndFK(String reg_id, String account_id);

    Integer updateByPKAndFK(RegRecord regRecord);
}
