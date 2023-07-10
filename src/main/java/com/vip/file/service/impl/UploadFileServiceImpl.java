package com.vip.file.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.vip.file.common.Convert;
import com.vip.file.common.Page;
import com.vip.file.common.StringUtils;
import com.vip.file.mapper.FilesMapper;
import com.vip.file.mapper.UploadFileMapper;
import com.vip.file.model.dto.UploadFileDto;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileDo;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.DateUtils;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-27
 */
@Service
@Slf4j
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFileDo> implements IUploadFileService {
    @Autowired
    private UploadFileMapper uploadFileMapper;
    @Autowired
    private FilesMapper filesMapper;
    @Value("${file.save-path:/data-center/files/vip-file-manager}")
    private String savePath;
    @Value("${file.conf-path:/data-center/files/vip-file-manager/conf}")
    private String confFilePath;

    @Value("${file.compose-path}")
    private String composePath;
    @Autowired
    MessagePushService messagePushService;
    @Value("${compose.compose_time}")
    private Integer compose_time;

    @Value("${compose.compose_rules}")
    private String compose_rules;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public UploadFileDo selectUploadFileById(String id) {
        return uploadFileMapper.selectUploadFileById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param uploadFile 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UploadFileDo> selectUploadFileList(UploadFileDo uploadFile) {
        return uploadFileMapper.selectUploadFileList(uploadFile);
    }

    /**
     * 新增总视频数据
     *
     * @param uploadFileDto
     * @return
     */
    @Override
    public int insertUploadFile(UploadFileDto uploadFileDto) {
        UploadFileDo uploadFile = new UploadFileDo();
        BeanUtils.copyProperties(uploadFileDto, uploadFile);
        return uploadFileMapper.insert(uploadFile);
    }

    /**
     * 修改总视频
     *
     * @param uploadFileDto
     * @return 结果
     */
    @Override
    public int updateUploadFile(UploadFileDto uploadFileDto) {
        LambdaUpdateWrapper<UploadFileDo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UploadFileDo::getId, uploadFileDto.getId())
                .set(UploadFileDo::getOccurTime, uploadFileDto.getOccurTime())
                .set(UploadFileDo::getOccurPlace, uploadFileDto.getOccurPlace())
                .set(UploadFileDo::getRemark, uploadFileDto.getRemark());
        return uploadFileMapper.update(null, lambdaUpdateWrapper);
    }

    /**
     * 更新总视频合成视频数据
     *
     * @param uploadFileDo
     * @return 结果
     */
    @Override
    public int updateUploadFile(UploadFileDo uploadFileDo) {
        return uploadFileMapper.updateUploadFile(uploadFileDo);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUploadFileByIds(String ids) {
        return uploadFileMapper.deleteUploadFileByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除总视频
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUploadFileById(String id) throws Exception {
        UploadFileDo uploadFileDo = uploadFileMapper.selectUploadFileById(id);
        if (uploadFileDo != null) {
            String path = uploadFileDo.getFilePath();
            if (!StringUtils.isEmpty(path)) {
                int secondSlashIndex = path.indexOf('/', path.indexOf('/') + 1);
                if (secondSlashIndex != -1) {
                    String result = path.substring(secondSlashIndex + 1);
                    File file = new File(composePath + result);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            uploadFileMapper.deleteUploadFileById(id);
            List<Map<String, Object>> list = uploadFileMapper.selectvideoFilePage(id);
            for (Map map : list) {
                String fileId = map.get("id").toString();
                deleteFileById(fileId);
            }
        }
        return 1;
    }

    /**
     * 删除分视频
     *
     * @param id
     * @return
     */
    @Override
    public int deleteFileById(String id) throws Exception {
        //获取上传视频数据
        Files files = filesMapper.selectById(id);
        String targetId = files.getTargetId();
        String path = files.getFilePath();
        String tempPath = path + "_temp";
        String fileName = files.getFileName();
//        File confFile = new File(confFilePath+"\\"+targetId+ File.separatorChar + fileName + ".conf");
        File confFile = new File(confFilePath + "/" + targetId + File.separatorChar + fileName + ".conf");
        if (confFile.exists()) {
            confFile.delete();
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        File tempFile = new File(tempPath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        int res = filesMapper.deleteFileById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("composeTime", "");
        Map<String, Object> fileMap = uploadFileMapper.getFileByTargetIdAndTime(targetId);
        if (fileMap != null && fileMap.size() > 0) {
            String time = fileMap.get("created_time").toString();
            jsonObject.put("composeTime", DateUtils.addDateMinut(compose_rules, time, compose_time));
            jsonObject.put("fileId", targetId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonObject.put("currentTime", sf.format(new Date()));
            messagePushService.sendComposeTime(ThirdInterfaceConstant.WebSocketMsg.CURRENT_TIME.getName(), jsonObject);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> selectUploadFilePage(Page page) {
        PageHelper.startPage(page.getCurrentPage(), page.getPerPageSize());
        return uploadFileMapper.selectUploadFilePage();
    }

    @Override
    public List<Map<String, Object>> selectUploadFileByKey(String id) {
        return uploadFileMapper.selectUploadFileByKey(id);
    }

    @Override
    public List<Files> selectFilesByTime(String time, String id) {
        return uploadFileMapper.selectFilesByTime(time, id);
    }

    @Override
    public List<Files> selectFilesById(String id) {
        return uploadFileMapper.selectFilesById(id);
    }

    @Override
    public List<Map<String, Object>> selectvideoFilePage(String id) {
        return uploadFileMapper.selectvideoFilePage(id);
    }

    @Override
    public List<Map<String, Object>> selectFileById(String id) {
        return uploadFileMapper.selectFileById(id);
    }

    @Override
    public List<Map<String, Object>> deleteUploadFileId(JSONObject param) {
        return uploadFileMapper.deleteUploadFileId(param);
    }

    @Override
    public List<Map<String, Object>> selectvideoFile(String id) {
        return uploadFileMapper.selectvideoFile(id);
    }

    @Override
    public Map<String, Object> getFileByTargetIdAndTime(String targetId) {
        return uploadFileMapper.getFileByTargetIdAndTime(targetId);
    }

}
