package com.school.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dto.LoginForm;
import com.school.eneity.Admin;
import com.school.eneity.Student;
import com.school.eneity.Teacher;
import com.school.service.AdminService;
import com.school.service.StudentService;
import com.school.service.TeacherService;
import com.util.CreateVerifiCodeImage;
import com.util.JwtHelper;
import com.util.MD5;
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
		/**
		 * code 是一个对象（数组是对象）。 toString() 方法调用的是 Object.toString()，默认返回的是“类名@哈希码”的形式，比如
		 * [C@4eec7777： [C 代表它是 char[] 类型。 @4eec7777 是内存地址的哈希值。
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

	/**
	 * 描述:图片上传
	 * 
	 * @param 参数说明:MultipartFile 是 Java 中用于处理文件上传的一种接口，常用于 Spring 框架中，特别是在使用 Spring
	 *                           MVC 时。 当我们通过表单或接口上传文件到服务器时，Spring 会自动将上传的文件封装为一个
	 *                           MultipartFile 对象，供我们在控制器中处理。
	 * @RequestPart 是 Spring 中用于处理 multipart/form-data 请求中 非表单字段（如文件）或嵌套 JSON 数据 的注解
	 * @return 返回值:
	 * @throws IOException
	 * @throws IllegalStateException
	 * @exception 异常:
	 */

	@Value("${school.basepath}")
	private String path;

	// 上传图片
	@PostMapping("/headerImgUpload")
	public Result upload(@RequestPart("multipartFile") MultipartFile multipartFile)
			throws IllegalStateException, IOException {
		// 保存文件
		// 使用uuid重新生成文件名
		String name = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		String originalFilename = multipartFile.getOriginalFilename();
		
		// 获取后缀名
		String lastname = originalFilename.substring(originalFilename.lastIndexOf("."));
		// 文件新名称
		String fileName = name + lastname;
		System.out.println(fileName);
		// 响应图片路径
		String filePath = path.concat(fileName);
		System.out.println(filePath);
		// 保存图片到本地
		multipartFile.transferTo(new File(filePath));
		// 返回图片路径
		return Result.ok("upload/".concat(fileName));

	}

	/**
	 * 描述:更改密码
	 * 
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/updatePwd/{oldPwd}/{newPwd}")
	public Result password(@RequestHeader("token") String token,@PathVariable String oldPwd, 
		@PathVariable String newPwd) {
		boolean expiration = JwtHelper.isExpiration(token);
		if (expiration) {
			// 已经过期
			return Result.fail().message("token失效，请重新登录");
		}
		// 根据token获取用户类型
		Integer userId = JwtHelper.getUserId(token).intValue();
		Integer userType = JwtHelper.getUserType(token);
		switch (userType) {
		case 1: {
			LambdaQueryWrapper<Admin> lqw = new LambdaQueryWrapper<>();
			lqw.eq(Admin::getId, userId);
			lqw.eq(Admin::getPassword, MD5.encrypt(oldPwd));
			Admin admin = adminService.getOne(lqw);
			if (admin != null) {
				admin.setPassword(MD5.encrypt(newPwd));
				adminService.saveOrUpdate(admin);
			} else {
				return Result.fail().message("原密码有误");
			}
			break;

		}

		case 2: {
			LambdaQueryWrapper<Student> lqw = new LambdaQueryWrapper<>();
			lqw.eq(Student::getId, userId);
			lqw.eq(Student::getPassword, MD5.encrypt(oldPwd));
			Student student = studentService.getOne(lqw);
			if (student != null) {
				student.setPassword(MD5.encrypt(newPwd));
				studentService.saveOrUpdate(student);
			} else {
				return Result.fail().message("原密码有误");
			}
			break;
		}

		case 3: {
			LambdaQueryWrapper<Teacher> lqw = new LambdaQueryWrapper<>();
			lqw.eq(Teacher::getId, userId);
			lqw.eq(Teacher::getPassword, MD5.encrypt(oldPwd));
			Teacher teacher = teacherService.getOne(lqw);
			if (teacher != null) {
				teacher.setPassword(MD5.encrypt(newPwd));
				teacherService.saveOrUpdate(teacher);
			} else {
				return Result.fail().message("原密码有误");
			}
			break;
		}

		}
		return Result.ok();

	}
}
