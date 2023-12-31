package com.vip.file.utils;

import com.vip.file.model.response.ErrorCode;
import com.vip.file.model.response.Result;
import com.vip.file.model.response.Results;

/**
 * 提供给NovelWeb工具的相关工具类
 *
 * @author wgb
 * @date 2020/9/1 14:53
 */
public class NovelWebUtils {
    /**
     * 上传文件结果转换为本系统的结果
     *
     * @param result
     * @return
     */
    public static Result<Object> forReturn(cn.novelweb.tool.http.Result<Object> result) {
        if ("200".equals(result.getCode())||"201".equals(result.getCode())) {
            return Results.newFailResult(ErrorCode.NO_ERROR, result.getMessage());
        }else if ("206".equals(result.getCode())) {
            return Results.newFailResult(ErrorCode.FILE_MISS_CHUNKS, result.getData(), result.getMessage());
        } else if ("404".equals(result.getCode())) {
            return Results.newSuccessResult(ErrorCode.NO_ERROR, result.getMessage());
        } else {
            return Results.newFailResult(ErrorCode.FILE_UPLOAD, result.getMessage());
        }
    }

    public static Result<Object> forReturn2(cn.novelweb.tool.http.Result<Object> result) {
        if ("200".equals(result.getCode())||"201".equals(result.getCode())) {
            return Results.newFailResult(ErrorCode.FILE_UPLOAD, result.getMessage());
        }else if ("206".equals(result.getCode())) {
            return Results.newFailResult(ErrorCode.FILE_MISS_CHUNKS, result.getData(), result.getMessage());
        } else if ("404".equals(result.getCode())) {
            return Results.newSuccessResult(ErrorCode.NO_ERROR, result.getMessage());
        } else {
            return Results.newFailResult(ErrorCode.FILE_UPLOAD, result.getMessage());
        }
    }

}
