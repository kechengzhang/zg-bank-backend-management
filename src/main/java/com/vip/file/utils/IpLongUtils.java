package com.vip.file.utils;


import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zkc
 *
 *
 */
public class IpLongUtils {
    /**
     * 把字符串IP转换成long
     *
     * @param ipStr 字符串IP
     * @return IP对应的long值
     */
    public static long ip2Long(String ipStr) {
        if (ConstantEnum.IP.getValue().equals(ipStr)) {
            ipStr = "127.0.0.1";
        }
            String[] ip = ipStr.split("\\.");
            return (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16)
                    + (Long.valueOf(ip[2]) << 8) + Long.valueOf(ip[3]);
        }

    /**
     * 把IP的long值转换成字符串
     *
     * @param ipLong IP的long值
     * @return long值对应的字符串
     */
    public static String long2Ip(long ipLong) {
        StringBuilder ip = new StringBuilder();
        ip.append(ipLong >>> 24).append(".");
        ip.append((ipLong >>> 16) & 0xFF).append(".");
        ip.append((ipLong >>> 8) & 0xFF).append(".");
        ip.append(ipLong & 0xFF);
        return ip.toString();
    }


    public static  List<Integer> getSum(int []numbers,int size){
       List<Integer> list =new ArrayList<>();
           for (int i = 0; i < numbers.length + 1 - size; i++) {
               int[] newNumbers = Arrays.copyOfRange(numbers, i, i + size);
               Arrays.sort(newNumbers);
               int temp = newNumbers[newNumbers.length - 1];
               list.add(temp);
           }
        return list;
    }
    public static  int getMaxNumber(int [] numbers){
        int temp = numbers[0];
        for (int i=0;i<numbers.length;i++){
            if(numbers[i]>temp){
                temp=numbers[i];
            }
        }
      return temp;
    }

    /**
     * 获取服务器ip
     * @return
     */
        public static String getServerIP() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                String ipAddress = localhost.getHostAddress();
                return ipAddress;
            } catch (Exception e) {
               e.printStackTrace();
            }
              return null;
        }
    public static void main(String[] args) {
//        System.out.println(CronExpression.isValidExpression(""));
//        System.out.println(long2Ip(2987026340L));
        try {
            FileChannel readChannel = FileChannel.open(Paths.get("D:\\1111.txt"), StandardOpenOption.READ);
            long len = readChannel.size();
            long position = readChannel.position();

            FileChannel writeChannel = FileChannel.open(Paths.get("D:\\2222.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            //数据传输
            readChannel.transferTo(position, len, writeChannel);
            readChannel.close();
            writeChannel.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
