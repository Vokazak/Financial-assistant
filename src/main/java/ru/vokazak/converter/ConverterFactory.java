package ru.vokazak.converter;

import ru.vokazak.dao.AccountModel;
import ru.vokazak.dao.CategoryModel;
import ru.vokazak.dao.UserModel;
import ru.vokazak.service.AccountDTO;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.UserDTO;

public class ConverterFactory {

    private static Converter<UserModel, UserDTO> userModelUserDTOConverter;
    public static Converter<UserModel, UserDTO> getUserModelUserDTOConverter() {
        if (userModelUserDTOConverter == null) {
            userModelUserDTOConverter = new UserModelToUserDTOConverter();
        }

        return userModelUserDTOConverter;
    }

    private static Converter<CategoryModel, CategoryDTO> categoryModelCategoryDTOConverter;
    public static Converter<CategoryModel, CategoryDTO> getCategoryModelCategoryDTOConverter() {
        if (categoryModelCategoryDTOConverter == null) {
            categoryModelCategoryDTOConverter = new CategoryModelToCategoryDTOConverter();
        }

        return categoryModelCategoryDTOConverter;
    }

    private static Converter<AccountModel, AccountDTO> accountModelAccountDTOConverter;
    public static Converter<AccountModel, AccountDTO> getAccountModelAccountDTOConverter() {
        if (accountModelAccountDTOConverter == null) {
            accountModelAccountDTOConverter = new AccModelToAccDTOConverter();
        }

        return accountModelAccountDTOConverter;
    }
}
