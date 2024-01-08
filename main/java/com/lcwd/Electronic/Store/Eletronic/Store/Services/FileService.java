package com.lcwd.Electronic.Store.Eletronic.Store.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile file,String path) throws IOException;      //This is to store images from client to server

    InputStream getUploadedImage(String path,String name) throws FileNotFoundException;

}
