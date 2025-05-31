package com.school.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.eneity.Class;
import com.school.mapper.ClassMapper;
import com.school.service.ClassService;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

}
