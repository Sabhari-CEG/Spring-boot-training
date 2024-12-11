package com.sabhari.demo.UI_controller;
import org.springframework.web.bind.annotation.RestController;

import com.sabhari.demo.Model_request.UpdateUserDetails;
import com.sabhari.demo.Model_request.UserDetail;
import com.sabhari.demo.UI_model_response.UserRest;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController()
@RequestMapping("users")
public class UserController {

    Map<String,UserRest> users = new HashMap<>();


    @GetMapping()
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
    @RequestParam(value = "limit", defaultValue = "50") int limit,
    @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort
    ){
        return "Get users was called with page = " + Integer.toString(page) + " and with limit = " + Integer.toString(limit) + " and sort  = " + sort;
    }
    
    @GetMapping(path = "/{userId}", produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE, org.springframework.http.MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){
        if (users.containsKey(userId)) {
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK); 
        } else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
    }

    @PostMapping(
        consumes = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE},
        produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserRest> createUser(@Validated @RequestBody UserDetail userDetail){
        UserRest returnValue = new UserRest();
        returnValue.setFirstName(userDetail.getFirstName());
        returnValue.setLastName(userDetail.getLastName());
        returnValue.setEmail(userDetail.getEmail());

        String uid = UUID.randomUUID().toString();
        returnValue.setUserId(uid);
        users.put(uid, returnValue);
        return new ResponseEntity<UserRest>(returnValue, HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}", consumes = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE},
    produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String userId, @RequestBody UpdateUserDetails userDetail){
        UserRest selectedUser = users.get(userId);
        selectedUser.setFirstName(userDetail.getFirstName());
        selectedUser.setLastName(userDetail.getLastName());
        users.put(userId, selectedUser);
        return selectedUser;
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId){
        users.remove(userId);
        return ResponseEntity.noContent().build();
    }
    
}
