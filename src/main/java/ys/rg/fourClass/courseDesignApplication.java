package ys.rg.fourClass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("ys.rg.fourClass.mapper")
public class courseDesignApplication {
    public static void main(String[] args) {
        SpringApplication.run(courseDesignApplication.class, args);
        System.out.println("===========================================");
        System.out.println("页面置换算法实验平台启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("===========================================");
    }
}
