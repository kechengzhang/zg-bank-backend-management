package com.vip.file.quartz;

import com.vip.file.model.dto.SyntheticVideoDTO;
import com.vip.file.model.vo.FileCleanVO;
import com.vip.file.model.vo.FileTargetIdSizeVO;
import com.vip.file.service.FileCleanService;
import com.vip.file.service.SyntheticVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zkc
 * @description
 * @Date 2023/7/3 9:33 星期一
 * @Version 1.0
 */
//@Component
@Slf4j
public class FileCleanSchedule {
    @Autowired
    private FileCleanService fileCleanService;
    @Autowired
    private SyntheticVideoService syntheticVideoService;
    @Value("${file.directory-path}")
    private String directoryPath;
    @Value("${file.compose-path}")
    private String composePath;
    @Value("${file.save-path}")
    private String savePath;
    @Scheduled(cron = "0 */1 * * * ?")
    public void fileClean(){
        // 指定要获取磁盘使用情况的目录路径
        Path directory = Paths.get(directoryPath);
        // 获取合成视频目录的文件系统
        Path directory1 = Paths.get(composePath);
        //获取已合成视频目录的文件系统
        Path directory2 = Paths.get(savePath);
        try {
        FileStore fileStore = Files.getFileStore(directory);
        //需要合成视频
        FileStore fileStore1 = Files.getFileStore(directory1);
        //已合成视频
        FileStore fileStore2 = Files.getFileStore(directory2);
         //获取视频文件占总磁盘的占比
         FileCleanVO fileCleanVO = fileCleanService.getFileClean();
         //获取存储视频空间大小单位GB，磁盘总量 * 磁盘可使用占比
         Double totalSpace = (fileStore.getTotalSpace() * fileCleanVO.getTotalDiskPercentage())/ (1024 * 1024 * 1024 );



//            String directoryPath = "d:/temp";
//            File directory = new File(directoryPath);
//            long directorySize = getDirectorySize(directory);

         double a  =(double) fileStore1.getUnallocatedSpace() / (1024 * 1024 * 1024);
         double b  =(double) fileStore2.getUnallocatedSpace() / (1024 * 1024 * 1024);

           //查询当前视频文件大小
            double usedSpaceInGB = ((double) fileStore1.getTotalSpace() / (1024 * 1024 * 1024))+((double) fileStore2.getTotalSpace() / (1024 * 1024 * 1024));
            //当前视频文件所占文件空间占比
            Double cleanPercentage =usedSpaceInGB/totalSpace;
            //当前视频文件所占文件空间占比大于设置值进行视频文件删除
            if(cleanPercentage > fileCleanVO.getCleanPercentage()){
                 //查询所有合成视频文件
               List<FileTargetIdSizeVO>list =  fileCleanService.getFileTargetIdSize();
               long fileSize = 0;
               List<String> targetIds= new ArrayList<>();
               List<String> filePath= new ArrayList<>();
                SyntheticVideoDTO syntheticVideoDTO =new SyntheticVideoDTO();
               for(FileTargetIdSizeVO fileTargetIdSizeVO :list) {
                   targetIds.add(fileTargetIdSizeVO.getTargetId());
                   filePath.add(fileTargetIdSizeVO.getFilePath());
                   fileSize += fileTargetIdSizeVO.getSize();
                   //获取需要删除视频文件  当前视频文件大小-
                   Double deleteFileSize = usedSpaceInGB - ((double)totalSpace * fileCleanVO.getCleanPercentage());
                   if(fileSize >= deleteFileSize){
                       List<String> distinctTargetIdList = targetIds.stream()
                               .distinct()
                               .collect(Collectors.toList());
                       List<String> distinctFilePathList = targetIds.stream()
                               .distinct()
                               .collect(Collectors.toList());
                       //删除文件
                       syntheticVideoDTO.setId(distinctTargetIdList);
                       syntheticVideoDTO.setName(distinctFilePathList);
                       syntheticVideoService.deleteSyntheticVideo(syntheticVideoDTO);
                   }
               }

            }
        } catch (IOException e) {
              log.error(e.getMessage(),e);
        }
    }

    public static long getDirectorySize(File directory) {
        long size = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    }
                }
            }
        }

        return size;
    }

    public static void main(String[] args) {
        String directoryPath = "d:/temp";
        File directory = new File(directoryPath);
        long directorySize = getDirectorySize(directory);
        System.out.println("Directory Size: " +(double) directorySize /(1024 * 1024 * 1024) + " bytes");
    }

    }
