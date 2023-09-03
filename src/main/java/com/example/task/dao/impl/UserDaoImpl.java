package com.example.task.dao.impl;

import com.example.task.dao.UserDao;
import com.example.task.domain.Email;
import com.example.task.domain.Phone;
import com.example.task.domain.UserData;
import com.example.task.dto.EmailDTO;
import com.example.task.dto.PhoneDTO;
import com.example.task.dto.UserDTO;
import com.example.task.repositories.EmailRepository;
import com.example.task.repositories.PhoneRepository;
import com.example.task.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    public static final String USER_WITH_ID_D_DOESN_T_EXIST = "user with id: %d doesn't exist";
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> UserDTO.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .orElseThrow(() -> new EntityNotFoundException(getError(id)));
    }

    @Override
    public UserDTO saveUser(UserDTO newAccount) {
        UserData userData = new UserData();
        userData.setFirstName(newAccount.getFirstName());
        userData.setLastName(newAccount.getLastName());
        List<EmailDTO> emails = newAccount.getEmails();
        if (Objects.nonNull(emails)) {
            emails.forEach(email -> {
                Email e = new Email();
                e.setEmail(email.getEmail());
                userData.addEmail(e);
            });
        }

        List<PhoneDTO> phones = newAccount.getPhones();
        if (Objects.nonNull(phones)) {
            phones.forEach(phone -> {
                Phone e = new Phone();
                e.setPhone(phone.getPhone());
                userData.addPhone(e);
            });
        }
        UserData saved = userRepository.save(userData);
        return UserDTO.builder()
                .id(saved.getId())
                .build();
    }


    @Override
    @Transactional
    public EmailDTO saveEmail(Long userId, EmailDTO emailDTO) {
        return userRepository.findById(userId).map(user -> {
            Email email = new Email();
            email.setUserData(user);
            email.setEmail(emailDTO.getEmail());
            return EmailDTO.builder()
                    .id(emailRepository.save(email).getId())
                    .build();
        }).orElseThrow(() -> new EntityNotFoundException(getError(userId)));
    }

    @Override
    @Transactional
    public PhoneDTO savePhone(Long userId, PhoneDTO phoneDTO) {
        return userRepository.findById(userId).map(user -> {
            Phone phone = new Phone();
            phone.setUserData(user);
            phone.setPhone(phoneDTO.getPhone());
            return PhoneDTO.builder()
                    .id(phoneRepository.save(phone).getId())
                    .build();
        }).orElseThrow(() -> new EntityNotFoundException(getError(userId)));
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build()).toList();
    }

    @Override
    public UserDTO getContacts(Long userId) {
        Optional<UserData> userWithEmail = userRepository.findUserWithJoinFetchEmail(userId);
        Optional<UserData> userWithPhone = userRepository.findUserWithJoinFetchPhone(userId);

        UserData userData = null;
        if (userWithEmail.isPresent()) {
            userData = userWithEmail.get();
        }
        if (userWithPhone.isPresent()) {
            if (userData != null) {
                userData.setPhones(userWithPhone.get().getPhones());
            } else {
                userData = userWithPhone.get();
            }
        }

        return Optional.ofNullable(userData).map(
                        user -> UserDTO.builder()
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .emails(user.getEmails().stream().map(email -> EmailDTO.builder()
                                                .email(email.getEmail()).build())
                                        .toList())
                                .phones(user.getPhones().stream().map(phone -> PhoneDTO.builder()
                                                .phone(phone.getPhone()).build())
                                        .toList())
                                .build())
                .orElseThrow(() -> new EntityNotFoundException(getError(userId)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void deletePhone(Long phoneId) {
        phoneRepository.deleteById(phoneId);
    }

    @Override
    public void deleteEmail(Long emailId) {
        emailRepository.deleteById(emailId);
    }

    @Override
    public UserDTO getEmails(Long userId) {
        return userRepository.findUserWithJoinFetchEmail(userId)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .emails(user.getEmails().stream().map(email -> EmailDTO.builder()
                                        .email(email.getEmail()).build())
                                .toList())
                        .build())
                .orElseThrow(() -> new EntityNotFoundException(getError(userId)));
    }

    @Override
    public UserDTO getPhones(Long userId) {
        return userRepository.findUserWithJoinFetchPhone(userId)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phones(user.getPhones().stream().map(phone -> PhoneDTO.builder()
                                        .phone(phone.getPhone()).build())
                                .toList())
                        .build())
                .orElseThrow(() -> new EntityNotFoundException(getError(userId)));
    }

    private static String getError(Long userId) {
        return String.format(USER_WITH_ID_D_DOESN_T_EXIST, userId);
    }
}
