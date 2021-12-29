package com.tutorial.apitest.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;
@Service
public class EmojiStorageService implements  IStorageService{

    private final Path storageFolder = Paths.get("uploads");
    public EmojiStorageService(){
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException exception){
            throw  new RuntimeException("Cannot initialize storage",exception);
        }
    }
    // kiểm tra xem có phải file Emoji ko
    private boolean isEmojiFile(MultipartFile file){
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"jpg","jpeg","png","bmp"}).contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
       try{
           if(file.isEmpty()){
               throw  new RuntimeException("failed to store empty file");
           }
           if(!isEmojiFile(file)){
               throw  new RuntimeException("you can only upload emoji file ");
           }
//           BufferedImage bimg = ImageIO.read(new File(file.getName()));
//           int width   = bimg.getWidth();
//           int height  = bimg.getHeight();
//           if(width > 14 && height>14){
//               throw  new RuntimeException("file muse width and height < 512px");
//           }
           String fileExtension =FilenameUtils.getExtension(file.getOriginalFilename());
           String generatedFileName = UUID.randomUUID().toString().replace("-","");
           generatedFileName =  generatedFileName +"."+ fileExtension;
           Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
           if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
               throw  new RuntimeException("cannot store file outside current directory");
           }
           try (InputStream inputStream =file.getInputStream()){
               Files.copy(inputStream,destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
           }
           return generatedFileName;
       } catch (IOException exception){
           throw  new RuntimeException("failed to store file", exception);
       }
    }

}
