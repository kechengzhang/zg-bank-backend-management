package com.vip.file.service.impl;

import com.vip.file.exception.JwtDecodeException;
import com.vip.file.mapper.UserLoginMapper;
import com.vip.file.model.dto.UserDTO;
import com.vip.file.model.vo.UserLoginVO;
import com.vip.file.service.UserLoginService;
import com.vip.file.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zkc
 * @description
 * @Date 2023/5/25 15:39 星期四
 * @Version 1.0
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Override
    public UserLoginVO getUserInformation(String userName, String password) {
        UserLoginVO userLoginVO =  userLoginMapper.getUserInformation(userName,password);
        if(userLoginVO != null) {
            try {
                //获取token
                String token = TokenUtils.getToken(userName, password);
                userLoginVO.setToken(token);
            } catch (Exception e) {
            throw new JwtDecodeException("获取token失败");
            }
        }
        return userLoginVO;
    }

    @Override
    public int updatePassword(UserDTO userDTO) {
        return userLoginMapper.updatePassword(userDTO) ;
    }

    @Override
    public int getUserOldPassword(UserDTO userDTO) {
        return userLoginMapper.getUserOldPassword(userDTO);
    }
}
