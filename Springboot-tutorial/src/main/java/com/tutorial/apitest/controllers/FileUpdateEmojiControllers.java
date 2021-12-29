package com.tutorial.apitest.controllers;

import com.tutorial.apitest.models.ResponseObject;
import com.tutorial.apitest.repositories.UserRepository;
import com.tutorial.apitest.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/FileUpdate")
public class FileUpdateEmojiControllers {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private UserRepository repository;
    @PutMapping(path="/{id}", consumes = {MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<ResponseObject> updateUser( @RequestParam("file") MultipartFile file,@PathVariable Long id){
        try {
            String generatedFileName = storageService.storeFile(file);
        Optional<Object> updateUser =repository.findById(id).map(user -> {

            user.setEmoji(generatedFileName);

            return  repository.save(user);
        });
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update User successfully", updateUser)
        );
        } catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("ok",exception.getMessage(),""));
        }
    }
}
