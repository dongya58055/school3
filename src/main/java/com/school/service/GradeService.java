package com.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.eneity.Grade;

public interface GradeService extends IService<Grade>{

	IPage<Grade> getGrade(Page<Grade> page, String gradeName);

}
