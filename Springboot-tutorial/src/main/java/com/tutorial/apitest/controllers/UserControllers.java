package com.tutorial.apitest.controllers;

import com.tutorial.apitest.models.ResponseObject;
import com.tutorial.apitest.models.User;
import com.tutorial.apitest.repositories.UserRepository;
import com.tutorial.apitest.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.*;

@RestController
@RequestMapping(path = "api/User")
public class UserControllers {

    @Autowired
    private UserRepository repository;
    @GetMapping("")
    List<User> getAllUser(){
        return repository.findAll();
    }
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<User> foundUser = repository.findById(id);
        return foundUser.isPresent()?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok","Query User successfully",foundUser)
                ):
                 ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed","cannot find user with id = "+ id,"")
                );
    }
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertUser(@RequestBody User newUser){
        List<User> foundUser = repository.findByEmail(newUser.getEmail().trim());

            if (foundUser.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "User email already taken", "")
                );
            }
            Pattern password= Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
            Matcher m =password.matcher(newUser.getPassword());
            if(!m.matches()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "your password there must be at least one lowercase letter, one uppercase letter, one number, one special character each", "")
                );
            }
        char toCheck = '.';
        int count = 0;
        for (char ch: newUser.getEmail().toCharArray()) {
            if (ch == toCheck) {
                count++;
            }
        }
        if(count>1){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Your email is in the wrong format", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert user successfully", repository.save(newUser))
            );

    }
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateUser(@RequestBody User newUser, @PathVariable Long id){
        User updateUser =repository.findById(id).map(user -> {
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            user.setAddress(newUser.getAddress());
            user.setEmoji(newUser.getEmoji());
            user.setName(newUser.getName());
            user.setNumberPhone(newUser.getNumberPhone());
            return  repository.save(user);
        }).orElseGet(() -> {
            newUser.setId(id);
            return repository.save(newUser);
        });
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update User successfully", updateUser)
        );
    }
    @DeleteMapping("{id}")
    ResponseEntity<ResponseObject> deleteUser( @PathVariable Long id){
        Boolean exitUser =repository.existsById(id);
        if(exitUser) {
            repository.deleteById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","delete User successfully","")
            );
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed","cannot find User to delete","")
        );
    }
}
