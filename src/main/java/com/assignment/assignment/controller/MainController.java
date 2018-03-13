package com.assignment.assignment.controller;


import com.assignment.assignment.Exception.MetaDataNotFountException;
import com.assignment.assignment.Service.FileService;
import com.assignment.assignment.Service.MailService;
import com.assignment.assignment.entity.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import java.util.List;
import java.util.Optional;

@RestController
public class MainController {


    @Autowired
    private FileService fileService;

    @Value("${sendTo}")
    String sendTo;

    @Value("${topic}")
    String topic;
    @Autowired
    MailService mailService;
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


    //Retrive the most recent 1 hour meta-datas
    @GetMapping("/metadatas/recent")
    public List<MetaData>  retieveRecentMetaData() throws MessagingException {
        List<MetaData> recentMetaData = fileService.recentMetaData();
        mailService.send(sendTo,topic,getBody());
        return recentMetaData;

    }

    @Scheduled(fixedDelay = 1000)
    public void sendEmail() throws MessagingException {
        String body = getBody();
        mailService.send(sendTo, topic, body);
    }

    private String getBody() {
        StringBuilder sb = new StringBuilder();
        List<MetaData> fileInfoList = fileService.recentMetaData();
        for (MetaData metaData : fileInfoList) {
            sb.append(metaData.toString()+"\r\n");
        }
        return sb.toString();
    }
    //Download the file by its Id.

    @GetMapping(value = "/metadatas/name/{name}")
    public Integer getId(@PathVariable("name") String name){
        return fileService.getIDByname(name);
    }
    @GetMapping(value="/metadatas/downloads/{id}")
    public void downLoadFile(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Optional<MetaData> metaData = fileService.loadMetaDataDetails(id);
        if(!metaData.isPresent()){
            throw new MetaDataNotFountException("id-"+id);
        }

        File file = fileService.getDownloadFile(id);
        String mediaType = URLConnection.guessContentTypeFromName(fileService.loadMetaDataDetails(id).get().getName());
        if(mediaType==null){
            mediaType = "application/octet-stream";
        }
        System.out.println(fileService.loadMetaDataDetails(id).get().getName());
        InputStream inputStream = new FileInputStream(metaData.get().getPath());
        response.addHeader("Content-disposition", "attachment; filename=\""+ metaData.get().getName()+"\"" );
        response.setContentType(mediaType);
        FileCopyUtils.copy(inputStream, response.getOutputStream());


    }

    //Retrieve all metadatas
    @PostMapping("/metadatas")
    public String singleFileUpload(@RequestBody MultipartFile file){


        fileService.uploadFile(file);
        return "Success";
    }

}
