package com.vip.file.aspect;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.entity.FileLogDO;
import com.vip.file.model.entity.FileUploadFailedDO;
import com.vip.file.model.vo.UserLoginVO;
import com.vip.file.service.FileVideoLogService;
import com.vip.file.service.IFileService;
import com.vip.file.service.UserLoginService;
import com.vip.file.utils.CommonUtil;
import com.vip.file.utils.ConstantEnum;
import com.vip.file.utils.DateUtils;
import com.vip.file.utils.IpLongUtils;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import com.vip.file.websocket.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zkc
 * @date 2021/06/16
 * <p>
 * 日志
 */
@Slf4j
@Component
@Aspect
public class SysLogAspect {
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private FileVideoLogService fileVideoLogService;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private IFileService fileService;
    @Autowired
    MessagePushService messagePushService;


    @Pointcut(" @annotation(com.vip.file.aspect.LogTrack)")
    public void logPointCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.vip.file.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @param logTrack
     * @return
     * @throws Throwable
     */
    @Around("logPointCut() && @annotation(logTrack)")
    public Result around(ProceedingJoinPoint joinPoint, LogTrack logTrack) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        //获取响应数据
        Object object = joinPoint.proceed();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(object);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Object[] objects = joinPoint.getArgs();
        //获取请求参数
        JsonObject parameter = getParameter(joinPoint, objects, signature);
        String[] array = logTrack.value().split(",");
        if ("添加分视频文件".equals(array[2])) {
            String fileVerificationRulesDeleteDTOString = parameter.get("param").getAsString();
            JsonObject fileVerificationRulesDeleteDTO = JsonParser.parseString(fileVerificationRulesDeleteDTOString).getAsJsonObject();
            if (fileVerificationRulesDeleteDTO.get("uniqueIdentification") == null) {
                return (Result) object;
            }
        }
        FileLogDO logDO = new FileLogDO();
        logDO.setSystemName(array[0]);
        //操作模块
        logDO.setBusinessModule(array[1]);
        //操作对象
        logDO.setOperationObjects(array[2]);
        if ("删除".equals(array[3]) && "合成视频管理".equals(array[1])) {
            String fileVerificationRulesDeleteDTOString = parameter.get("syntheticVideoDTO").getAsString();
            JsonObject fileVerificationRulesDeleteDTO = JsonParser.parseString(fileVerificationRulesDeleteDTOString).getAsJsonObject();
            int deleteType = fileVerificationRulesDeleteDTO.get("deleteType").getAsInt();
            if (2 == deleteType) {
                logDO.setOperationType("批量删除");
            } else {
                logDO.setOperationType(array[3]);
            }
        } else if ("删除".equals(array[3]) && "合成自定义".equals(array[1])) {
            String fileVerificationRulesDeleteDTOString = parameter.get("fileVerificationRulesDeleteDTO").getAsString();
            JsonObject fileVerificationRulesDeleteDTO = JsonParser.parseString(fileVerificationRulesDeleteDTOString).getAsJsonObject();
            int deleteType = fileVerificationRulesDeleteDTO.get("deleteType").getAsInt();
            if (2 == deleteType) {
                logDO.setOperationType("批量删除");
            } else {
                logDO.setOperationType(array[3]);
            }
        } else {
            logDO.setOperationType(array[3]);
        }
        //获取请求ip
        logDO.setIp(IpLongUtils.ip2Long(CommonUtil.getIpAddress(request)));
        logDO.setCreateTime(DateUtils.getTime());
        if ("登录".equals(array[1])) {
            MethodSignature methodSignature = (MethodSignature) signature;
            //获取请参数名称
            String[] properties = methodSignature.getParameterNames();
            for (int i = 0; i < properties.length; i++) {
                if ("userName".equals(properties[i])) {
                    UserLoginVO userLoginVO = userLoginService.getUserInformation(String.valueOf(joinPoint.getArgs()[i]), null);
                    if (userLoginVO != null) {
                        logDO.setUserId(userLoginVO.getUserId());
                    } else {
                        logDO.setUserName(String.valueOf(joinPoint.getArgs()[i]));
                    }
                }
            }
        } else if (!"登录".equals(array[1]) && !"上传分视频".equals(array[1])) {
            logDO.setUserId((Long) cache.getIfPresent(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue())));
        }
        logDO.setRequestParameter(parameter.toString());
        //获取接口请求是否成功,code值为00000代表成功否则失败
        int code = (ResultCodeEnum.SUCCESS.getCode().equals(jsonObject.get("code").getAsString())) ? 0 : 1;
        if (code == 0) {
            logDO.setOperationResult("成功");
        } else {
            if ("上传分视频".equals(array[1])) {
                  saveFileUploadFailed(gson,parameter);
//                JsonObject jsonObject1 = gson.fromJson(parameter.get("param").getAsString(), JsonObject.class);
//                String currentTime = DateUtils.getTime();
//                FileUploadFailedDO fileUploadFailedDO =new FileUploadFailedDO();
//                fileUploadFailedDO.setFileName(jsonObject1.get("name").getAsString());
//                fileUploadFailedDO.setUploadTime(currentTime);
//                fileService.saveFileUploadFailed(fileUploadFailedDO);
//                JSONObject jsonObject2 =new JSONObject();
//                JSONObject jsonObject3 =new JSONObject();
//                //获取未上传成功总数
//                int messageNumber = fileService.queryFileUploadFailed();
//                jsonObject3.put("messageNumber",messageNumber);
//                jsonObject2.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
//                jsonObject2.put("type", ThirdInterfaceConstant.WebSocketMsg.MESSAGE_NUMBER.getName());
//                jsonObject2.put("info", jsonObject3);
//                WebSocketServer.sendInfo(jsonObject2.toJSONString());
            }
            logDO.setOperationResult("失败");
            logDO.setFailureReason(jsonObject.get("msg").getAsString());
        }
        //保存日志
        fileVideoLogService.saveFileVideoLog(logDO);
        return (Result) object;
    }

    public void saveFileUploadFailed(Gson gson, JsonObject parameter) throws Exception {
        JsonObject jsonObject1 = gson.fromJson(parameter.get("param").getAsString(), JsonObject.class);
        String currentTime = DateUtils.getTime();
        FileUploadFailedDO fileUploadFailedDO = new FileUploadFailedDO();
        fileUploadFailedDO.setFileName(jsonObject1.get("name").getAsString());
        fileUploadFailedDO.setUploadTime(currentTime);
        fileService.saveFileUploadFailed(fileUploadFailedDO);
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        //获取未上传成功总数
        int messageNumber = fileService.queryFileUploadFailed();
        jsonObject3.put("messageNumber", messageNumber);
        jsonObject2.put(ThirdInterfaceConstant.WebSocketKey.STATE, ThirdInterfaceConstant.WebSocketKey.CODE_OK);
        jsonObject2.put("type", ThirdInterfaceConstant.WebSocketMsg.MESSAGE_NUMBER.getName());
        jsonObject2.put("info", jsonObject3);
        WebSocketServer.sendInfo(jsonObject2.toJSONString());
    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "execution(* com.vip.file.controller..*.*(..)) && @annotation(logTrack)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, LogTrack logTrack, Throwable e) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Signature signature = joinPoint.getSignature();
            JsonObject parameter = new JsonObject();
            Object[] objects = joinPoint.getArgs();
            parameter = getParameter(joinPoint, objects, signature);
            //获取请求时长 毫秒
            FileLogDO fileLogDO = new FileLogDO();
//
//
//        logDO.setBeginTime(LocalDateUtils.longToDateTime(beginTime));
//        logDO.setEndTime(LocalDateUtils.longToDateTime(endTime));
//        logDO.setRequestTime(requestTime);
//        logDO.setRequestInterface(signature.getName());
//        logDO.setRequestType(request.getMethod());
//        logDO.setOperation(logTrack.value());
//        logDO.setRequestArgs(parameter.toJSONString());
//        logDO.setResult(1);
//        //获取请求ip
//        logDO.setIp(IpLongUtils.ip2Long(CommonUtils.getRealIp(request)));
//            cache.getIfPresent(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue()));
//            logDO.setLogType(4);
//            logDO.setUserId((Long) cache.getIfPresent(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue())));
//
//        logDO.setAnomalousCause(e.getCause().toString());
//        //保存日志
//        systemLogService.saveLog(logDO);
//        if(e instanceof BadSqlGrammarException){
//            log.error(e.getMessage(),e);
//            throw new SqlException();
//        }else if(e instanceof  NullPointerException) {
//            log.error(e.getMessage(),e);
//            throw new CustomizeException("空指针异常");
//        }else {
//            log.error(e.getMessage(),e);
//            throw new CustomizeException();
//        }

        } catch (NullPointerException e1) {
        }
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint
     * @param objects
     * @param signature
     * @return
     */
    public JsonObject getParameter(JoinPoint joinPoint, Object[] objects, Signature signature) {
        JsonObject parameter = new JsonObject();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof HttpServletRequest || objects[i] instanceof MultipartFile) {
                continue;
            }
            //获取请求参数值
            Object args = joinPoint.getArgs()[i];
            Gson gson = new Gson();
            String json = gson.toJson(args);
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                //获取请参数名称
                String[] properties = methodSignature.getParameterNames();
                String parameterName = properties[i];
                parameter.addProperty(parameterName, json);
            }
        }
        return parameter;
    }

    public static void main(String[] args) {
        String a = "{\"targetId\":\"9\",\"type\":\"video/mp4\",\"uid\":\"o_1h1tlaqsb163e1qcf116a1i2mo808\",\"eventId\":\"9\",\"fileNames\":\"202310011505.mp4\",\"fileLength\":0,\"fileCount\":1,\"sessionId\":\"1685696372165\",\"chunks\":1,\"chunk\":0,\"size\":1186576,\"name\":\"202310011505.mp4\",\"file\":{\"part\":{\"fileItem\":{\"fieldName\":\"file\",\"contentType\":\"video/mp4\",\"isFormField\":false,\"fileName\":\"202310011505.mp4\",\"size\":-1,\"sizeThreshold\":0,\"repository\":{\"path\":\"C:\\\\Users\\\\zhongGuan-Mwj\\\\AppData\\\\Local\\\\Temp\\\\tomcat.1292824930336930413.9555\\\\work\\\\Tomcat\\\\localhost\\\\ROOT\"},\"headers\":{\"headerNameToValueListMap\":{\"content-disposition\":[\"form-data; name\\u003d\\\"file\\\"; filename\\u003d\\\"202310011505.mp4\\\"\"],\"content-type\":[\"video/mp4\"]}},\"defaultCharset\":\"ISO-8859-1\"},\"location\":{\"path\":\"C:\\\\Users\\\\zhongGuan-Mwj\\\\AppData\\\\Local\\\\Temp\\\\tomcat.1292824930336930413.9555\\\\work\\\\Tomcat\\\\localhost\\\\ROOT\"}},\"filename\":\"202310011505.mp4\"}}";
        Gson gson = new Gson();
        JsonObject jsonObject1 = gson.fromJson(a, JsonObject.class);
        System.out.println(jsonObject1.get("name").getAsString());

    }
}
