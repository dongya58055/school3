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
import com.school.eneity.Student;
import com.school.service.StudentService;
import com.school.service.impl.StudentServiceImpl;
import com.util.MD5;
import com.util.Result;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
	
	@Autowired
	private StudentService studentService;

    StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }
	/**
	 * 描述: 分页插件
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getStudentByOpr/{pageNo}/{pageInfo}")
	public Result getStudentByOpr(@PathVariable("pageNo") Integer pageNo,@PathVariable("pageInfo") Integer pageInfo,
			@RequestParam(value = "name",required = false) String name) {
		Page<Student> page = new Page<>(pageNo,pageInfo);
		LambdaQueryWrapper<Student> lqw = new LambdaQueryWrapper<>();
		lqw.like(StringUtils.isNotBlank(name), Student::getName, name);
		lqw.orderByDesc(Student::getId);
		Page<Student> page2 = studentService.page(page, lqw);
		return Result.ok(page2);
	}
	/**
	 * 描述:添加更新
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/addOrUpdateStudent")
	public Result saveOrUpdate (@RequestBody Student student) {
		//判断更新还是添加 没有ID说明是修改
		if (student.getId() != null) {
			
		}else {
			student.setPassword(MD5.encrypt(student.getPassword()));
		}
		studentService.saveOrUpdate(student);
		return Result.ok();
		
	}
	
	@DeleteMapping("/delStudentById")
	public Result delete(@RequestBody List<Integer> id) {
		studentService.removeByIds(id);
		return Result.ok();
		
	}
}
