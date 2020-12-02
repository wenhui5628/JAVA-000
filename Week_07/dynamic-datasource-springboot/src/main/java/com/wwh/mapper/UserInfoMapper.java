package com.wwh.mapper;

import com.wwh.annotation.TargetDataSource;
import com.wwh.pojo.UserInfo;


public interface UserInfoMapper {
	/**
	 * 从master数据源中获取用户信息
	 */
	//@TargetDataSource("master")
	UserInfo selectByOddUserId(Integer id);
	/**
	 * 从slave数据源中获取用户信息
	 */
	@TargetDataSource("slave")
	UserInfo selectByEvenUserId(Integer id);
}