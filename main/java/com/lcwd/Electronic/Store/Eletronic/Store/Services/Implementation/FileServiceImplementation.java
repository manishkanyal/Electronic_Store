package com.lcwd.Electronic.Store.Eletronic.Store.Services.Implementation;

import com.lcwd.Electronic.Store.Eletronic.Store.Exceptions.BadApiRequest;
import com.lcwd.Electronic.Store.Eletronic.Store.Services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImplementation.class);


    //This method for uploading image in backend system from frontend.
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalName=file.getOriginalFilename();
        logger.info("Original File name :{}", originalName);
        String filename= UUID.randomUUID().toString();
        String extension=originalName.substring(originalName.lastIndexOf("."));
        String filenameWithExtension=filename+extension;
        String fullPathWithFileName=path+ File.separator+filenameWithExtension;

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg"))
        {
            //file save
            File folder=new File(path);

            if(!folder.exists())
            {
                //Create the folder
                folder.mkdirs();     //Use mkdirs method becasue it creates sub folder in it.

            }

            //upload file in folder
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return filenameWithExtension;


        }
        else {
                throw new BadApiRequest("File with this "+extension+" not allowed!!");

        }

    }

    //This method is to send image file to client.
    @Override
    public InputStream getUploadedImage(String path, String name) throws FileNotFoundException {

        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
