package com.vip.file.utils;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.model.entity.Files;
import com.vip.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zkc
 * @description
 * @Date 2023/4/26 14:10 星期三
 * @Version 1.0
 */
@Slf4j
@Component
public class MergeVideoUtils {
    /**
     * 视频合成
     *
     * @param outputDir
     * @param outputFile
     * @param fileService
     * @param filesList
     * @param format
     * @return
     */
    public static boolean mergeVideo(String outputDir, String outputFile, String format, List<Files> filesList, IFileService fileService, Cache<String, Object> cache) {
        List<String> list = new ArrayList<>();
        CommonUtil commonUtil = new CommonUtil();
        //获取ffmpeg 路径String folderPath,String resourcePath
        String ffmpegPath = commonUtil.getFfmpeg(cache.getIfPresent("folderPath").toString(), cache.getIfPresent("resourcePath").toString());
        String filePath = getFilePath(filesList.get(0).getFilePath());
        for (Files files : filesList) {
            //判断视频是否需要解码
            boolean b  = isNeedsTransCode(files.getFilePath(),ffmpegPath);
            if(b){
                list.add(files.getFilePath());
                continue;
            }
            //转码后的文件路径
//            String decodingVideo = filePath +UUID.randomUUID()+".mp4";
            String decodingVideo = filePath + UuidUtils.uuid() + ".mp4";
            //原视频解码(此处比较耗时)
            FFmpegVideoConverter(ffmpegPath, files.getFilePath(), decodingVideo);
            list.add(decodingVideo);
            //通过id修改tb_files表中file_path
            fileService.updateTbFilesFilePath(files.getId(), decodingVideo);
            //删除原上传文件
            File file = new File(files.getFilePath());
            file.delete();
        }
        try {
            List<String> commandList = new ArrayList<>();
            StringBuilder concat = new StringBuilder();
            List<String> list1 = new ArrayList<>();
            List<String> inputList = new ArrayList<>();
            list1.add(ffmpegPath);
            if (list.size() == 1) {
                try {
                    String command = String.format(format, ffmpegPath, list.get(0), outputDir + outputFile);
                    if (execCommand(command) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return false;
                }
            } else {
                for (String l : list) {
//                    UUID uuid = UUID.randomUUID();
                    String uuid = UuidUtils.uuid();
                    String input = "input" + uuid + ".ts";
                    String command = String.format(format, ffmpegPath, l, outputDir + input);
                    commandList.add(command);
                    concat.append("%s|");
                    list1.add(outputDir + input);
                    inputList.add(input);
                }
            }
            list1.add(outputFile);
            String format3 = "%s -i  concat:" + concat.toString() + " -c copy -bsf:a aac_adtstoasc -movflags +faststart %s";
            String command3 = String.format(format3, list1.toArray());
            commandList.add(command3);
            commandList.stream().forEach(c -> {
                if (execCommand(c) > 0) {
                    return;
                }
            });
            //删除ts文件
            for (String l : inputList) {
                File file = new File(outputDir + l);
                file.delete();
            }
            File fileffmpeg = new File(ffmpegPath);
            fileffmpeg.delete();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private static Integer execCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            //获取进程的标准输入流
            final InputStream is1 = process.getInputStream();
            //获取进城的错误流
            final InputStream is2 = process.getErrorStream();
            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            readInputStream(is1);
            readInputStream(is2);
            process.waitFor();
            process.destroy();
            log.info("-----操作成功:{}", command);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("-----操作失败", command);
            return -1;
        }
    }

    private static void readInputStream(InputStream inputStream) {
        new Thread(() -> {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line1 = null;
                while ((line1 = br1.readLine()) != null) {
                    if (line1 != null) {
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }).start();
    }

    /**
     * @param ffmpegPath    路径
     * @param originalVideo 转码前文件
     * @param decodingVideo 转码后文件
     */
    public static void FFmpegVideoConverter(String ffmpegPath, String originalVideo, String decodingVideo) {
        // 构建FFmpeg命令行
        String[] command = {
                ffmpegPath,
                "-i", originalVideo,
                // 使用libx264解码器
                "-c:v", "libx264",
                // 使用AAC解码器
                "-c:a", "aac",
                // 使用快速预设以加快处理速度
                "-preset", "fast",
                // 设置视频质量，值越小质量越高（18-28）
                "-crf", "23",
                // 设置解码时使用的线程数
//                "-threads", "4",
                // 覆盖输出文件
                "-y",
                decodingVideo
        };
        try {
            // 执行FFmpeg命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            // 读取FFmpeg输出日志
            InputStream inputStream = process.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
            }
            // 等待FFmpeg完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("解码完成，输出文件路径:{}", decodingVideo);
            } else {
                log.info("解码失败，退出代码:{}", exitCode);
            }
            // 关闭资源
            reader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void FFmpegVideoConverter1(String ffmpegPath, String originalVideo, String decodingVideo) {
//        // 构建FFmpeg命令行
//        String[] command = {
//                ffmpegPath,
//                "-i", originalVideo,
//                // 使用libx264解码器
//                "-c:v", "libx264",
//                // 使用AAC解码器
//                "-c:a", "aac",
//                // 使用快速预设以加快处理速度
//                "-preset", "fast",
//                // 设置视频质量，值越小质量越高（18-28）
//                "-crf", "23",
//                // 覆盖输出文件
//                "-y",
//                decodingVideo
//        };
//
//        try {
//            // 执行FFmpeg命令
//            ProcessBuilder processBuilder = new ProcessBuilder(command);
//            Process process = processBuilder.start();
//
//            // 异步读取输出流
//            CompletableFuture<Void> outputReaderFuture = CompletableFuture.runAsync(() -> {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // 处理输出日志，可以选择输出到日志文件或打印到控制台
//                        // System.out.println(line);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            // 等待FFmpeg完成
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                log.info("解码完成，输出文件路径:{}", decodingVideo);
//            } else {
//                log.info("解码失败，退出代码:{}", exitCode);
//            }
//            // 等待输出流读取完成
//            outputReaderFuture.join();
//
//        } catch (IOException | InterruptedException e) {
//            log.error(e.getMessage(), e);
//        }
//    }

    /**
     * 获取文件存储路径
     */
    public static String getFilePath(String filePath) {
        String[] array = filePath.split("/");
        int length = array.length;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            stringBuilder.append(array[i]).append("/");
        }
        return stringBuilder.toString();

    }
    /**
     * 判断视频是否需要解码
     *
     * @param videoFilePath 视频文件
     * @param ffmpegPath    ffmpeg文件目录
     * @return
     */
    public static boolean isNeedsTransCode(String videoFilePath, String ffmpegPath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath, "-i", videoFilePath);
            Process process = processBuilder.start();
            InputStream errorStream = process.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // 在输出中查找编解码器信息
                if (line.contains("h264") || line.contains("MPEG-4")) {
                    log.info("视频解码信息:{}", line);
                    return true;
                }
            }
            process.waitFor();
            return false;
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


}
