package ru.vokazak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.AccountDao;
import ru.vokazak.dao.UserDao;
import ru.vokazak.entity.Account;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.view.RequestHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccService {

    private final AccountDao accountDao;
    private final Converter<Account, AccountDTO> accDTOConverter;

    public AccountDTO create(String name, BigDecimal balance, long userId) {

        Account account = accountDao.insert(name, balance, userId);
        if (account == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in AuthService while creating account");
        }

        return accDTOConverter.convert(account);
    }

    public AccountDTO delete(String name, long userId) {

        Account accToDelete = accountDao.delete(name, userId);
        if (accToDelete == null) {
            throw new UnsuccessfulCommandExecutionExc("Error while deleting account");
        }

        return accDTOConverter.convert(accToDelete);
    }

    public AccountDTO modify(String name, BigDecimal balance, long userId) {

        AccountDTO accToModify = RequestHandler.getAccount(userId, name);
        if (accToModify == null) {
            throw new UnsuccessfulCommandExecutionExc("Error while deleting account");
        }

        accountDao.update(accToModify.getId(), balance);

        return accToModify;
    }

    public List<AccountDTO> getAccList(long userId) {

        List<Account> accountModelList = accountDao.findByUserId(userId);
        if (accountModelList == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in AuthService while listing accounts");
        }

        List<AccountDTO> accList = new ArrayList<>();
        accountModelList.forEach(
                acc -> accList.add(accDTOConverter.convert(acc))
        );

        return accList;
    }
}
