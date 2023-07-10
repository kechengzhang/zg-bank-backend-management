package com.vip.file.websocket.heart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.WebSocketServer;

/**
 * @author zw
 */
public class WebSocketHeart {
	
	class HeartThread implements Runnable {
		@Override
		public void run() {
			try {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
				result.put("HEART", "heart");
				
				WebSocketServer.sendInfo(JSON.toJSONString(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void start(){
		try {
			Thread thread = new Thread(new HeartThread()); 
			thread.run();
			thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
