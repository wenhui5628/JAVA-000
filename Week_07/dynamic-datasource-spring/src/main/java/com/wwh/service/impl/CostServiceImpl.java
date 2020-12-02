package com.wwh.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wwh.annotation.DynamicRoutingDataSource;
import com.wwh.bean.Cost;
import com.wwh.mapper.CostMapper;
import com.wwh.service.CostService;

@Service
public class CostServiceImpl implements CostService {

	@Resource
	private CostMapper costMapper;

	@Override
	public void insert(Cost cost)  {
		System.out.println("insert:" + costMapper.insert(cost));
	}

	@Override
	@DynamicRoutingDataSource("dataSource2")
	public void insert2(Cost cost)  {
		System.out.println("insert2:" + costMapper.insert(cost));
	}

	@Override
	@DynamicRoutingDataSource("dataSource3")
	public void insert3(Cost cost)  {
		System.out.println("insert3:" + costMapper.insert(cost));
	}
}