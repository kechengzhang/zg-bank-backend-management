package com.vip.file.websocket.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.mapper.CustomCompositionMapper;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.quartz.CustomCompositionSchedulingTask;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.CommonUtil;
import com.vip.file.utils.DateUtils;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author zw
 */
@ServerEndpoint(value = "/websocket")
@Component
@Slf4j
public class WebSocketServer {
	public static IUploadFileService iUploadFileService;
	public static Integer compose_time;
	public static String compose_rules;
	public static Cache<String, Object> cache;
	public static CustomCompositionMapper customCompositionMapper;
	/**
	 * 静态变量，用来记录当前在线连接数
	 */
	private static int onlineCount = 0;
	/**
	 * concurrent包的线程安全set,用来存放每个客户端对应的MyWebSocket对象
	 */
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

	public static ConcurrentHashMap<String , LinkedBlockingDeque<String>> concurrentHashMap = new ConcurrentHashMap<>();
	/**
	 * 与某个客户端的连接会话，需要通过他来给客户端发送数据
	 */
	private Session session;
	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		//加入set中
		webSocketSet.add(this);
		//在线数+1
		addOnlineCount();
		log.info("有新连接加入！当前在线人数为:{}",getOnlineCount());
		if(getOnlineCount() > 0) {
			if (session.getRequestParameterMap() != null && !session.getRequestParameterMap().isEmpty()) {
				Map<String, List<String>> map = session.getRequestParameterMap();
				String fileId = map.get("fileId").get(0);
				pushVideoComposeStatus(fileId);
			}
		}
	}
	@OnClose
	public void onClose(Session session) {
			Map<String, List<String>> map = session.getRequestParameterMap();
			if(map.get("sessionId") != null){
			String sessionId = map.get("sessionId").get(0);
			concurrentHashMap.remove(sessionId);
				//从set中删除
			webSocketSet.remove(this);
				//在线数-1
			subOnlineCount();
			log.info("有一连接关闭！当前在线人数为:{}", getOnlineCount());
		}else{
			//从set中删除
			webSocketSet.remove(this);
			//在线数-1
			subOnlineCount();
			log.info("有一连接关闭！当前在线人数为:{}",getOnlineCount());
		}
	}
	
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("onError:"+ CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], error.getMessage()));
    }
    
    /**
     * 收到客户端消息后调用的方法
     * @param session
     */
    @OnMessage
    public void onMessage(String string,Session session) {
		Map<String, List<String>> map = session.getRequestParameterMap();
		String sessionId = map.get("sessionId").get(0);
		String fileId = map.get("fileId").get(0);
		if(!concurrentHashMap.containsKey(sessionId)){
			concurrentHashMap.put(sessionId,new LinkedBlockingDeque<>(50));
		}
    	log.info("接受上传文件事件ID:{}",fileId);
		LinkedBlockingDeque<String> queue = concurrentHashMap.get(sessionId);
		log.info("队列长度为:{}" ,queue.size());
		if(queue.isEmpty()){
			queue.offer(fileId);
			//群发消息
			for (WebSocketServer item : webSocketSet) {
				try {
					synchronized (item) {
						Map<String, Object> result = new HashMap<String, Object>();
						result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
						result.put("type", ThirdInterfaceConstant.WebSocketMsg.EVENT_SUCCESS.getName());
						result.put("info", fileId);
						String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
						log.info("发送消息:{}",str);
						item.sendMessage(str);
					}
				} catch (Exception e) {
					log.error(CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], e));
				}
			}
		}else{
			queue.offer(fileId);
		}
    }
	
	/**
	 * 实现服务器主动推送
	 * @param message
	 */
	
	public void sendMessage(String message) {
		try {
			this.session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			log.error(CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], e));
		}
	}
	
    /**
     * 群发自定义消息
     * @param message
     * @throws IOException
     */
    public static void sendInfo(String message) throws IOException {
    	JSONObject jsonObject = JSONObject.parseObject(message);
    	log.info("推送数据:{}",jsonObject);
    	for (WebSocketServer item : webSocketSet) {
            try {
            	synchronized (item) {
            		 item.sendMessage(message);
            	}
            } catch (Exception e) {
                continue;
            }
        }
    }


	/**
	 * 群发自定义消息
	 * @param message
	 * @throws IOException
	 */
	public static void sendInfo1(String message) {
		JSONObject jsonObject1 = JSONObject.parseObject(message);
		log.info("推送数据:{}",jsonObject1);
		for (WebSocketServer item : webSocketSet) {
			try {
				synchronized (item) {
					String fileId ="";
					if("videoCompose".equals(jsonObject1.getString("type")) && "3".equals(jsonObject1.getString("isCompose"))) {
						Map<String, List<String>> map = item.session.getRequestParameterMap();
						fileId = map.get("fileId").get(0);
					}
					if(!"".equals(fileId)) {
						Map<String, Object> result = new HashMap<>();
						//查询该任务是否有合成视频
						Map<String, Object> fileMap = iUploadFileService.getFileByTargetIdAndTime(fileId);
						if (fileMap != null ) {
							int isCompose= Integer.parseInt(String.valueOf(fileMap.get("isCompose")));
							if(isCompose == 0) {
								//isCompose ==0 未合成，未合成视频前端根据 isCompose和begintime endTime来判断
								//isComposeBoolean ture说明视频正在合成中
								boolean isComposeBoolean = CustomCompositionSchedulingTask.composeStatusMap.containsKey(fileId);
								if(isComposeBoolean){
									String composeStr =videoCompose(fileId,isCompose);
									item.sendMessage(composeStr);
								}else{
									String str = videoNoCompose(fileId);
//									JSONObject jsonObject = new JSONObject();
//									jsonObject.put("fileId", fileId);
//									jsonObject.put("currentTime", DateUtils.getTime());
//									String endTime =getComposeTime();
//									String beginTime = DateUtils.reduceDateMinute(endTime,compose_time);
//									jsonObject.put("beginTime",DateUtils.getDate()+" "+beginTime);
//									jsonObject.put("endTime",DateUtils.getDate()+" "+endTime);
//									result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
//									result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
//									result.put("info", jsonObject);
//									result.put("isCompose", 2);
//									String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
									item.sendMessage(str);
								}
							}else{
								String str = videoSynthesized(isCompose);
//								result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
//								result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
//								result.put("isCompose", isCompose);
//								String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
								item.sendMessage(str);
							}
						}
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
	public static String getComposeTime(){
    	String endTime ="";
		if(cache.getIfPresent("composeTime") != null) {
			endTime = cache.getIfPresent("composeTime").toString();
		}else {
			CustomCompositionVO customCompositionVO = customCompositionMapper.queryCustomComposition();
			endTime =customCompositionVO.getSynthesisTime();
			cache.put("composeTime",endTime);
		}
		return endTime;
	}
	/**
	 * 推送视频状态给前端
	 *
	 */
	public static void pushVideoComposeStatus(String fileId)throws IOException {
		Map<String, Object> result = new HashMap<>();
		//查询该任务是否有合成视频
		Map<String, Object> fileMap = iUploadFileService.getFileByTargetIdAndTime(fileId);
		if (fileMap != null ) {
			int isCompose= Integer.parseInt(String.valueOf(fileMap.get("isCompose")));
			if(isCompose == 0) {
				//isCompose ==0 未合成，未合成视频前端根据 isCompose和begintime endTime来判断
				//isComposeBoolean ture说明视频正在合成中
				boolean isComposeBoolean = CustomCompositionSchedulingTask.composeStatusMap.containsKey(fileId);
				if(isComposeBoolean){
					String composeStr = videoCompose(fileId,isCompose);
					sendInfo(composeStr);
				}else{
					String str = videoNoCompose(fileId);
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("fileId", fileId);
//					jsonObject.put("currentTime", DateUtils.getTime());
//					String endTime =getComposeTime();
//					String beginTime = DateUtils.reduceDateMinute(endTime,compose_time);
//					jsonObject.put("beginTime",DateUtils.getDate()+" "+beginTime);
//					jsonObject.put("endTime",DateUtils.getDate()+" "+endTime);
//					result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
////							result.put("type", ThirdInterfaceConstant.WebSocketMsg.CURRENT_TIME.getName());
//					result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
//					result.put("info", jsonObject);
//					result.put("isCompose", 2);
//					String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
					sendInfo(str);
				}
			}else{
				String str = videoSynthesized(isCompose);
//				result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
//				result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
//				result.put("isCompose", isCompose);
//				String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
				sendInfo(str);
			}
		}
	}
	public static String videoCompose(String fileId,int isCompose){
		Map<String, Object> composeMap = new HashMap<>(3);
		composeMap.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
//							composeMap.put("type", ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_ONGOING.getName());
		composeMap.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
		composeMap.put("info", fileId);
		composeMap.put("isCompose", isCompose);
		composeMap.put("fileId", fileId);
		String composeStr = JSON.toJSONString(composeMap, SerializerFeature.DisableCircularReferenceDetect);
		return composeStr;
	}

	public static String videoNoCompose(String fileId){
		Map<String, Object> result = new HashMap<>(3);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fileId", fileId);
		jsonObject.put("currentTime", DateUtils.getTime());
		String endTime =getComposeTime();
		String beginTime = DateUtils.reduceDateMinute(endTime,compose_time);
		jsonObject.put("beginTime",DateUtils.getDate()+" "+beginTime);
		jsonObject.put("endTime",DateUtils.getDate()+" "+endTime);
		result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
		result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
		result.put("info", jsonObject);
		result.put("isCompose", 2);
		String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
		return str;
	}

	public static String videoSynthesized(int isCompose){
		Map<String, Object> result = new HashMap<>(3);
		result.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
		result.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
		result.put("isCompose", isCompose);
		String str = JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
		return str;
	}

}
