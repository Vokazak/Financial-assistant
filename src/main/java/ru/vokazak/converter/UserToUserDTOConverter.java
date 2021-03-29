package ru.vokazak.converter;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.User;
import ru.vokazak.service.UserDTO;

@Service
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setEmail(source.getEmail());
        userDTO.setName(source.getName());

        return userDTO;
    }
}
