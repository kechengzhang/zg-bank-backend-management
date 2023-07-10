//package com.vip.file.utils;
//
//import cn.hutool.core.date.DateUtil;
//import com.vip.file.ffmpeg.Encoder;
//import com.vip.file.ffmpeg.MultimediaInfo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * @author: LiYaQ
// * @description:
// * @date: Created in 2019/3/10 11:45
// * @modified By:
// */
//public class VideoInfoUtil {
//    private static final Logger log = LoggerFactory.getLogger(VideoInfoUtil.class);
////    private static final String ffmpegPath = "C:\\Users\\34897\\AppData\\Local\\Temp\\java-1\\ffmpeg.exe";
////      private static final String ffmpegPath = "/home/user/wuxibank/ffmpeg-git-20220722-amd64-static/ffmpeg";
//    private static final String ffmpegPath = "/usr/bin/ffmpeg";
////    private static final String ffmpegPath = "D:/aaaa/ffmpeg.exe";
//
//    public static void main(String[] args) {
//
//
//
//
////        List<String> input = new ArrayList<String>();
////        input.add("D:\\temp\\file-manager\\5\\8.mp4");
////        input.add("D:\\temp\\file-manager\\5\\10.mp4");
////        input.add("D:\\temp\\file-manager\\5\\11.mp4");
////        String outputDir = "D:\\temp\\";
////        String output = "D:\\temp\\test.mp4";
////        input.add("D:\\temp\\file-manager\\5\\ch01_20220625110000.mp4");
////        input.add("D:\\temp\\file-manager\\5\\ch02_20220625110000.mp4");
////        mergeVideo(input,outputDir,output);
//    }
//    public static String mergeVideo2() {
//        String[] cmd = new String[]{"sh","-c","/home/user/wuxibank/ffmpeg-git-20220722-amd64-static/ffmpeg -i 'concat:/home/user/wuxibank/temp/files/input0.ts|/home/user/wuxibank/temp/files/input1.ts' /home/user/wuxibank/temp/files/10f3b055-5b06-4ce8-a50c-be2e1d00e8b1.mp4"};
//        execCommand2(cmd);
//        return null;
//    }
//
////    public static String mergeVideo(List<String> list, String outputDir, String outputFile,int height,int width) {
//    public static String mergeVideo(List<String> list, String outputDir, String outputFile) {
//        log.info("临时文件存放路径============"+outputDir);
//        log.info("合成文件存放路径============"+outputFile);
//        try {
//            String format1 = "%s -i %s -s 368x208 -b:v 768k %s";
////            String format1 = "%s -i %s -s %sx%s -b:v 768k %s";
//            int input = 0;
//            List<String> urlList = new ArrayList<>();
//            String[] str = new String[list.size()+2];
//            str[0]=ffmpegPath;
//            String format3 = "%s -i 'concat:%s";
//            for(int i = 0; i< list.size();i++){
//                input = i;
//                if(i!=0){
//                    format3+="|%s";
//                }
//                log.info("execCommand============"+i);
//
////                execCommand2(new String[]{"sh","-c",String.format(format1, ffmpegPath, list.get(i),width,height, outputDir + "input"+input+".ts")});
//                execCommand2(new String[]{"sh","-c",String.format(format1, ffmpegPath, list.get(i), outputDir + "input"+input+".ts")});
//                str[i+1]=outputDir + "input"+input+".ts";
//            }
//            format3+="' %s";
////            String command1 = String.format(format1, ffmpegPath, list.get(0), outputDir + "input1.ts");
////            String command2 = String.format(format1, ffmpegPath, list.get(1), outputDir + "input2.ts");
//
//            str[list.size()+1]=outputFile;
////            String format4 = "%s -i \"concat:%s|%s"+"\" %s";
//            String command3 = String.format(format3, str);
//            log.info(command3);
////            execCommand(command3);
////            String command3 = String.format(format4, ffmpegPath, outputDir + "input1.ts", outputDir + "input2.ts", outputFile);
//            System.out.println("command3######=     "+command3);
//            log.info("执行cmmand3########");
//            String[] cmd = new String[]{"sh","-c",command3};
//            int num = execCommand2(cmd);
//            log.info("执行cmmand3完成########");
//            if (num > 0) {
//                for (String s:str) {
//                    if(s.contains("input")){
//                        File file = new File(s);
//                        file.delete();
//                    }
//                }
////                File file1 = new File(outputDir + "input1.ts");
////                File file2 = new File(outputDir + "input2.ts");
////                file1.delete();
////                file2.delete();
//                return "1";
//            } else {
//                return "0";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("-----合并失败!!!!!!" + outputFile);
//            return "0";
//        }
//    }
//
//    private  static Integer execCommand2(String[] command) {
//            try {
//                final Process p = Runtime.getRuntime().exec(command);
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        BufferedReader br = new BufferedReader(
//                                new InputStreamReader(p.getInputStream()));
//                        try {
//                            while (br.readLine() != null) {
//
//                            }
//                            br.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                BufferedReader br = null;
//                br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                   log.info(line);
//                }
//                p.waitFor();
//                br.close();
//                p.destroy();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return 1;
//        }
//    private static Integer execCommand(String command) {
//        try {
//            Process process = Runtime.getRuntime().exec(command);
//            //获取进程的标准输入流
//            final InputStream is1 = process.getInputStream();
//            //获取进城的错误流
//            final InputStream is2 = process.getErrorStream();
//            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
//            readInputStream(is1);
//            readInputStream(is2);
//            process.waitFor();
//            process.destroy();
//            log.info("-----操作成功" + command + " " + DateUtil.now());
//            return 1;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("-----操作失败" + command);
//            return -1;
//        }
//    }
//
//    private static void readInputStream(InputStream inputStream) {
//        new Thread(() -> {
//            BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
//            try {
//                String line1 = null;
//                while ((line1 = br1.readLine()) != null) {
//                    log.info("readInputStream####################"+line1);
//                    if (line1 != null) {
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    inputStream.close();
//                    log.info("关闭inputStream" );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    log.info("inputStream关闭inputStream" );
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 通过视频文件获取视频信息
//     *
//     * @param videoPath
//     * @return
//     */
//    public static MultimediaInfo getVideoInfoByFile(String videoPath) {
//        try {
//            File file = new File(videoPath);
//            Encoder encoder = new Encoder();
//            MultimediaInfo m = encoder.getInfoByFile(file);
//            if (null != m) {
//                m.setVideoSize(file.length());
//            }
//            return m;
//        } catch (Exception e) {
//            log.error("获取播放播放时长异常 videoPath=" + videoPath, e);
//        }
//        return null;
//    }
//
//    /**
//     * 通过视频地址获取视频信息
//     *
//     * @param videoUrl
//     * @return
//     */
//    public static MultimediaInfo getVideoInfoByUrl(String videoUrl, String ua, int timeout, boolean ifProxy) {
//        try {
//            long start = System.currentTimeMillis();
//            Encoder encoder = new Encoder();
//            MultimediaInfo m = encoder.getInfoByUrl(videoUrl, ua,timeout, ifProxy);
//            long end = System.currentTimeMillis();
//            log.info("获取视频宽高时长,duration={}; 耗时={}", m.getDuration(), (end - start));
//            return m;
//        } catch (Exception e) {
//            log.error("获取视频信息异常 videoUrl=" + videoUrl, e);
//        }
//        return null;
//    }
//
//    /**
//     * 下载m3u8视频
//     *
//     * @param url    m3u8播放地址
//     * @param output 视频输出路径
//     * @return
//     */
//    public static boolean downloadAndMergeM3U8Video(String url, String output) {
//        try {
//            long start = System.currentTimeMillis();
//            Encoder encoder = new Encoder();
//            boolean b = encoder.mergeM3U8Video(url, output);
//            long end = System.currentTimeMillis();
//            log.info("url={} output={} m3u8视频耗时={}", url, output, (end - start));
//            return b;
//        } catch (Exception e) {
//            log.error("合并视频异常 url={} output={}", url, output, e);
//        }
//        return false;
//    }
//
//    /**
//     * 合并视频
//     *
//     * @param output 视频的输出位置
//     * @param input  分段视频
//     * @return
//     */
//    public static boolean mergeVideo(String output, List<String> input) {
//        try {
//            if (null == output || null == input) {
//                return false;
//            }
//            long start = System.currentTimeMillis();
//            Encoder encoder = new Encoder();
//            boolean b = encoder.mergeVideo(output, input.toArray(new String[input.size()]));
//            long end = System.currentTimeMillis();
//            log.info("input={} output={} 合并视频耗时={}", input, output, (end - start));
//            return b;
//        } catch (Exception e) {
//            log.error("合并视频异常 input=" + input + " output" + output, e);
//        }
//        return false;
//    }
//
//
//
//    /**
//     * 截封面图
//     *
//     * @param input     视频文件或地址
//     * @param time      截图的固定时间点
//     * @param imgOutPut 图片的输出路径
//     * @return 是否成功
//     */
//    public static boolean videoScreenshot(String input, String time, String imgOutPut) {
//        try {
//            long start = System.currentTimeMillis();
//            Encoder encoder = new Encoder();
//            String imgPath = null;
//            if (null == imgOutPut) {
//                File temp = new File(System.getProperty("user.dir"), "work");
//                if (!temp.exists()) {
//                    temp.mkdirs();
//                }
//                imgPath = temp.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + ".png";
//            } else {
//                imgPath = imgOutPut;
//            }
//            boolean b = encoder.videoScreenshot(input, time, imgPath);
//            long end = System.currentTimeMillis();
//            log.info("input={} imgPath={} 截图耗时={}", input, imgOutPut, (end - start));
//            return b;
//        } catch (Exception e) {
//            log.error("视频截图异常 time=" + time + " output" + input, e);
//        }
//
//        return false;
//    }
//
//    /**
//     * 压缩视频
//     *
//     * @param output
//     * @param input
//     * @return
//     */
//    public static boolean compressVideo(String output, String input) {
//        try {
//            long start = System.currentTimeMillis();
//            Encoder encoder = new Encoder();
//            boolean b = encoder.compressVideo(output, input);
//            long end = System.currentTimeMillis();
//            log.info("input=" + input + " output=" + output + "压缩视频耗时=" + (end - start));
//            return b;
//        } catch (Exception e) {
//            log.error("压缩视频异常 input=" + input + " output" + output, e);
//        }
//        return false;
//    }
//
//}
