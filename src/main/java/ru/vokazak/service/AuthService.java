package ru.vokazak.service;

import ru.vokazak.converter.UserModelToUserDTOConverter;
import ru.vokazak.dao.UserDao;
import ru.vokazak.dao.UserModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

public class AuthService {

    private final UserDao userDao;
    private final DigestService digestService;
    private final UserModelToUserDTOConverter userDTOConverter;

    public AuthService(UserDao userDao, DigestService digestService, UserModelToUserDTOConverter userDTOConverter) {
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
