package com.vip.file.websocket.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.mapper.CustomCompositionMapper;
import com.vip.file.service.IUploadFileService;
import com.vip.file.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zw
 */
@Configuration
public class WebSocketConfig {

//	@Value("${compose.compose_time}")
//	public static Integer compose_time;
//
//	@Value("${compose.compose_rules}")
//	public static String compose_rules;
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {

		return new ServerEndpointExporter();
	}

	@Autowired
	public void setMessageService(IUploadFileService iUploadFileService, Cache<String, Object> cache,
			CustomCompositionMapper customCompositionMapper) {
		WebSocketServer.iUploadFileService = iUploadFileService;
		WebSocketServer.cache = cache;
		WebSocketServer.customCompositionMapper = customCompositionMapper;
	}

	@Value("${compose.compose_time}")
	public void setCompose_time(Integer compose_time) {
		WebSocketServer.compose_time = compose_time;
	}
	@Value("${compose.compose_rules}")
	public void setCompose_rules(String compose_rules) {
		WebSocketServer.compose_rules = compose_rules;
	}

}
