package com.dellwebsevice.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dellwebsevice.exception.StorageException;

@Service
public class StorageService {
	
	@Value("${upload.path}")
	private String path;
	
	// upload file logic
	public void uploadFile(MultipartFile file) {
		// file is empty
		if(file.isEmpty()) {
			throw new StorageException("Failed to upload a file, file is empty !");
		}
		// file upload logic
		try {
			String filename = file.getOriginalFilename();
			InputStream filesrc = file.getInputStream();
			Files.copy(filesrc, Paths.get(path+filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new StorageException("Failed to upload a file !");
		}
	}
	
	public String getFile(String filename) {
		return path+filename;
	}
}
