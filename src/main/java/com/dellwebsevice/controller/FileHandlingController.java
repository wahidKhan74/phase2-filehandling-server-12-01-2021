package com.dellwebsevice.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dellwebsevice.exception.StorageException;
import com.dellwebsevice.service.StorageService;

@Controller
public class FileHandlingController {
	
	@Autowired
	StorageService storageService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index() {
		return "index.html";
	}

	@RequestMapping(value="/do-upload", method=RequestMethod.POST, consumes= {"multipart/form-data"})
	public String upload(@RequestParam MultipartFile file) {
		// logic to upload file
		storageService.uploadFile(file);
		return "redirect:/success.html";
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFile(@RequestParam(value="filename") String filename){
		InputStreamResource resource;
		try {
		resource = new InputStreamResource(
				new FileInputStream(new File(storageService.getFile(filename))));
		} catch (Exception e) {
			throw new StorageException("Failed to download a file !");
		}
		// set up header for file download
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
		return ResponseEntity.ok().headers(headers).body(resource);
		
	}
	
	@ExceptionHandler(StorageException.class)
	public String storageExceptionHandler() {
		return "redirect:/failure.html";
	}
}
