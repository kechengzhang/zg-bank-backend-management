package com.vip.file.mapper;

import com.vip.file.model.dto.UserDTO;
import com.vip.file.model.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zkc
 *
 */
@Mapper
public interface UserLoginMapper {
    /**
     * 获取用户信息
     *
     * @param userName
     * @param password
     * @return
     */
    UserLoginVO getUserInformation(@Param("userName") String userName, @Param("password") String password);

    /**
     * 密码修改
     *
     * @param userDTO
     * @return
     */
    int updatePassword(@Param("userDTO") UserDTO userDTO);
    /**
     *查询用户原始密码是否正确
     * @param userDTO
     * @return
     */
    int getUserOldPassword(UserDTO userDTO);
}
