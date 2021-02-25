package ru.vokazak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.vokazak.converter.UserModelToUserDTOConverter;
import ru.vokazak.dao.UserDao;
import ru.vokazak.dao.UserModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks AuthService subj;

    @Mock UserDao userDao;
    @Mock DigestService digestService;
    @Mock UserModelToUserDTOConverter userDTOConverter;

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

        UserModel userModel = new UserModel();
        userModel.setId(1);
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
    public void register() {
    }
}