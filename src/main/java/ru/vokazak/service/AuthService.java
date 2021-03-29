package ru.vokazak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.UserDao;
import ru.vokazak.entity.User;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;
    private final DigestService digestService;
    private final Converter<User, UserDTO> userDTOConverter;

    public UserDTO getUserById(long userId) {
        User user = userDao.findById(userId);

        if (user == null) {
            throw new UnsuccessfulCommandExecutionExc("User with id=" + userId + " was not found");
        }

        return userDTOConverter.convert(user);
    }

    public UserDTO auth(String email, String password) {
        String hash = digestService.hex(password);

        User user = userDao.findByEmailAndHash(email, hash);
        if (user == null) {
            throw new UnsuccessfulCommandExecutionExc("Login error in AuthService");
        }

        return userDTOConverter.convert(user);
    }

    public UserDTO register(String email, String password, String name, String surname) {
        String hash = digestService.hex(password);

        User user = userDao.insert(name, surname, email, hash);
        if (user == null) {
            throw new UnsuccessfulCommandExecutionExc("Registration error in AuthService");
        }

        return userDTOConverter.convert(user);
    }

}
