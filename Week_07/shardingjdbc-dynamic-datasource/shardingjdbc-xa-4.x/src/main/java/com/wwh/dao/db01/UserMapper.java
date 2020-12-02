package com.wwh.dao.db01;

import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Update("update user_info set user_name = #{username} where id = #{uid}")
    int updateUserName(String username, Integer uid);
}
