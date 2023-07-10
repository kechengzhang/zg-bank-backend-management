package com.vip.file.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zkc
 *
 * @date  2021-06-16 14:50
 *
 * 返回结果
 *
 * @param <T>
 */
@Data
@ApiModel(value = "返回类")
public class Result<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	private T data;
	public Result() {
	}
	public Result(ResultCodeEnum resultCode) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
	}

	public Result(ResultCodeEnum resultCode, T data) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
		this.data = data;
	}


	public static <T>Result<T> success(ResultCodeEnum resultCode) {
		Result<T> result = new Result<T>();
		result.setCode(resultCode.getCode());
		result.setMsg(resultCode.getMessage());
		return result;
	}
	public static <T>Result<T> success(ResultCodeEnum resultCode, T data) {
		Result<T> result = new Result<T>();
		result.setCode(resultCode.getCode());
		result.setMsg(resultCode.getMessage());
		result.setData(data);
		return result;
	}
	public static <T>Result<T> failure(ResultCodeEnum resultCode) {
		Result<T> result = new Result<T>();
		result.setCode(resultCode.getCode());
		result.setMsg(resultCode.getMessage());
		return result;
	}
	public static <T>Result<T> failure(ResultCodeEnum resultCode, String msg) {
		Result<T> result = new Result<T>();
		result.setCode(resultCode.getCode());
		result.setMsg(msg);
		return result;
	}
	public static <T>Result<T> failure(ResultCodeEnum resultCode, T data) {
		Result<T> result = new Result<T>();
		result.setCode(resultCode.getCode());
		result.setMsg(resultCode.getMessage());
		return result;
	}

	public static <T>Result<T> failure(String code,String msg) {
		Result<T> result = new Result<T>();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
}
