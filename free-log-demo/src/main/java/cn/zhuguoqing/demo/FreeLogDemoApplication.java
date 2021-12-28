package cn.zhuguoqing.demo;

import cn.zhuguoqing.operationLog.bean.annotation.EnableOperationLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/28 14:31
 * @Desription: TODO
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan("cn.zhuguoqing.demo.mapper")
@EnableOperationLog
public class FreeLogDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(FreeLogDemoApplication.class);
    }


}
