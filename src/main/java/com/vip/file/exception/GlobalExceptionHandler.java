package com.vip.file.exception;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @author zkc
 * @version 1.0
 * 全局异常
 * @date 2021-06-22
 * @since jdk 1.8
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception exception) {
        if (exception instanceof SqlException) {
            return Result.failure(ResultCodeEnum.SQL_EXCEPTION);
        } else if (exception instanceof JwtDecodeException) {
            String msg = ((JwtDecodeException) exception).getMsg();
            return Result.failure(ResultCodeEnum.ERROR, msg);
        } else if (exception instanceof MissingServletRequestParameterException) {
            //缺少参数抛出异常
            String msg = MessageFormat.format("缺少参数{0}", ((MissingServletRequestParameterException) exception).getParameterName());
            return Result.failure(ResultCodeEnum.ERROR, msg);
        } else if (exception instanceof BindException) {
            //get请求的对象参数校验异常
            List<ObjectError> errors = ((BindException) exception).getBindingResult().getAllErrors();
            String msg = getValidExceptionMsg(errors);
            if (StringUtils.isNotBlank(msg)) {
                return Result.failure(ResultCodeEnum.ERROR, msg);
            }
            return Result.failure(ResultCodeEnum.ERROR, "");
        } else if (exception instanceof MethodArgumentNotValidException) {
            // post请求的对象参数校验异常
            List<ObjectError> errors = ((MethodArgumentNotValidException) exception).getBindingResult().getAllErrors();
            String msg = getValidExceptionMsg(errors);
            if (StringUtils.isNotBlank(msg)) {
                return Result.failure(ResultCodeEnum.ERROR, msg);
            }
            return Result.failure(ResultCodeEnum.ERROR, "");
        } else if (exception instanceof ConstraintViolationException) {
            //单参数异常
            Set<ConstraintViolation<?>> sets = ((ConstraintViolationException) exception).getConstraintViolations();
            if (CollectionUtils.isNotEmpty(sets)) {
                StringBuilder sb = new StringBuilder();
                sets.forEach(error -> {
                    if (error instanceof FieldError) {
                        sb.append(((FieldError) error).getField()).append(":");
                    }
                    sb.append(error.getMessage()).append(";");
                });
                String msg = sb.toString();
                msg = StringUtils.substring(msg, 0, msg.length() - 1);
                return Result.failure(ResultCodeEnum.ERROR, msg);
            }
            return Result.failure(ResultCodeEnum.ERROR, "");
        } else {
            return Result.failure(ResultCodeEnum.ERROR.getCode(), exception.getMessage());
        }
    }


    private String getValidExceptionMsg(List<ObjectError> errors) {
        if (CollectionUtils.isNotEmpty(errors)) {
            StringBuilder sb = new StringBuilder();
            errors.forEach(error -> {
                sb.append(error.getDefaultMessage()).append(";");
            });
            String msg = sb.toString();
            msg = StringUtils.substring(msg, 0, msg.length() - 1);
            return msg;
        }
        return null;
    }
}