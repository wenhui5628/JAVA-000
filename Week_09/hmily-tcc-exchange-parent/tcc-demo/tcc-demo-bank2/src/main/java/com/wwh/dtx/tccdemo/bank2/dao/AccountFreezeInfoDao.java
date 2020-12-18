package com.wwh.dtx.tccdemo.bank2.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/***
 * 冻结账户金额Dao
 */
@Mapper
@Component
public interface AccountFreezeInfoDao {
    @Insert("insert into account_freeze_info(freeze_no,account_no,freeze_amount) values(#{freezeNo},#{accountNo},#{freezeAmount})")
    int insertFreezeInfo(@Param("freezeNo") long freezeNo, @Param("accountNo") String accountNo, @Param("freezeAmount") Double freezeAmount);

    @Delete("delete from account_freeze_info where account_no=#{accountNo} and freeze_amount = #{freezeAmount}")
    int deleteFreezeInfo(@Param("accountNo") String accountNo, @Param("freezeAmount") Double freezeAmount);
}
