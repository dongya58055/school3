package com.school.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginForm;
import com.school.eneity.Teacher;
import com.school.mapper.TeacherMapper;
import com.school.service.TeacherService;
import com.util.MD5;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{

	@Override
	public Teacher login(LoginForm loginForm) {
		// 判断用户名、密码是否匹配
				LambdaQueryWrapper<Teacher> lqw = new LambdaQueryWrapper<>();
				lqw.eq(Teacher::getName, loginForm.getUsername());
				// 密码加密 因为数据库也是加密
				lqw.eq(Teacher::getPassword, MD5.encrypt(loginForm.getPassword()));
				Teacher teacher = getOne(lqw);
				return teacher;
	}

}
