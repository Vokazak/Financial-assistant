package ru.vokazak.service;

import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.UserDao;
import ru.vokazak.dao.UserModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

@Service
public class AuthService {

    private final UserDao userDao;
    private final DigestService digestService;
    private final Converter<UserModel, UserDTO> userDTOConverter;

    public AuthService(UserDao userDao, DigestService digestService, Converter<UserModel, UserDTO> userDTOConverter) {
        this.userDao = userDao;
        this.digestService = digestService;
        this.userDTOConverter = userDTOConverter;
    }

    public UserDTO auth(String email, String password) {
        String hash = digestService.hex(password);

        UserModel userModel = userDao.findByEmailAndHash(email, hash);
        if (userModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Login error in AuthService");
        }

        return userDTOConverter.convert(userModel);
    }

    public UserDTO register(String email, String password, String name, String surname) {
        String hash = digestService.hex(password);

        UserModel userModel = userDao.insert(name, surname, email, hash);
        if (userModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Registration error in AuthService");
        }

        return userDTOConverter.convert(userModel);
    }

}
