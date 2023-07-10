package com.vip.file.mapper;

import com.vip.file.model.dto.GetFileDto;
import com.vip.file.model.entity.FileUploadFailedDO;
import com.vip.file.model.entity.Files;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LEON
 * @since 2020-06-09
 */
@Mapper
public interface FilesMapper extends BaseMapper<Files> {
    /**
     * 获取文件列表
     *
     * @return
     */
    List<GetFileDto> selectFileList();

    /**
     * 判断文件是否已存在
     *
     * @param fileName
     * @return
     */
    boolean fileIsExist(@Param("fileName") String fileName);

    /**
     * 删除分视频
     * @param id
     * @return
     */
    int deleteFileById(@Param("id") String id);

    /**
     * 根据一个或多个属性获取Files
     * @param file
     * @return
     */
    GetFileDto getByFileByMd5(Files file);

    /**
     * 新增文件上传状态
     * @param files
     * @return
     */
    @InsertProvider(type =FileSqlProvider.class,method = "insertFiles" )
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Integer insertFiles(Files files);

    /**
     * 根据总视频查询当前已上传分视频数量
     * @param targetId
     * @return
     */
//    @Select({
//            "select count(s.id) from tb_files s where s.target_id = #{targetId,jdbcType=VARCHAR} "
//    })
    Integer getFilesCountByTargetId(@Param("targetId") String targetId);

//    @Select({
//            "<script>select * from tb_files a where  "
//                    + " a.file_name = #{fileName} "
//                    + " and <![CDATA[a.target_id <> #{targetId}]]></script>"
//    })
    List<Files> getFilesByCons(@Param("fileName") String fileName, @Param("targetId") String targetId);

    /**
     * 文件上传失败保存
     * @param fileUploadFailedDO
     * @return
     */
    int saveFileUploadFailed(@Param("fileUploadFailedDO") FileUploadFailedDO fileUploadFailedDO);

    /**
     * 获取未上传文件总数
     *
     * @return
     */
    int queryFileUploadFailed();

    /**
     * 获取上传文件路径
     *
     * @param targetId
     * @return
     */

    List<Map<String,String>> queryFilePath(@Param("targetId") String targetId);

    /**
     * 修改分视频文件路径
     *
     * @param id
     * @param filePath
     * @return
     */
    int updateTbFilesFilePath(@Param("id")String id,@Param("filePath")String filePath );
}
