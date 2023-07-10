package com.vip.file.websocket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vip.file.utils.CommonUtil;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessagePushService {
	
	/**
	 * 推送消息
	 * @param
	 */
	public void sendStatus(String state,String event) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
			result.put("type", state);
			if(event==null){
				event = "";
			}
			result.put("info", event);
			String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
			WebSocketServer.sendInfo(str);
		} catch (IOException e) {
			log.error(CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], e));
		}
	}

	/**
	 * 推送合成时间
	 * @param
	 */
	public void sendComposeTime(String state,JSONObject jsonObject) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
			result.put("type", state);
			result.put("info", jsonObject);
			String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
			WebSocketServer.sendInfo(str);
		} catch (IOException e) {
			log.error(CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], e));
		}
	}
	
	
	
}
