package com.school.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.eneity.Grade;
import com.school.service.GradeService;
import com.school.service.impl.GradeServiceImpl;
import com.util.Result;


@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    private final GradeServiceImpl gradeServiceImpl;
	
	@Autowired
	private GradeService gradeService;

    GradeController(GradeServiceImpl gradeServiceImpl) {
        this.gradeServiceImpl = gradeServiceImpl;
    }
	
	/**
	 * 描述:分页管理
	 * @param 参数说明:@PathVariable 用途：用于获取 RESTful 风格 URL 中的变量。
	 * @RequestParam来源：URL 查询参数（?key=value），表单参数，或POST请求体。 用途：获取请求参数中的值。
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getGrades/{pageNo}/{pageSize}")
	public Result getGrades(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize,
			String gradeName) {
		// 分页 带条件查询
		Page<Grade> page = new Page<>(pageNo, pageSize);
		// 通过服务处处理
		// IPage<Grade> page2 = gradeService.getGrade(page,gradeName);
		LambdaQueryWrapper<Grade> lqw = new LambdaQueryWrapper<>();
		lqw.like(StringUtils.isNotBlank(gradeName), Grade::getName, gradeName);
		lqw.orderByDesc(Grade::getId);
		Page<Grade> page2 = gradeService.page(page, lqw);
		return Result.ok(page2);
	}
	/**
	 * 描述:
	 * @param 参数说明: json请求体
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/saveOrUpdateGrade")
	public Result saveOrUpdateGrade(@RequestBody Grade grade) {
		//接收参数
		//调用服务层方法完成增加或者修改
		gradeService.saveOrUpdate(grade);
		return Result.ok();
	}
	/**
	 * 描述:json的数据
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@DeleteMapping("/deleteGrade")
	public Result delete(@RequestBody List<Integer> id) {
		gradeService.removeByIds(id);
		return Result.ok();
	}
	/**
	 * 
	 * 描述:
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getGrades")
	public Result getGrades() {
		List<Grade> list = gradeService.list();
		return Result.ok(list);
		
	}
	
}
