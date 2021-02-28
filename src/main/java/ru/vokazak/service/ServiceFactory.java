package ru.vokazak.service;

import ru.vokazak.converter.AccModelToAccDTOConverter;
import ru.vokazak.converter.CategoryModelToCategoryDTOConverter;
import ru.vokazak.converter.UserModelToUserDTOConverter;
import ru.vokazak.dao.AccountDao;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.dao.UserDao;

public class ServiceFactory {

    private static AuthService authService;

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

    private static AccService accService;

    public static AccService getAccService() {
        if (accService == null) {
            accService = new AccService(
                    new AccountDao(),
                    new AccModelToAccDTOConverter()
            );
        }
        return accService;
    }

    private static CategoryService categoryService;

    public static CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryService(
                    new CategoryDao(),
                    new CategoryModelToCategoryDTOConverter()
            );
        }
        return categoryService;
    }

    private ServiceFactory() {}
}
