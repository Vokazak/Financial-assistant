package ru.vokazak.service;

import ru.vokazak.converter.UserModelToUserDTOConverter;
import ru.vokazak.dao.UserDao;

public class ServiceFactory {

    private static AuthService authService;

    private ServiceFactory() {}

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(
                    new UserDao(),
                    new Md5DigestService(),
                    new UserModelToUserDTOConverter()
            );
        }
        return authService;
    }
}
