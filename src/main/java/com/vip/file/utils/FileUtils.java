package com.vip.file.utils;

import java.io.*;
import java.util.*;

/**
 * @author  zkc
 */
public class FileUtils {
    /**
     * 遍历文件夹下的所有文件（递归方式）
     * @param path 路径(支持文件夹路径，也支持文件路径。如果path是文件夹路径，则返回该文件夹下的所有文件路径；如果path是文件路径，则返回该文件路径)
     * @param resultList 返回所有文件 list
     */
    public static void traverFolder2(String path, List<String> resultList) {
        File file = new File(path);
        // 判断参数path，路径是否存在
        if (! file.exists()) {
            System.out.println(String.format("路径[%s]不存在。", path));
        }

        // 判断参数path，是否是文件（只在第一次调用该方法时有用，第二次递归进入该方法，肯定不是文件，而是文件夹）
        if (file.isFile()) {
            resultList.add(file.getAbsolutePath());
            return;
        }

        // 判断参数apth, 是否是空文件夹
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println(String.format("路径[%s]是一个空文件夹。", path));
        }

        // 遍历，递归
        for (File file1 : files) {
            if (file1.isDirectory()) {
                System.out.println(String.format("文件夹：%s", file1.getAbsolutePath()));
                traverFolder2(file1.getAbsolutePath(), resultList);
            } else {
                System.out.println(String.format("文件：%s", file1.getAbsolutePath()));
                resultList.add(file1.getAbsolutePath());
            }
        }
    }

    /**
     * 遍历文件夹下的所有文件（非递归方式）
     * @param path 路径(支持文件夹路径，也支持文件路径。如果path是文件夹路径，则返回该文件夹下的所有文件路径；如果path是文件路径，则返回该文件路径)
     * @return 返回所有文件 resultList
     */
    public static List<String> traverFolder1(String path) {
        // folderNum 文件夹数量  fileNum 文件数量
        int folderNum = 0, fileNum = 0;
        // 所有文件存入 resultList
        List<String> resultList = new ArrayList<>();
        // 将目录下的文件/文件夹放入list
        LinkedList<File> list = new LinkedList<>();

        File file = new File(path);
        // 判断参数path，路径是否存在
        if (! file.exists()) {
            System.out.println(String.format("路径[%s]不存在。", path));
            return resultList;
        }

        // 判断参数path，是否是文件
        if (file.isFile()) {
            resultList.add(file.getAbsolutePath());
            fileNum++;
            System.out.println("文件夹数量:" + folderNum + ",文件数量:" + fileNum);
            return resultList;
        }

        // 判断参数path，是否是空文件夹
        if (file.listFiles() == null) {
            System.out.println(String.format("路径[%s]是一个空文件夹。", path));
            return resultList;
        }
        // 如果该路径下有文件/文件夹，则加入队列list
        list.addAll(Arrays.asList(file.listFiles()));

        // 如果list不为空，遍历
        while (!list.isEmpty()) {
            File file1 = list.removeFirst();
            if (file1.exists() && file1.isFile()) {
                resultList.add(file1.getAbsolutePath());
                System.out.println(String.format("文件：%s", file1.getAbsolutePath()));
                fileNum++;
                continue;
            }
            File[] files = file1.listFiles();
            if (files == null) {
                continue;
            } else {
                System.out.println(String.format("文件夹：%s", file1.getAbsolutePath()));
                folderNum++;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    System.out.println(String.format("文件夹：%s", f.getAbsolutePath()));
                    list.add(f);
                    folderNum++;
                } else {
                    System.out.println(String.format("文件：%s", f.getAbsolutePath()));
                    resultList.add(f.getAbsolutePath());
                    fileNum++;
                }
            }
        }

        System.out.println("文件夹数量:" + folderNum + ",文件数量:" + fileNum);

        System.out.println("resultList: " + resultList);
        return resultList;
    }

    /**
     * 分块写入文件
     * @param target
     * @param targetSize
     * @param src
     * @param srcSize
     * @param chunks
     * @param chunk
     * @throws IOException
     */
    public static void writeWithBlok(String target, Long targetSize, InputStream src, Long srcSize, Integer chunks, Integer chunk) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(target,"rw");
        randomAccessFile.setLength(targetSize);
        if (chunk == chunks - 1) {
            randomAccessFile.seek(targetSize - srcSize);
        } else {
            randomAccessFile.seek(chunk * srcSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            randomAccessFile.write(buf,0,len);
        }
        randomAccessFile.close();
    }


    /**
     * 写入文件
     * @param target
     * @param src
     * @throws IOException
     */
    public static void write(String target, InputStream src) throws IOException {
        OutputStream os = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            os.write(buf,0,len);
        }
        os.flush();
        os.close();
    }

    /**
     * 生成随机文件名
     * @return
     */
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }


    /**
     * 删除制定目录下文件
     *
     */
    public static void deleteFile(String filePath,String composePath){
        int secondSlashIndex = filePath.indexOf('/', filePath.indexOf('/') + 1);
        if (secondSlashIndex != -1) {
            String result = filePath.substring(secondSlashIndex + 1);
            File file = new File(composePath + result);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
