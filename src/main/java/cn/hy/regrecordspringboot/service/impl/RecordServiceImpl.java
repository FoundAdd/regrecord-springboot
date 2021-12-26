package cn.hy.regrecordspringboot.service.impl;

import cn.hy.regrecordspringboot.bean.entity.RegRecord;
import cn.hy.regrecordspringboot.mapper.RecordMapper;
import cn.hy.regrecordspringboot.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    RecordMapper recordMapper;

    @Override
    public List<RegRecord> selectByAccountId(String uid , Integer index, Integer limit) {
        return recordMapper.selectByAccountId(Integer.valueOf(uid), index, limit);
    }

    @Override
    public Integer selectCount(String uid) {
        return recordMapper.selectCount(Integer.valueOf(uid));
    }

    @Override
    public Integer deleteRecords(String uid, List<Integer> reg_id_arr){
        return recordMapper.deleteRecords(Integer.valueOf(uid), reg_id_arr.toArray(new Integer[0]));
    }

    @Override
    public Integer insert(RegRecord regRecord){
        return recordMapper.insert(regRecord);
    }

    @Override
    public Integer deleteByPKAndFK(String reg_id, String account_id) {
        return recordMapper.deleteByPKAndFK(Integer.valueOf(reg_id), Integer.valueOf(account_id));
    }

    @Override
    public Integer updateByPKAndFK(RegRecord regRecord) {
        return recordMapper.updateByPKAndFK(regRecord);
    }
}
