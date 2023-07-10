package com.vip.file.utils;

import com.vip.file.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

/**
 * UUID工具
 *
 * @author vip
 * @date 2019/12/03 16:12
 */
public class UuidUtils {


    /**
     * 得到32位的uuid
     *
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
