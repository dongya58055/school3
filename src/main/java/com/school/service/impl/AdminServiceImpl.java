package com.school.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginForm;
//import com.dto.LoginForm;
import com.school.eneity.Admin;
import com.school.mapper.AdminMapper;
import com.school.service.AdminService;
import com.util.MD5;
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService{
	
	
 
	@Override
	public Admin login(LoginForm loginForm) {

		// 判断用户名、密码是否匹配
		LambdaQueryWrapper<Admin> lqw = new LambdaQueryWrapper<>();
		lqw.eq(Admin::getName, loginForm.getUsername());
		// 密码加密 因为数据库也是加密
		lqw.eq(Admin::getPassword, MD5.encrypt(loginForm.getPassword()));
		Admin admin = getOne(lqw);
		return admin;
	}

//	@Autowired
//	AdminService as;
//	@Override
//	public Admin login(LoginForm login) {
//		
//		LambdaQueryWrapper<Admin> lqw = new LambdaQueryWrapper<>();
//		lqw.eq(Admin::getName, login.getUsername());
//		lqw.eq(Admin::getPassword,MD5.encrypt(login.getPassword()));
//		Admin admin = this.getOne(lqw);
//		//Admin admin = baseMapper.selectOne(lqw);
//		return admin;
//	}

}
