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
import com.school.eneity.Admin;
import com.school.service.AdminService;
import com.util.MD5;
import com.util.Result;

@RequestMapping("/sms/adminController")
@RestController
public class AdminController {
	@Autowired
	private AdminService adminService;

	/**
	 * 
	 * 描述:分页带条件查询
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getAllAdmin/{pageNo}/{pageInfo}")
	public Result getAllAdmin(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageInfo") Integer pageInfo,
			String gradeName, String name) {
		Page<Admin> page = new Page<>(pageNo, pageInfo);
		LambdaQueryWrapper<Admin> lqw = new LambdaQueryWrapper<>();
		if (!StringUtils.isBlank(name)) {
			lqw.like(Admin::getName, name);
		}
		Page<Admin> page2 = adminService.page(page, lqw);
		return Result.ok(page2);

	}

	@PostMapping("/saveOrUpdateAdmin")
	public Result saveOrUpdate(@RequestBody Admin admin) {
		// 判断更新还是添加 没有ID说明是修改
		if (admin.getId() != null) {

		} else {
			admin.setPassword(MD5.encrypt(admin.getPassword()));
		}
		adminService.saveOrUpdate(admin);
		return Result.ok();

	}

	@DeleteMapping("/deleteAdmin")
	public Result delete(@RequestBody List<Integer> id) {
		adminService.removeByIds(id);
		return Result.ok();

	}

	@GetMapping("/getClazzs")
	public Result getGrades() {
		List<Admin> list = adminService.list();
		return Result.ok(list);

	}
}
