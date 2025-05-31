package com.school.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.eneity.Student;

@Mapper
public interface StudentMapper extends BaseMapper<Student>{

}
