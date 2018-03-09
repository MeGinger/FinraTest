package com.assignment.assignment.controller;


import com.assignment.assignment.Exception.MetaDataNotFountException;
import com.assignment.assignment.Service.FileService;
import com.assignment.assignment.entity.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {


    @Autowired
    private FileService fileService;
    @GetMapping("/metadatas")
    public List<MetaData> retrieveAllMetaData(){
        return fileService.loadAllMetaData();
    }



    @GetMapping("/metadatas/{id}")
    public MetaData retrieveMetaData(@PathVariable int id){
        Optional<MetaData> metaDataOptional = fileService.loadMetaDataDetails(id);
        if(!metaDataOptional.isPresent()){
            throw new MetaDataNotFountException("id-"+id);
        }
        return metaDataOptional.get();

    }



    @GetMapping("/metadatas/recent")
    public List<MetaData>  retieveRecentMetaData(){
        List<MetaData> recentMetaData = fileService.recentMetaData();
        return recentMetaData;

    }

    @GetMapping(value="/metadatas/downloads/{id}")
    public FileSystemResource downLoadFile(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Optional<MetaData> metaData = fileService.loadMetaDataDetails(id);

        File file = fileService.getDownloadFile(id);
        String mediaType = URLConnection.guessContentTypeFromName(fileService.loadMetaDataDetails(id).get().getName());
        System.out.println(fileService.loadMetaDataDetails(id).get().getName());
        InputStream inputStream = new FileInputStream(metaData.get().getPath());
        response.addHeader("Content-disposition", "attachment;file=\""+ metaData.get().getName() );
        response.setContentType(mediaType);
        FileCopyUtils.copy(inputStream, response.getOutputStream());

        return new FileSystemResource(file);
//        String string = fileService.downloadFile(id, response, request);
//        return string;
    }


    @PostMapping("/metadatas")
    public String singleFileUpload(@RequestBody MultipartFile file){


        fileService.uploadFile(file);
        return "Success";
    }

}
