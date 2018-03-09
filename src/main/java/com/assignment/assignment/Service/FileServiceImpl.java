package com.assignment.assignment.Service;

import com.assignment.assignment.DAO.MetaDataDAO;
import com.assignment.assignment.entity.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService{
    private static String UPLOADED_FOLDER="src/main/resources/uploadFile/";
    private File DOWNLOAD_FOLDER = new File("src/main/resources/downloadFile/");
    @Autowired
    private MetaDataDAO metaDataDAO;


    @Override
    public List<MetaData> loadAllMetaData() {
        return metaDataDAO.findAll();
    }

    @Override
    public Optional<MetaData> loadMetaDataDetails(int id) {
        Optional<MetaData> metaDataOptional= metaDataDAO.findById(id);
        return metaDataOptional;

    }
    @Override
    public File getDownloadFile(Integer id){
        Optional<MetaData> metaData = metaDataDAO.findById(id);
        File downloadFile = null;
        if (metaData != null) {
            String path = metaData.get().getPath();
            downloadFile = new File(path);
        }
        return downloadFile;
    }
    @Override
    public List<MetaData> recentMetaData(){
        return metaDataDAO.getrecent();
    }
    @Override
    public String downloadFile(Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {

        Optional<MetaData> metaData = metaDataDAO.findById(id);
//        FileInfo fileInfo = fileInfoService.getFileInfoById(id);
        if (metaData != null) {
            String path = metaData.get().getPath();
            File downloadFile = new File(path);
            String fileName = metaData.get().getName();
            String fileType= URLConnection.guessContentTypeFromName(fileName);

            //String fileType = URLConnection.guessContentTypeFromStream(new FileInputStream(new File(fileName)));
            //String fileType = Files.probeContentType(downloadFile.toPath());
            //System.out.println(downloadFile.toPath().toString());
            System.out.println(fileType);
            if (downloadFile.exists()) {
                InputStream inputStream = new FileInputStream(path);
                response.addHeader("Content-disposition", "attachment;file=" + metaData.get().getName());
                response.setContentType(fileType);
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                return "Succeed to DownloadFile";
            }
        }
        return "Fail to DownloadFile";
    }



    public String uploadFile(MultipartFile file){
        if (file.isEmpty()) {

            return "fileNotFound";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();
            Date date = new Date();
            Date time = date;
            long size = bytes.length;
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            System.out.println(path.toString());
            Files.write(path, bytes);
            MetaData metaData= new MetaData(name, path.toString(), time,size);
            metaDataDAO.save(metaData);

//            System.out.println(bytes.length);
//            System.out.println(file.getOriginalFilename());



//            System.out.println("finish uploading");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }

}
