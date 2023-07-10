package com.vip.file.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zkc
 *
 */
public class CommonUtil {

	public static String getErrorMessage(StackTraceElement stackTraceElement, String errorMessage) {
		StringBuilder result = new StringBuilder();
		result.append("[" + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName()
				+ "]:failed. throw e:" + errorMessage);
		return result.toString();
	}

	public static String getErrorMessage(StackTraceElement stackTraceElement, Exception e) {
		StringBuilder errorMessage = new StringBuilder();
		errorMessage.append(e.toString() + "\n");
		StackTraceElement[] stacks = e.getStackTrace();
		if (stacks != null) {
			for (StackTraceElement stack : stacks) {
				errorMessage.append("\t" + stack.toString() + "\n");
			}
		}
		return getErrorMessage(stackTraceElement, errorMessage.toString());
	}

	public static List<String> getArrayListRecord(List<String> array, int count) {
		int size = array.size();
		if (size < count) {
			count = size;
		}
		List<String> newarr = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			newarr.add(array.get(size - 1 - i));
		}
		return newarr;
	}

	public static String getFormatDateStr(Date date, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}

	/**
	 * 这个方法我看不懂啊
	 * 但是我用的美滋滋啊
	 *
	 * @param dataBody
	 * @return
	 */
	public static double getFileB64Size(String dataBody) {
		int dataSize = dataBody.length();
		String tailStr = dataBody.substring(dataSize - 10);
		int equalIndex = tailStr.indexOf("=");
		if (equalIndex > 0) {
			dataSize = dataSize - (10 - equalIndex);
		}
		double fileSize = dataSize - ((double) dataSize / 8) * 2;
		return fileSize;
	}

	public static String getPageTitleNew(List<String> titleList, String titleBase, int type) {
		String pageTitle = "";
		if (type == 1) {
			int count = 1;
			pageTitle = titleBase + count;
			while (titleList.contains(pageTitle)) {
				count++;
				pageTitle = titleBase + count;
			}
		} else if (type == 2) {
			int count = 1;
			pageTitle = titleBase;
			while (titleList.contains(pageTitle)) {
				count++;
				pageTitle = titleBase + "(" + count + ")";
			}
		} else {
			pageTitle = "页面";
		}
		return pageTitle;
	}

	/**
	 * 获取ip
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ConstantEnum.UNKNOWN.getValue().equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ConstantEnum.UNKNOWN.getValue().equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ConstantEnum.UNKNOWN.getValue().equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ConstantEnum.UNKNOWN.getValue().equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ConstantEnum.UNKNOWN.getValue().equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取ffmpeg路径
	 * @param folderPath
	 * @param resourcePath
	 * @return 返回路径
	 */

	public  String getFfmpeg(String folderPath,String resourcePath) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        // 获取资源文件的输入流
		if (inputStream != null) {
			try {
				File folder = new File(folderPath);
				if (!folder.exists()) {
					// 使用 mkdirs() 方法创建文件夹，如果存在多级父文件夹也会一并创建
					folder.mkdirs();
				}
				// 创建临时文件
				File tempFile = File.createTempFile("ffmpeg", "", folder);
				tempFile.deleteOnExit();
				// 将资源文件内容复制到临时文件
				Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// 设置临时文件的执行权限
				tempFile.setExecutable(true);
				// 获取临时文件的路径
				String filePath = tempFile.getAbsolutePath();
				// 在这里可以使用 filePath 进行进一步的操作
				return filePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

