package com.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.LoginForm;
import com.school.eneity.Teacher;

public interface TeacherService extends IService<Teacher> {

	Teacher login(LoginForm loginForm);

}
