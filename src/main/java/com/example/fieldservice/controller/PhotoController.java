package com.example.fieldservice.controller;

import com.example.fieldservice.service.EstimateService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;

@Controller
public class PhotoController {

    private final EstimateService estimateService;

    public PhotoController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    @GetMapping("/photos/{storedName}")
    public ResponseEntity<Resource> viewPhoto(@PathVariable String storedName) throws MalformedURLException {
        Resource file = estimateService.loadPhoto(storedName);
        MediaType mediaType = MediaTypeFactory.getMediaType(storedName).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + storedName + "\"")
            .body(file);
    }
}
