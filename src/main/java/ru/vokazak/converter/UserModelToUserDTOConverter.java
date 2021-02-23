package ru.vokazak.converter;

import ru.vokazak.dao.UserModel;
import ru.vokazak.service.UserDTO;

public class UserModelToUserDTOConverter implements Converter<UserModel, UserDTO> {
    @Override
    public UserDTO convert(UserModel source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setEmail(source.getEmail());
        userDTO.setName(source.getName());

        return userDTO;
    }
}