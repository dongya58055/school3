package com.school.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.eneity.Grade;
import com.school.mapper.GradeMapper;
import com.school.service.GradeService;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService{

	@Override
	public IPage<Grade> getGrade(Page<Grade> page, String gradeName) {
		return null;
	}

}
