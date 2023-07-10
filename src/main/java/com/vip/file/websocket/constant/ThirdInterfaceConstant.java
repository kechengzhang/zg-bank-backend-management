package com.vip.file.websocket.constant;

public interface ThirdInterfaceConstant {

	/**
	 * 推送消息KEY
	 */
	public static interface WebSocketKey {
		public static final String STATE = "state";
		public static final int CODE_OK = 1;
		public static final int CODE_ERROR = -1;
		public static final int CODE_EXCEP = -2;
	}
	
	/**
	 * 推送消息KEY及CODE
	 */
	public static enum WebSocketMsg {
		//系统时间
		CURRENT_TIME("CURRENT_TIME", 0),
		//系统时间
		COMPOSE_TIME("COMPOSE_TIME", 0),
		//文件合成中
		MERGE_VIDEO_ONGOING("MERGE_VIDEO_ONGOING", 0),
		//文件合成完成
		MERGE_VIDEO_SUCCESS("MERGE_VIDEO_SUCCESS", 0),
		//文件合成失败
		MERGE_VIDEO_FAILED("MERGE_VIDEO_FAILED", 0),
		//文件上传中
		EVENT_UPLOADING("EVENT_UPLOADING", 1),
		//文件上传成功
		EVENT_SUCCESS("EVENT_SUCCESS", 2),
		//文件上传失败
		EVENT_FAILED("EVENT_FAILED", -1),
		MESSAGE_NUMBER("messageNumber", 0),
		VIDEO_COMPOSE("videoCompose", 0);
	    private String name;
	    private int index;

	   
	    private WebSocketMsg(String name, int index) {
	        this.name = name;
	        this.index = index;
	    }

	    public static String getName(int index) {
	        for (WebSocketMsg c : WebSocketMsg.values()) {
	        if (c.getIndex() == index) {
	            return c.name;
	        }
	        }
	        return null;
	    }

	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    public int getIndex() {
	        return index;
	    }
	    public void setIndex(int index) {
	        this.index = index;
	    }
	}
	
}
