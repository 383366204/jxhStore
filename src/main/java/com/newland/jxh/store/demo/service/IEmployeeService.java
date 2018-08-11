package com.newland.jxh.store.demo.service;

import com.newland.jxh.store.common.jsonResult.ServerResponse;
import com.newland.jxh.store.demo.model.Employee;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qyw
 * @since 2018-08-11
 */
public interface IEmployeeService extends IService<Employee> {

    ServerResponse<Employee> save(Employee employee);
}
