package cn.hy.regrecordspringboot.mapper;

import cn.hy.regrecordspringboot.bean.entity.RegRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordMapper {
    List<RegRecord> selectByAccountId(@Param("accountId") Integer uid, @Param("index") Integer index, @Param("limit") Integer limit);

    Integer selectCount(@Param("accountId") Integer uid);

    Integer deleteRecords(@Param("accountId") Integer uid, @Param("reg_id_array") Integer[] reg_id_arr);

    Integer insert(@Param("record") RegRecord regRecord);

    Integer deleteByPKAndFK(@Param("regId") Integer reg_id, @Param("accountId") Integer account_id);

    Integer updateByPKAndFK(@Param("record") RegRecord regRecord);
}
