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
import com.school.eneity.Class;
import com.school.service.ClassService;
import com.util.Result;
@RestController
@RequestMapping("/sms/clazzController")
public class ClassController {
	
	@Autowired
	private ClassService classService;
	/**
	 * 
	 * 描述:分页带条件查询
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getClazzsByOpr/{pageNo}/{pageInfo}")
	public Result getClazzsByOpr(@PathVariable("pageNo") Integer pageNo,@PathVariable("pageInfo") Integer pageInfo,
			String gradeName,String name) {
		Page<Class> page = new Page<>(pageNo, pageInfo);
		LambdaQueryWrapper<Class> lqw = new LambdaQueryWrapper<>();
//		if (StringUtils.isBlank(name)) {
//			
//		}else if (StringUtils.isBlank(gradeName)) {
//			
//		}else {
//			lqw.like(Class::getName, name);
//			lqw.like(Class::getGradeName, gradeName);
//		}
		if (!StringUtils.isBlank(name)) {
			lqw.like(Class::getName, name);
		}
		if (!StringUtils.isBlank(gradeName)) {
			lqw.like(Class::getGradeName, gradeName);
		}
		Page<Class> page2 = classService.page(page, lqw);
		return Result.ok(page2);
		
	}
	
	@PostMapping("/saveOrUpdateClazz")
	public Result saveOrUpdate(@RequestBody Class clazz) {
		classService.saveOrUpdate(clazz);
		return Result.ok();

	}
	
	@DeleteMapping("/deleteClazz")
	public Result delete(@RequestBody List<Integer> id) {
		classService.removeByIds(id);
		return Result.ok();
		
	}
}
