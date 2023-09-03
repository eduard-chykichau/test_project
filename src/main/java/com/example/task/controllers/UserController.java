package com.example.task.controllers;

import com.example.task.dao.UserDao;
import com.example.task.dto.EmailDTO;
import com.example.task.dto.PhoneDTO;
import com.example.task.dto.ResponseError;
import com.example.task.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserDao userDao;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userDao.getUsers();
    }


    @GetMapping("/users/contacts/{userId}")
    public UserDTO getUserContacts(@PathVariable Long userId) {
        return userDao.getContacts(userId);
    }

    @GetMapping(value = "/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userDao.getUserById(id);
    }
    @GetMapping(value = "/users/emails/{userId}")
    public UserDTO getUserEmails(@PathVariable Long userId) {
        return userDao.getEmails(userId);
    }

    @GetMapping(value = "/users/phones/{userId}")
    public UserDTO getUserPhones(@PathVariable Long userId) {
        return userDao.getPhones(userId);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDTO newAccount) {
        UserDTO saved = userDao.saveUser(newAccount);
        return entityWithLocation(saved.getId());
    }

    @PostMapping(value = "/users/phone")
    public ResponseEntity<Void> addPhone(@RequestBody PhoneDTO newPhone) {
        PhoneDTO saved = userDao.savePhone(newPhone.getUserId(), newPhone);
        return entityWithLocation(saved.getId());
    }

    @PostMapping(value = "/users/email")
    public ResponseEntity<Void> addEmail(@RequestBody EmailDTO newEmail) {
        EmailDTO saved = userDao.saveEmail(newEmail.getUserId(), newEmail);
        return entityWithLocation(saved.getId());
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userDao.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> genericException(Exception e) {
        return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Void> entityWithLocation(Object resourceId) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{resourceId}")
                .buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
