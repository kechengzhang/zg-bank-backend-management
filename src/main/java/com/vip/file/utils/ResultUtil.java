package com.vip.file.utils;

import com.alibaba.fastjson.JSONObject;

import com.vip.file.common.ResultEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回工具类
 * @author 34897
 *
 */
public class ResultUtil {

    /**
     * 基础返回
     *
     * @param resultCode
     * @param resultMsg
     * @param detail
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static ResponseEntity<Result> base(int resultCode, String resultMsg, Object detail) {
        Result result = new Result(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        Result.Data data = result.new Data(resultCode, resultMsg, detail);
        result.setData(data);
        return new ResponseEntity<Result>(result, HttpStatus.OK);
    }

    /**
     * 分页返回
     *
     * @param resultCode
     * @param resultMsg
     * @param detail
     * @param page
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static ResponseEntity<Result> base(int resultCode, String resultMsg, Object detail, Page page) {
        Result result = new Result(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        Result.Data data = result.new Data(resultCode, resultMsg, detail, page);
        result.setData(data);
        return new ResponseEntity<Result>(result, HttpStatus.OK);
    }

    /**
     * 成功返回
     *
     * @param detail
     * @return
     */
    public static ResponseEntity<Result> success(Object detail) {
        return base(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), detail);
    }

    /**
     * 成功返回
     * @param resultMsg
     * @return
     */
    public static ResponseEntity<Result> success(String resultMsg) {
        return base(ResultEnum.SUCCESS.getCode(), resultMsg, null);
    }

    /**
     * 成功返回
     */
    public static ResponseEntity<Result> success() {
        return base(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 带分页信息的成功返回
     *
     * @param detail
     * @param page
     * @return
     */
    public static ResponseEntity<Result> success(Object detail, Page page) {
        return base(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), detail, page);
    }

    /**
     * 失败返回
     *
     * @return
     */
    public static ResponseEntity<Result> error() {
        return base(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMessage(), null);
    }
    

    /**
     * 失败返回
     * @param resultMsg
     * @return
     */
    public static ResponseEntity<Result> error(String resultMsg) {
        return base(ResultEnum.ERROR.getCode(), resultMsg, null);
    }

    /**
     * 调用第三方接口失败返回
     *
     * @return
     */
    public static ResponseEntity<Result> httpError() {
    	return base(ResultEnum.HTTP_ERROR.getCode(), ResultEnum.HTTP_ERROR.getMessage(), null);
    }
    
    /**
     * 调用第三方接口失败返回
     * @param resultMsg
     * @return
     */
    public static ResponseEntity<Result> httpError(String resultMsg) {
        return base(ResultEnum.HTTP_ERROR.getCode(), resultMsg, null);
    }
    
    /**
     * 异常返回
     *
     * @return
     */
    public static ResponseEntity<Result> exception() {
        return base(ResultEnum.EXCEPTION.getCode(), ResultEnum.EXCEPTION.getMessage(), null);
    }

    /**
     * 异常返回
     * @param resultMsg
     * @return
     */
    public static ResponseEntity<Result> exception(String resultMsg) {
        return base(ResultEnum.EXCEPTION.getCode(), resultMsg, null);
    }

    /**
     * 异常返回
     *
     * @param resultCode
     * @param resultMsg
     * @return
     */
    public static ResponseEntity<Result> exception(int resultCode, String resultMsg) {
        return base(resultCode, resultMsg, null);
    }

    
    @SuppressWarnings("unchecked")
	public static ResponseEntity<Result> dealData(Map<String,Object> map){
    	int code = Integer.parseInt(map.get("code").toString());
		String msg = map.get("msg").toString();
		Map<String,Object> dataMap = new HashMap<>();
		if(code==0) {
			dataMap = JSONObject.parseObject(map.get("data").toString(), Map.class);
			return success(dataMap);
		}else if(code==-2){
			return httpError("调用第三方服务失败："+code+msg);
		}else{
			return httpError("调用第三方服务失败"+code+msg);
		}
    }
}
