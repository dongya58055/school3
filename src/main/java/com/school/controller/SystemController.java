package com.school.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.LoginForm;
import com.school.eneity.Admin;
import com.school.eneity.Student;
import com.school.eneity.Teacher;
import com.school.service.AdminService;
import com.school.service.StudentService;
import com.school.service.TeacherService;
import com.util.CreateVerifiCodeImage;
import com.util.JwtHelper;
import com.util.Result;
import com.util.ResultCodeEnum;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sms/system")
public class SystemController {

	@Autowired
	AdminService adminService;
	@Autowired
	StudentService studentService;
	@Autowired
	TeacherService teacherService;

	/**
	 * 描述:test
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/hello")
	public String hello() {
		return "hello world";
	}

	/**
	 * 描述:获取验证码图片
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getVerifiCodeImage")
	public void getVerifiCodeImage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		// 获取图片
		BufferedImage image = CreateVerifiCodeImage.getVerifiCodeImage();
		// 获取图片的验证码
//		char[] code = CreateVerifiCodeImage.getVerifiCode();
//		String string = code.toString();     
		/**code 是一个对象（数组是对象）。
		 * toString() 方法调用的是 Object.toString()，默认返回的是“类名@哈希码”的形式，比如 [C@4eec7777：
			[C 代表它是 char[] 类型。
			@4eec7777 是内存地址的哈希值。
		 */
		String string = new String(CreateVerifiCodeImage.getVerifiCode());
		// 验证码文本放入session，给验证做准备
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute("code", string);
		// 将图片发送
		ImageIO.write(image, "JPEG", httpServletResponse.getOutputStream());
		System.out.println("验证码" + string);
	}

	/**
	 * 
	 * 描述:登录
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/login")
	// 这个 JSON 数据无法自动对应到 Java 的参数上，除非你用 @RequestBody 告诉 Spring：“请把请求体里的 JSON
	// 数据，反序列化成一个 LoginForm 对象。”
	public Result login(@RequestBody LoginForm loginForm, HttpSession session) {

		// 验证码校验
		String sessionCode = session.getAttribute("code").toString();
		System.out.println("验证码" + sessionCode);
		// 对session验证码校验
		if (session.equals("") || session == null) {
			return Result.fail().message("页面失效，请重新登录");
		}
		// 取消验证码大小写
		if (!loginForm.getVerifiCode().equalsIgnoreCase(sessionCode)) {
			return Result.fail().message("验证码有误，请重试");
		} // 从session中移除验证码
		session.removeAttribute("code");

		// 用户类型校验
		Integer userType = loginForm.getUserType();
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		switch (userType) {
		// 管理
		case 1: {
			// 管理登录方法
			Admin admin = adminService.login(loginForm);
			try {
				if (admin != null) {
					// 用户类型和id转换成密文，以token传给客户端
					map.put("token", JwtHelper.createToken(admin.getId().longValue(), userType));

				} else {
					throw new RuntimeException("用户名或密码有误");
				}
				return Result.ok(map);
			} catch (RuntimeException e) {
				// TODO: handle exception
				log.error("错误信息", e.getMessage());
				e.printStackTrace();
				return Result.fail().message(e.getMessage());
			}
		}
		// student
		case 2: {
			// 管理登录方法
			Student student = studentService.login(loginForm);
			try {
				if (student != null) {
					// 用户类型和id转换成密文，以token传给客户端
					map.put("token", JwtHelper.createToken(student.getId().longValue(), userType));

				} else {
					throw new RuntimeException("用户名或密码有误");
				}
				return Result.ok(map);
			} catch (RuntimeException e) {
				// TODO: handle exception
				log.error("错误信息", e.getMessage());
				e.printStackTrace();
				return Result.fail().message(e.getMessage());
			}

		}
		// teacher
		case 3: {
			// 管理登录方法
			Teacher teacher = teacherService.login(loginForm);
			try {
				if (teacher != null) {
					// 用户类型和id转换成密文，以token传给客户端
					map.put("token", JwtHelper.createToken(teacher.getId().longValue(), userType));
					// return Result.ok(token);
				} else {
					throw new RuntimeException("用户名或密码有误");
				}
				return Result.ok(map);
			} catch (RuntimeException e) {
				// TODO: handle exception
				log.error("错误信息", e.getMessage());
				e.printStackTrace();
				return Result.fail().message(e.getMessage());
			}

		}
		}
		return Result.fail().message("没有此用户");
	}

	/**
	 * 
	 * 描述:从请求头的token中解析数据并根据类型跳转首页
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/getInfo")
	public Result getInfo(@RequestHeader("token") String token) {
		// 校验token是否过期
		if (JwtHelper.isExpiration(token)) {
			return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
		}
		// 从token中解析出用户id和类型
		Long userId = JwtHelper.getUserId(token);
		Integer userType = JwtHelper.getUserType(token);
		Map<String, Object> map = new LinkedHashMap<>();
		switch (userType) {
		case 1: {
			Admin admin = adminService.getById(userId);
			map.put("userType", userType);
			map.put("user", admin);
			break;
		}
		case 2: {
			Student student = studentService.getById(userId);
			map.put("userType", userType);
			map.put("user", student);
			break;
		}
		case 3: {
			Teacher teacher = teacherService.getById(userId);
			map.put("userType", userType);
			map.put("user", teacher);
			break;
		}
		}
		
		return Result.ok(map);

	}

}
