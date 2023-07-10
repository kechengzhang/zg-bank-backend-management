package com.vip.file.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.dto.UserDTO;
import com.vip.file.model.vo.UserLoginVO;
import com.vip.file.service.IUploadFileService;
import com.vip.file.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zkc
 * @description
 * @Date 2023/5/25 15:34 星期四
 * @Version 1.0
 */
@RestController
@Api(tags = "用户登录")
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private Cache<String, Object> cache;

    @LogTrack(value = "后台管理系统,登录,用户登录,登录")
    @ApiOperation(value = "用户登录")
    @GetMapping("userLogin")
    public Result<UserLoginVO> userLogin(@RequestParam String userName, @RequestParam String password) {
        if (userName == null || "".equals(userName)) {
            return Result.failure(ResultCodeEnum.USERNAME_NULL_ERROR);
        }
        if (password == null || "".equals(password)) {
            return Result.failure(ResultCodeEnum.PASSWORD_NULL_ERROR);
        }
        //查询用户名是否正确
        UserLoginVO userLoginVO = userLoginService.getUserInformation(userName, null);
        if (userLoginVO == null) {
            return Result.failure(ResultCodeEnum.USERNAME_ERROR);
        }
        //查询密码是否正确
        userLoginVO = userLoginService.getUserInformation(userName, password);
        if (userLoginVO == null) {
            return Result.failure(ResultCodeEnum.PASSWORD_ERROR);
        }
        //将该用户userId存到缓存中
        cache.put(userLoginVO.getToken(), userLoginVO.getUserId());
        return Result.success(ResultCodeEnum.USER_LOGIN_SUCCESS, userLoginVO);
    }

    @LogTrack(value = "后台管理系统,用户中心,用户登出,登出")
    @ApiOperation(value = "用户退出登录")
    @GetMapping("userLoginOut")
    public Result userLoginOut(HttpServletRequest httpServletRequest) {
        cache.asMap().remove(httpServletRequest.getHeader("Accesstoken"));
        return Result.success(ResultCodeEnum.USER_LOGIN_OUT_SUCCESS);
    }
    @LogTrack(value = "后台管理系统,用户中心,修改密码,修改")
    @ApiOperation(value = "修改密码")
    @PutMapping("updatePassword")
    public Result updatePassword(@RequestBody UserDTO userDTO) {
        //查询该用户旧密码是否正确
        int result = userLoginService.getUserOldPassword(userDTO);
        if (result < 1) {
            return Result.success(ResultCodeEnum.OLD_PASSWORD_ERROR);
        }
        userLoginService.updatePassword(userDTO);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }
}
