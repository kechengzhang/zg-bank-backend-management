package com.vip.file.mapper;

import com.vip.file.model.entity.Files;
import org.apache.ibatis.jdbc.SQL;

public class FileSqlProvider {


    /**
     * 新增文件上传状态
     * @param files
     * @return
     */
    public String insertFiles(Files files) {
        SQL sql = new SQL();
        sql.INSERT_INTO("tb_files");
        if (files.getId()!=null) {
            sql.VALUES("id", "#{id,jdbcType=VARCHAR}");
        }
        if (files.getType()!=null) {
            sql.VALUES("type", "#{type,jdbcType=VARCHAR}");
        }
        if (files.getFilePath()!=null) {
            sql.VALUES("file_path", "#{filePath,jdbcType=VARCHAR}");
        }
        if (files.getTargetId()!=null) {
            sql.VALUES("target_id", "#{targetId,jdbcType=VARCHAR}");
        }
        if (files.getFileName()!=null) {
            sql.VALUES("file_name", "#{fileName,jdbcType=VARCHAR}");
        }
        if (files.getChunk()!=null) {
            sql.VALUES("chunk", "#{chunk,jdbcType=INTEGER}");
        }
        if (files.getChunks()!=null) {
            sql.VALUES("chunks", "#{chunks,jdbcType=INTEGER}");
        }
        if (files.getSize()!=null) {
            sql.VALUES("size", "#{size,jdbcType=INTEGER}");
        }
        if (files.getMd5()!=null) {
            sql.VALUES("md5", "#{md5,jdbcType=VARCHAR}");
        }
        if (files.getStatus()!=null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        if (files.getBankBranchId()!=null) {
            sql.VALUES("bank_branch_id", "#{bankBranchId,jdbcType=VARCHAR}");
        }
        if (files.getBankBranchName()!=null) {
            sql.VALUES("bank_branch_name", "#{bankBranchName,jdbcType=VARCHAR}");
        }
        sql.VALUES("created_time", "now()");
        return sql.toString();
    }

}
