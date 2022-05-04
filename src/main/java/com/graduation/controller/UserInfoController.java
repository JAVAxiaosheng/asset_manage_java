package com.graduation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduation.common.R;
import com.graduation.entity.UserInfo;
import com.graduation.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user_info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户登录
     *
     * @param request
     * @param userInfo
     * @return
     */
    @PostMapping("/login")
    public R<UserInfo> login(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        log.info(userInfo.getUserName());
        log.info(userInfo.getPassword());
        // 密码加密
        String password = userInfo.getPassword();
        DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        // 根据用户名查数据库
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userInfo.getUserName());
        UserInfo uInfo = userInfoService.getOne(queryWrapper);
        log.info(userInfo.getUserName());

        // 判断是否查到
        if (uInfo == null) {
            return R.error("没有找到用户名，登录失败");
        }
        if (!uInfo.getPassword().equals(password)) {
            return R.error("密码错误，登录失败");
        }
        return R.success(uInfo, 1L);
    }

    /**
     * 用户信息分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    @GetMapping("/query_user")
    public R<List> queryUserInfo(int pageNum, int pageSize, String username, int role, int id) {
        Page pageInfo = new Page(pageNum, pageSize);
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(StringUtils.isNotEmpty(username), UserInfo::getUserName, username).
                eq(role >= 0, UserInfo::getRole, role).
                eq(id >= 0, UserInfo::getId, id);
        userInfoService.page(pageInfo, queryWrapper);
        List<UserInfo> userInfoList = pageInfo.getRecords();
        return R.success(userInfoList, pageInfo.getTotal());
    }

    /**
     * 添加用户
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/save_user")
    public R<Boolean> saveUserInfo(@RequestBody UserInfo userInfo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userInfo.getUserName());
        UserInfo uInfo = userInfoService.getOne(queryWrapper);
        if (uInfo != null) {
            return R.error("该用户已存在，请勿重复注册");
        }

        boolean isSuccess = userInfoService.save(userInfo);
        if (isSuccess) {
            return R.success(true, 0);
        }
        return R.error("添加失败");
    }

    /**
     * 修改用户信息
     *
     * @param userInfo
     * @return
     */
    @PutMapping("update_user")
    public R<Boolean> updateUserInfo(@RequestBody UserInfo userInfo) {
        boolean isSuccess = userInfoService.updateById(userInfo);
        if (isSuccess) {
            return R.success(true, 0);
        }
        return R.error("修改失败");
    }

    /**
     * 删除用户
     *
     * @param userInfo
     * @return
     */
    @DeleteMapping("delete_user")
    public R<Boolean> deleteUserInfo(@RequestBody UserInfo userInfo) {
        boolean isSuccess = userInfoService.removeById(userInfo);
        if (isSuccess) {
            return R.success(true, 0);
        }
        return R.error("删除失败");
    }
}
