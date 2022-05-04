package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 卢展
 * @version 1.0
 * 用户实体类
 */
@Data
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userName;
    private String password;
    private int role;
}
