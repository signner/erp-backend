package cn.shiying.users_department;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"cn.shiying.common.mapper","cn.shiying.users_department.mapper"})
@ComponentScan(basePackages = {"cn.shiying.config","cn.shiying.users_department"})
@SpringBootApplication
public class Users_departmentApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Users_departmentApplication.class, args);
    }

}