package ru.vokazak.service;

import ru.vokazak.converter.Converter;
import ru.vokazak.dao.AccountDao;
import ru.vokazak.dao.AccountModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccService {

    private final AccountDao accountDao;
    private final Converter<AccountModel, AccountDTO> accDTOConverter;

    public AccService(AccountDao accountDao, Converter<AccountModel, AccountDTO> accDTOConverter) {
        this.accountDao = accountDao;
        this.accDTOConverter = accDTOConverter;
    }

    public AccountDTO create(String name, BigDecimal balance, long userId) {

        AccountModel accountModel = accountDao.insert(name, balance, userId);
        if (accountModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in AuthService while creating account");
        }

        return accDTOConverter.convert(accountModel);
    }

    public AccountDTO delete(String name, long userId) {

        AccountModel accToDelete = accountDao.delete(name, userId);
        if (accToDelete == null) {
            throw new UnsuccessfulCommandExecutionExc("Error while deleting account");
        }

        return accDTOConverter.convert(accToDelete);
    }

    public List<AccountDTO> getAccList(long userId) {

        List<AccountModel> accountModelList = accountDao.findByUserId(userId);
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
