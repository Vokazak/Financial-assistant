package ru.vokazak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.vokazak.converter.UserToUserDTOConverter;
import ru.vokazak.dao.UserDao;
import ru.vokazak.entity.User;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks AuthService subj;

    @Mock UserDao userDao;
    @Mock DigestService digestService;
    @Mock
    UserToUserDTOConverter userDTOConverter;

    @Test
    public void auth_UserNotFound() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        when(userDao.findByEmailAndHash("artvas@gmail.com", "hex"))
                .thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.auth("artvas@gmail.com", "password");
        });


        //these methods must execute 1 time
        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("artvas@gmail.com", "hex");

        //no actions with this mock
        verifyZeroInteractions(userDTOConverter);
    }

    @Test
    public void auth_UserFound() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        User userModel = new User();
        userModel.setId(1L);
        userModel.setEmail("artvas@gmail.com");
        userModel.setPassword("hex");
        when(userDao.findByEmailAndHash("artvas@gmail.com", "hex"))
                .thenReturn(userModel);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("artvas@gmail.com");
        when(userDTOConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.auth("artvas@gmail.com", "password");

        assertNotNull(user);
        assertEquals(userDTO, user);

        //these methods must execute 1 time
        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).findByEmailAndHash("artvas@gmail.com", "hex");
        verify(userDTOConverter, times(1)).convert(userModel);

    }

    @Test
    public void register_successful() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        User userModel = new User();
        userModel.setId(1L);
        userModel.setEmail("artvas@gmail.com");
        userModel.setPassword("hex");
        userModel.setName("Artyom");
        userModel.setSurname("Vasiliev");
        when(userDao.insert("Artyom", "Vasiliev", "artvas@gmail.com", "hex"))
                .thenReturn(userModel);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("artvas@gmail.com");
        userDTO.setName("Artyom");
        when(userDTOConverter.convert(userModel)).thenReturn(userDTO);

        UserDTO user = subj.register("artvas@gmail.com", "password", "Artyom", "Vasiliev");
        assertNotNull(user);
        assertEquals(userDTO, user);

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).insert("Artyom", "Vasiliev", "artvas@gmail.com", "hex");
        verify(userDTOConverter, times(1)).convert(userModel);
    }

    @Test
    public void register_unsuccessful() {
        when(digestService.hex("password"))
                .thenReturn("hex");

        when(userDao.insert("Artyom", "Vasiliev", "artvas@gmail.com", "hex"))
                .thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.register("artvas@gmail.com", "password", "Artyom", "Vasiliev");
        });

        verify(digestService, times(1)).hex("password");
        verify(userDao, times(1)).insert("Artyom", "Vasiliev", "artvas@gmail.com", "hex");

        verifyZeroInteractions(userDTOConverter);
    }
}