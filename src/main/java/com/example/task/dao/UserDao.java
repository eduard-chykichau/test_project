package com.example.task.dao;

import com.example.task.dto.EmailDTO;
import com.example.task.dto.PhoneDTO;
import com.example.task.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao {

    UserDTO getUserById(Long userId);

    @Transactional
    UserDTO saveUser(UserDTO userData);

    @Transactional
    EmailDTO saveEmail(Long userId, EmailDTO email);

    @Transactional
    PhoneDTO savePhone(Long userId, PhoneDTO phone);

    List<UserDTO> getUsers();

    UserDTO getContacts(Long userId);

    void deleteUser(Long userId);

    void deletePhone(Long phoneId);

    void deleteEmail(Long emailId);

    UserDTO getEmails(Long userId);

    UserDTO getPhones(Long userId);
}
