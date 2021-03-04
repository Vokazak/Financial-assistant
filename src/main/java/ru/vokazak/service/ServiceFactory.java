package ru.vokazak.service;

import ru.vokazak.converter.ConverterFactory;
import ru.vokazak.dao.DaoFactory;
import ru.vokazak.atomicTransaction.TransRep;

public class ServiceFactory {

    private static AuthService authService;
    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(
                    DaoFactory.getUserDao(),
                    getDigestService(),
                    ConverterFactory.getUserModelUserDTOConverter()
            );
        }
        return authService;
    }

    private static AccService accService;
    public static AccService getAccService() {
        if (accService == null) {
            accService = new AccService(
                    DaoFactory.getAccountDao(),
                    ConverterFactory.getAccountModelAccountDTOConverter()
            );
        }
        return accService;
    }

    private static CategoryService categoryService;
    public static CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryService(
                    DaoFactory.getCategoryDao(),
                    ConverterFactory.getCategoryModelCategoryDTOConverter()
            );
        }
        return categoryService;
    }

    private static DigestService digestService;
    public static DigestService getDigestService() {
        if (digestService == null) {
            digestService = new Md5DigestService();
        }

        return digestService;
    }

    private static TransService transService;
    public static TransService getTransService() {
        if (transService == null) {
            transService = new TransService(
                   new TransRep(
                           DaoFactory.getDataSource(),
                           DaoFactory.getAccountDao(),
                           DaoFactory.getTransDao(),
                           DaoFactory.getTransactionToCategoryDao()
                   ),
                    ConverterFactory.getTransModelTransDTOConverter()
            );

        }

        return transService;
    }

}
