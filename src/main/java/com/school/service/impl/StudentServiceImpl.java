package com.school.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginForm;
import com.school.eneity.Student;
import com.school.mapper.StudentMapper;
import com.school.service.StudentService;
import com.util.MD5;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService{

	@Override
	public Student login(LoginForm loginForm) {
		// 判断用户名、密码是否匹配
				LambdaQueryWrapper<Student> lqw = new LambdaQueryWrapper<>();
				lqw.eq(Student::getName, loginForm.getUsername());
				// 密码加密 因为数据库也是加密
				lqw.eq(Student::getPassword, MD5.encrypt(loginForm.getPassword()));
				Student student = getOne(lqw);
				return student;
	}

}
