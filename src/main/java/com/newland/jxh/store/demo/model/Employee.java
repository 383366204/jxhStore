package com.newland.jxh.store.demo.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author qyw
 * @since 2018-08-11
 */
@TableName("tbl_employee")
@Data
public class Employee extends Model<Employee> {

    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.AUTO)
    private Long id;
    @TableField(value = "last_name",exist = true)
    @NotBlank(message = "用户名不能为空!")
    private String lastName;
    @NotBlank(message = "邮箱不能为空!")
    @Email(message = "邮箱不正确!")
    private String email;
    @Range(min = -1,max = 1,message = "性别代号不正确")
    private String gender;
    @Range(min = 0,max = 300,message = "年龄不正确")
    private Integer age;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
