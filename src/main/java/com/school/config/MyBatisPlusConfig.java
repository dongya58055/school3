package com.school.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * 官方文档说明：于 v3.5.9 起，PaginationInnerInterceptor 已分离出来。如需使用，则需单独引入 mybatis-plus-jsqlparser 依赖
 * @description 
 * @author DY
 * @date 2025-05-30 01:37:48
 *
 */
@Configuration
public class MyBatisPlusConfig {
	@Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }
}
