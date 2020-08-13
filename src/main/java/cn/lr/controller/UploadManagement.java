package cn.lr.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/UploadManagement")
@Controller
public class UploadManagement {
	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session,HttpServletRequest request) {
		LoggerUtil.LOGGER.info("-------------enter 上传图片信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, userId : {}", session.getId(), session.getAttribute("userId"));
		LoggerUtil.LOGGER.info("file: {}", file);
		try {
			// 获取原文件名称
			String fileName = file.getOriginalFilename();
			LoggerUtil.LOGGER.info("fileName : {}",fileName);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 保证文件名的唯一性：uuid 或者 时间戳
			String newFileName = sdf.format(new Date()) + fileName.substring(fileName.lastIndexOf("."));
			String folderPath = "D:\\upload/";
			File actFile = new File(folderPath);
			// 该目录是否已经存在
			if (!actFile.exists()) {
				// 创建文件夹
				actFile.mkdir();
			}
			// 文件输出流
			FileOutputStream fos;
			fos = new FileOutputStream(folderPath + newFileName);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
			return ResultJsonUtil.toJsonString(200, "/upload/" + newFileName, "上传图片成功",session.getId());
		}catch (IOException e) {
			return ResultJsonUtil.toJsonString(103,  null, "图片上传错误",session.getId());
		}catch (Exception e) {
			return ResultJsonUtil.toJsonString(404,  null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 上传图片信息--------------------");
		}
	}
}
