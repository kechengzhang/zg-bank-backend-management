//package com.vip.file.quartz;
//
//import com.alibaba.fastjson.JSONObject;
//import com.github.benmanes.caffeine.cache.Cache;
//import com.vip.file.model.entity.Files;
//import com.vip.file.model.entity.UploadFileDo;
//import com.vip.file.service.IUploadFileService;
//import com.vip.file.utils.DateUtils;
//import com.vip.file.utils.OkHttpUtil;
//import com.vip.file.utils.VideoInfoUtil;
//import com.vip.file.websocket.constant.ThirdInterfaceConstant;
//import com.vip.file.websocket.service.MessagePushService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author zw
// */
//@Slf4j
//@Component
//public class DasysTaskTimer {
//	@Autowired
//	private OkHttpUtil okHttpUtil;
//	@Value("${file.save-path:/data-center/files/vip-file-manager}")
//	private String savePath;
//	@Value("${file.conf-path:/data-center/files/vip-file-manager/conf}")
//	private String confFilePath;
//
//	@Value("${file.request-path}")
//	private String requestPath;
//
//	@Value("${file.compose-path}")
//	private String composePath;
//
//	@Value("${file.server-path}")
//	private String serverPath;
//	@Autowired
//	private IUploadFileService uploadFileService;
//	@Autowired
//	MessagePushService messagePushService;
//
//	@Value("${compose.compose_time}")
//	private Integer compose_time;
//
//	@Value("${compose.compose_rules}")
//	private String compose_rules;
//
//	@Value("${authentication_api.appKey}")
//	private String appKey;
//
//	@Value("${authentication_api.appSecret}")
//	private String appSecret;
//
//	@Value("${authentication_api.token}")
//	private String tokenUrl;
//
//	@Value("${authentication_api.set-video-file}")
//	private String videoFileUrl;
//	@Autowired
//	private Cache<String, Object> cache;
//
//	public static ConcurrentHashMap<String , Object> uploadFileMap = new ConcurrentHashMap<>();
//
//	public static ConcurrentHashMap<String , Object> composeStatusMap = new ConcurrentHashMap<>();
//
//	public void compose() {
//		VideoInfoUtil.mergeVideo2();
//	}
//	/**
//	 * 定时合成视频 2分钟一次
//	 */
////   	@Scheduled(cron = "0 */2 * * * ?")
//    public void mergeVideoTask() {
//		log.info("——————————————进入定时任务——————————————————");
//		UploadFileDo uploadFile = new UploadFileDo();
//		List<UploadFileDo> uploadFiles = uploadFileService.selectUploadFileList(uploadFile);
//		String tempId = "";
//		String token = null;
//		String callBackUrl = "";
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			Date date = new Date();
//			for (UploadFileDo file : uploadFiles) {
//				String id = String.valueOf(file.getId());
//				String ip = file.getIp();
//				String port = file.getPort();
//				callBackUrl = "http://"+ip+":"+port;
//				tempId = id;
//				//判断当前事件是否有文件正在上传，如果有，跳过本次视频合成
//				if(uploadFileMap.containsKey(file.getId())){
//					log.info("当前事件文件上传状态====="+uploadFileMap.containsKey(file.getId()));
//					continue;
//				}
//				//查看该事件分视频是否已到合成时间
//				boolean flag = false;
//				Map<String,Object> map = uploadFileService.getFileByTargetIdAndTime(id);
//				if(map!=null&&map.size()>0&&map.containsKey("created_time")){
//					//文件合成时间
//					String compareTime = DateUtils.addDateMinut(compose_rules,map.get("created_time").toString(),compose_time);
//					String currentTime = DateUtils.getTime();
//					log.info("当前时间====="+currentTime);
//					//判断是否需要合成，如果当前时间大于合成时间就需要合成，否则不需要合成
//					flag = DateUtils.compareTime(currentTime,compareTime);
//				}
//				if (flag) {
//					if(!StringUtils.isEmpty(ip)) {
//						//获取token
//						token = getToken(callBackUrl);
//						if (!StringUtils.isEmpty(token)) {
//							//开始合成，通知用户
//							setVideoFile(callBackUrl, token, id, "in process", "in process");
//						}
//					}
//					List<Files> fileList = uploadFileService.selectFilesById(id);
//					messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_ONGOING.getName(), id);
//					log.info("开始合成视频，通知页面："+id);
//					//合并好的目录
//					String path = UUID.randomUUID()+".mp4";
//					String savePath = composePath+path;
//					List<String> files = new ArrayList<>();
//					//文件名校验
//					int setFiles = 0;
//					String firstNameSize = fileList.get(0).getFileName().substring(0,fileList.get(0).getFileName().indexOf("."));
//					String fileUrls = "";
//					String tempFileName = "";
//					int height = 0;
//					int width = 0;
//					//单个视频不需要合成
//					if(fileList.size()>1){
//						for (int i = 0; i < fileList.size(); i++) {
//							files.add(fileList.get(i).getFilePath());
//							String url = serverPath+id+"/"+fileList.get(i).getFileName();
//							if(i == fileList.size()-1){
//								fileUrls+=url;
//							}else{
//								tempFileName = url+",";
//								fileUrls+=tempFileName;
//							}
//							if(i!=0){
//								String iNameSize = fileList.get(i).getFileName().substring(0,fileList.get(0).getFileName().indexOf("."));
//								long diff = getDiffTime(firstNameSize,iNameSize);
//								if(firstNameSize.length()!=iNameSize.length()||diff!=1){
//									log.info("文件名称连续性校验失败："+firstNameSize+"-"+iNameSize);
//									setFiles = -1;
//									if(!StringUtils.isEmpty(ip)) {
//										//合成异常，通知用户
//										setVideoFile(callBackUrl, token, tempId, "exception", f.format(new Date()));
//									}
//									break;
//								}
////
//								firstNameSize = iNameSize;
//							}
//							//提取所有视频最大分辨率
////						Encoder encoder = new Encoder();
////						File source = new File(fileList.get(i).getFilePath());
////						MultimediaInfo m = encoder.getInfo(source);
////						int tempHeight = m.getVideo().getSize().getHeight();
////						int tempWidth = m.getVideo().getSize().getWidth();
////						if(tempHeight>height){
////							height = tempHeight;
////						}
////						if(tempWidth>width){
////							width = tempWidth;
////						}
//						}
//						log.info("mp4文件最大分辨率height："+height+",width:"+width);
//						if(setFiles==0){
//							composeStatusMap.put(id,true);
////						String res = VideoInfoUtil.mergeVideo(files,composePath,savePath,height,width);
//							String res = VideoInfoUtil.mergeVideo(files,composePath,savePath);
//							if("1".equals(res)){
//								log.info("合成视频存放地址:{}",savePath);
//								file.setFilePath(requestPath+path);
//								file.setStatus("0");
//								file.setIsCompose("1");
//								uploadFileService.updateUploadFile(file);
//								composeStatusMap.remove(id);
//								messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_SUCCESS.getName(), id);
//								if(!StringUtils.isEmpty(ip)){
//									token = getToken(callBackUrl);
//									if(!StringUtils.isEmpty(token)){
//										//合成成功，通知用户
//										setVideoFile(callBackUrl,token,id,requestPath+path,fileUrls);
//									}
//								}
//								log.info("视频合成完成，通知页面:{}",id);
//							}
//						}
//					}else{
//						String url = serverPath+id+"/"+fileList.get(0).getFileName();
//						file.setFilePath(url);
//						file.setStatus("0");
//						file.setIsCompose("1");
//						uploadFileService.updateUploadFile(file);
//						composeStatusMap.remove(id);
//						messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_SUCCESS.getName(), id);
//						if(!StringUtils.isEmpty(ip)){
//							token = getToken(callBackUrl);
//							if(!StringUtils.isEmpty(token)){
//								//合成成功，通知用户
//								setVideoFile(callBackUrl,token,id,url,url);
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_FAILED.getName(), tempId);
//			composeStatusMap.remove(tempId);
//			e.printStackTrace();
//			e.getMessage();
//            token = getToken(callBackUrl);
//            if(!StringUtils.isEmpty(token)){
//                //合成异常，通知用户
//                setVideoFile(callBackUrl,token,tempId,"exception",f.format(new Date()));
//            }
//			log.info("视频合成异常........");
//		}
//	}
//
//
//	/**
//	 * 计算时间差yyyyMMddHHmm
//	 * @param time1
//	 * @param time2
//	 * @return
//	 */
//	 public Long getDiffTime(String time1,String time2){
//		try {
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
//			Date date1 =  simpleDateFormat.parse(time1);
//			Date date2 =  simpleDateFormat.parse(time2);
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long d1 = df.parse(df.format(date1)).getTime();
//			long d2 = df.parse(df.format(date2)).getTime();
//			long diff=(d2-d1)/1000/60;
//			return diff;
//		} catch (ParseException e) {
//			e.printStackTrace();
//			log.info("文件连续性校验失败："+time1+"-"+time2);
//			return -1L;
//		}
//
//	}
//
//	public String getToken(String callBackUrl){
//        String token = null;
//        String tokenUrl_ = callBackUrl+tokenUrl+"?appKey=%s&appSecret=%s";
//        String queryTokenUrl = String.format(tokenUrl_,appKey,appSecret);
//        log.info("queryTokenUrl：：：：：：："+queryTokenUrl);
//        Map<String, String> header = new HashMap<>();
//        Map<String, Object> queryMap = new HashMap<>();
//        String tokenResult = okHttpUtil.postSend(queryTokenUrl, header, queryMap);
//        log.info("tokenResult：：：：：：："+tokenResult);
//        Map<String,Object> resultMap = JSONObject.parseObject(tokenResult, Map.class);
//        int code = Integer.parseInt(resultMap.get("code").toString());
//        if(code==200) {
//            Map<String, Object> dataMap = new HashMap<>();
//            if (resultMap.get("data") != null) {
//                dataMap = JSONObject.parseObject(resultMap.get("data").toString(), Map.class);
//                token = dataMap.get("token").toString();
//            }
//        }else{
//            log.info("调用getAppToken获取token失败："+code+",msg:"+resultMap.get("msg"));
//            return null;
//        }
//        return token;
//    }
//
//    public void setVideoFile(String callBackUrl,String token,String missionID,String sourceFile,String syntheticFile){
//   		log.info("回调参数：：：missionID:"+missionID+",sourceFile:"+sourceFile+",syntheticFile："+syntheticFile);
//        Map<String, String> header = new HashMap<>();
//        Map<String, Object> queryMap = new HashMap<>();
//        String videoUrl = callBackUrl+videoFileUrl+"?missionID=%s&sourceFile=%s&syntheticFile=%s";
//        String queryVideoFileUrl = String.format(videoUrl,missionID,sourceFile,syntheticFile);
//        log.info("queryVideoFileUrl：：：：：："+queryVideoFileUrl);
//        header.put("token",token);
//        String videoFileResult = okHttpUtil.postSend(queryVideoFileUrl, header, queryMap);
//        log.info("videoFileResult：：：：：：："+videoFileResult);
//        if(!StringUtils.isEmpty(videoFileResult)){
//            Map<String,Object> videoFileResultMap = JSONObject.parseObject(videoFileResult, Map.class);
//            int lastCode = Integer.parseInt(videoFileResultMap.get("code").toString());
//            log.info("事件"+missionID+"合成视频成功，回调结果:"+lastCode+",msg:"+videoFileResultMap.get("msg"));
//        }else{
//            log.info("回调VideoFileUrl失败，videoFileResult："+videoFileResult);
//        }
//    }
//
//	/**
//	 * 获取前一天日期yyyy-MM-dd
//	 * @return
//	 */
//	public String getDate() {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		date = calendar.getTime();
//		String time = df.format(date);
//		return time;
//	}
//
//	/**
//	 * 删除单个文件
//	 *
//	 * @param fileName 要删除的文件的文件名
//	 * @return 单个文件删除成功返回true，否则返回false
//	 */
////	public static boolean deleteFile(String fileName) {
////		try {
////			if (null == fileName) {
////				log.info("删除的单个文件不存在== " + fileName);
////				return false;
////			}
////			File file = new File(fileName);
////			// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
////			if (file.exists() && file.isFile()) {
////				if (file.delete()) {
////					log.info("删除单个文件" + fileName + "成功！");
////					return true;
////				} else {
////					log.info("删除单个文件" + fileName + "失败！");
////					return false;
////				}
////			} else {
////				log.info("删除单个文件失败：" + fileName + "不存在！");
////				return false;
////			}
////		} catch (Exception e) {
////			log.error("删除单个文件失败== " + fileName, e);
////		}
////		return false;
////	}
//
//}
