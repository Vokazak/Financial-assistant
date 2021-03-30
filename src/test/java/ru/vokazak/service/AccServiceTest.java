package ru.vokazak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.vokazak.converter.AccEntityToAccDTOConverter;
import ru.vokazak.dao.AccountDao;
import ru.vokazak.entity.Account;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccServiceTest {

    @InjectMocks AccService subj;

    @Mock AccountDao accountDao;
    @Mock
    AccEntityToAccDTOConverter accDTOConverter;

    @Test
    public void create_successful() {

        Account accountModel = new Account();
        accountModel.setId(1L);
        accountModel.setName("MainAcc");
        accountModel.setBalance(new BigDecimal("123.4"));
        //accountModel.setUserId(2);

        when(accountDao.insert("MainAcc", new BigDecimal("123.4"), 1))
                .thenReturn(accountModel);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setName("MainAcc");
        accountDTO.setBalance(new BigDecimal("123.4"));
        accountDTO.setUserId(2);

        when(accDTOConverter.convert(accountModel))
                .thenReturn(accountDTO);

        AccountDTO acc = subj.create("MainAcc", new BigDecimal("123.4"), 1);

        assertNotNull(acc);
        assertEquals(accountDTO, acc);

        verify(accountDao, times(1)).insert("MainAcc", new BigDecimal("123.4"), 1);
        verify(accDTOConverter, times(1)).convert(accountModel);
    }

    @Test
    public void create_unsuccessful() {
        when(accountDao.insert("MainAcc", new BigDecimal("123.4"), 1))
                .thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.create("MainAcc", new BigDecimal("123.4"), 1);
        });

        verify(accountDao, times(1)).insert("MainAcc", new BigDecimal("123.4"), 1);

        verifyZeroInteractions(accDTOConverter);
    }

    @Test
    public void delete_successful() {
        Account accountModel = new Account();
        accountModel.setId(1L);
        accountModel.setName("MainAcc");
        accountModel.setBalance(new BigDecimal("123.4"));
        //accountModel.setUserId(2);

        when(accountDao.delete("MainAcc",  2))
                .thenReturn(accountModel);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setName("MainAcc");
        accountDTO.setBalance(new BigDecimal("123.4"));
        accountDTO.setUserId(2);

        when(accDTOConverter.convert(accountModel))
                .thenReturn(accountDTO);

        AccountDTO acc = subj.delete("MainAcc",  2);

        assertNotNull(acc);
        assertEquals(accountDTO, acc);

        verify(accountDao, times(1)).delete("MainAcc", 2);
        verify(accDTOConverter, times(1)).convert(accountModel);
    }

    @Test
    public void delete_unsuccessful() {
        when(accountDao.delete("MainAcc", 2))
                .thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.delete("MainAcc", 2);
        });

        verify(accountDao, times(1)).delete("MainAcc", 2);

        verifyZeroInteractions(accDTOConverter);
    }

    @Test
    public void getAccList_successful() {
        Account accountModel1 = new Account();
        accountModel1.setId(1L);
        accountModel1.setName("MainAcc");
        accountModel1.setBalance(new BigDecimal("123.4"));
        //accountModel1.setUserId(2);

        Account accountModel2 = new Account();
        accountModel2.setId(2L);
        accountModel2.setName("SecondAcc");
        accountModel2.setBalance(new BigDecimal("432.1"));
        //accountModel2.setUserId(2);

        List<Account> accountModelList = new ArrayList<>();
        accountModelList.add(accountModel1);
        accountModelList.add(accountModel2);

        when(accountDao.findByUserId(2)).thenReturn(accountModelList);

        AccountDTO accountDTO1 = new AccountDTO();
        accountDTO1.setId(1);
        accountDTO1.setName("MainAcc");
        accountDTO1.setBalance(new BigDecimal("123.4"));
        accountDTO1.setUserId(2);

        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setId(2);
        accountDTO2.setName("SecondAcc");
        accountDTO2.setBalance(new BigDecimal("432.1"));
        accountDTO2.setUserId(2);

        List<AccountDTO> accountDTOList = new ArrayList<>();
        accountDTOList.add(accountDTO1);
        accountDTOList.add(accountDTO2);

        when(accDTOConverter.convert(accountModel1)).thenReturn(accountDTO1);
        when(accDTOConverter.convert(accountModel2)).thenReturn(accountDTO2);

        List<AccountDTO> list = subj.getAccList(2);
        assertNotNull(list);
        assertEquals(list, accountDTOList);

        verify(accountDao, times(1)).findByUserId(2);
        verify(accDTOConverter, times(1)).convert(accountModel1);
        verify(accDTOConverter, times(1)).convert(accountModel2);
    }

    @Test
    public void getAccList_unsuccessful() {
        when(accountDao.findByUserId(2)).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.getAccList(2);
        });

        verify(accountDao, times(1)).findByUserId(2);

        verifyZeroInteractions(accDTOConverter);
    }
}
