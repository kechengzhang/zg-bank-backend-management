package com.vip.file.mapper;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.common.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class UploadFileSqlProvider {
    public String selectUploadFilePage() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM  upload_file ");
        return sb.toString();
    }

    public String selectvideoFilePage(String id) {
        StringBuffer sb = new StringBuffer();

        if (!StringUtils.isEmpty(id)) {
            sb.append(" SELECT * FROM tb_files  where target_id ='" + id + "' order by file_name asc");
        }
        return sb.toString();
    }

    public String selectFileById(String id) {
        StringBuffer sb = new StringBuffer();

        if (!StringUtils.isEmpty(id)) {
            sb.append("  SELECT * FROM  upload_file where id = '" + id + "' ");
        }
        return sb.toString();
    }

    public String deleteUploadFileId(JSONObject param) {
        StringBuffer sb = new StringBuffer();
        if (param.getString("targetId") != null && "" != param.getString("targetId")) {
            sb.append("   delete from tb_files where target_id =  '" + param.getString("targetId") + "'");
        }
        return sb.toString();
    }


    public String selectvideoFile(String id) {
        StringBuffer sb = new StringBuffer();

        if (id != null && "" != id) {
            sb.append(" SELECT file_path FROM tb_files  where target_id ='" + id + "'");
        }
        return sb.toString();
    }

    /**
     * 更新总视频
     * @param id
     * @return
     */
    public String updateUploadFileById(Long id){
        SQL sql = new SQL();
        sql.UPDATE("upload_file");
        sql.SET("status = "+1);
        sql.WHERE("id = "+id);
        return sql.toString();
    }

    /**
     * 更新总视频
     * @param id
     * @return
     */
    public String selectUploadFileByKey(String id){
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * from upload_file where id ='" + id + "'");
        String s = sb.toString();
        return sb.toString();
    }

}
