package com.test.user.controller;
import com.test.user.domain.Users;
import com.test.user.service.UserService;
import com.test.user.model.request.UserRequest;
import com.test.user.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> get_all_users(@RequestParam(defaultValue = "5") int max_records, @RequestParam(defaultValue = "0") int offset) {
        try {
            return new ResponseEntity<>(userService.get_all_users(max_records, offset), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> get_user_by_id(@PathVariable("id") Long id) {
        try {
            UserResponse user = userService.get_user_by_id(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Object> create_user(@RequestBody UserRequest request) {
        try {

            UserResponse user = userService.create_user(request);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> update_user(@RequestBody UserRequest request, @PathVariable("id") Long id){
        try{
            UserResponse user = userService.update_user(request, id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/users/{id}/settings")
    public ResponseEntity<Object> update_user_setting(@PathVariable("id") Long id, @RequestBody List<Map<String,String>> data){
        try{
            UserResponse user = userService.update_user_setting(data, id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete_user(@PathVariable("id") Long id){
        try{
            Users user = userService.delete_user( id);
            if( null == user ){
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/users/{id}/refresh")
    public ResponseEntity<Object> refresh_user(@PathVariable("id") Long id){
        try{
            UserResponse user = userService.refresh_user(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
