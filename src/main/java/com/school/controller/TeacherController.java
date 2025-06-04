package com.school.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.eneity.Teacher;
import com.school.service.TeacherService;
import com.util.MD5;
import com.util.Result;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
	@Autowired
	private TeacherService teacherService;

    
	/**
	 * 描述: 分页插件
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getTeachers/{pageNo}/{pageInfo}")
	public Result getStudentByOpr(@PathVariable("pageNo") Integer pageNo,@PathVariable("pageInfo") Integer pageInfo,
			@RequestParam(value = "name",required = false) String name) {
		Page<Teacher> page = new Page<>(pageNo,pageInfo);
		LambdaQueryWrapper<Teacher> lqw = new LambdaQueryWrapper<>();
		lqw.like(StringUtils.isNotBlank(name), Teacher::getName, name);
		lqw.orderByDesc(Teacher::getId);
		Page<Teacher> page2 = teacherService.page(page, lqw);
		return Result.ok(page2);
	}
	/**
	 * 描述:添加更新
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/saveOrUpdateTeacher")
	public Result saveOrUpdate (@RequestBody Teacher teacher) {
		//判断更新还是添加 没有ID说明是修改
		if (teacher.getId() != null) {
			
		}else {
			teacher.setPassword(MD5.encrypt(teacher.getPassword()));
		}
		teacherService.saveOrUpdate(teacher);
		return Result.ok();
		
	}
	
	@DeleteMapping("/deleteTeacher")
	public Result delete(@RequestBody List<Integer> id) {
		teacherService.removeByIds(id);
		return Result.ok();
		
	}
}
