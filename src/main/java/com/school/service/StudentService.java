package com.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.LoginForm;
import com.school.eneity.Student;

public interface StudentService extends IService<Student> {

	Student login(LoginForm loginForm);

}
