package com.vip.file.service;

import com.vip.file.model.dto.UserDTO;
import com.vip.file.model.vo.UserLoginVO;

/**
 * @author zkc
 *
 */
public interface UserLoginService {
    /**
     *
     * 获取用户信息
     * @param userName
     * @param password
     * @return
     */
    UserLoginVO getUserInformation(String userName, String password);

    /**
     * 密码修改
     * @param userDTO
     * @return
     */
    int updatePassword(UserDTO userDTO);

    /**
     * 查询原始密码是否正确
     *
     * @param userDTO
     * @return
     */
    int getUserOldPassword(UserDTO userDTO);
}
