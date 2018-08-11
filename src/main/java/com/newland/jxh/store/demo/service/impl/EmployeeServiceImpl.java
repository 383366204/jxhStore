package com.newland.jxh.store.demo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Preconditions;
import com.newland.jxh.store.common.exception.UserNameExistException;
import com.newland.jxh.store.common.jsonResult.ServerResponse;
import com.newland.jxh.store.demo.model.Employee;
import com.newland.jxh.store.demo.dao.EmployeeMapper;
import com.newland.jxh.store.demo.service.IEmployeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qyw
 * @since 2018-08-11
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public ServerResponse<Employee> save(Employee employee) {
        Preconditions.checkNotNull(employee,"employee实体为null");
        Integer count = baseMapper.selectCount(new EntityWrapper<Employee>().eq("last_name", employee.getLastName()));
        if(count > 0){
            throw new UserNameExistException();
        }
        Integer insert = baseMapper.insert(employee);
        //int  i=1/0; 测试事物是否生效
        if(insert > 0){
            return ServerResponse.createBySuccess("插入成功",employee);
        }
        return ServerResponse.createByError("插入失败",employee);
    }
}
