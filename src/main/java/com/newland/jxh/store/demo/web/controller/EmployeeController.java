package com.newland.jxh.store.demo.web.controller;


import com.newland.jxh.store.common.exception.ApiException;
import com.newland.jxh.store.common.exception.StoreException;
import com.newland.jxh.store.common.exception.UserNameExistException;
import com.newland.jxh.store.common.jsonResult.ServerCode;
import com.newland.jxh.store.common.jsonResult.ServerResponse;
import com.newland.jxh.store.demo.model.Employee;
import com.newland.jxh.store.demo.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qyw
 * @since 2018-08-11
 */
@Api(description = "演示使用的employeeController")
@Controller
@RequestMapping("/demo/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @GetMapping
    public String index(){
        return "index";
    }

    @ApiOperation(value="创建employee", notes="根据employee对象创建用户")
    @ApiImplicitParam(name = "employee", value = "员工详细实体employee", required = true, dataType = "Employee")
    @PostMapping("/save")
    @ResponseBody
    public ServerResponse<Employee> save(@Valid Employee employee){
        ServerResponse<Employee> serverResponse=null;
        try {
            serverResponse= employeeService.save(employee);
        }catch (UserNameExistException e){
            throw new ApiException(0,e.getMessage());
        } catch (StoreException e){
            throw new ApiException(ServerCode.UNKNOW_EXCEPTION.getCode(),"插入失败");
        }
        return serverResponse;
    }


}

