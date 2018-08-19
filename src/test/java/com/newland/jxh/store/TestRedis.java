package com.newland.jxh.store;

import com.newland.jxh.store.demo.model.Employee;
import com.newland.jxh.store.demo.service.IEmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author qyw
 * @Description TODO
 * @Date Created in 12:59 2018/8/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    @Autowired
    StringRedisTemplate str;

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    RedisTemplate<Object, Employee> employeeRedisTemplate;

    @Test
    public void testString() throws Exception{
        str.opsForValue().append("msg","hello");
        System.out.println(str.opsForValue().get("msg"));
    }
    @Test
    public void testList() throws Exception{
        str.opsForList().leftPush("myList01","2323");
        //System.out.println(str.opsForList().leftPop("myList01"));
    }

    @Test
    public void testSerializer() throws Exception{
        Employee employee = employeeService.selectById(1);
        employeeRedisTemplate.opsForValue().set(employee.getId(),employee);
    }
}
