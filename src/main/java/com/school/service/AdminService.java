package com.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.LoginForm;
import com.school.eneity.Admin;
public interface AdminService extends IService<Admin>{

	Admin login(LoginForm loginForm);

	

}
