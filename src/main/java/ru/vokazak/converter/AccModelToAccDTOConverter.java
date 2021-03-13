package ru.vokazak.converter;

import org.springframework.stereotype.Service;
import ru.vokazak.dao.AccountModel;
import ru.vokazak.service.AccountDTO;

@Service
public class AccModelToAccDTOConverter implements Converter<AccountModel, AccountDTO> {

    @Override
    public AccountDTO convert(AccountModel source) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName(source.getName());
        accountDTO.setUserId(source.getUserId());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setId(source.getId());

        return accountDTO;
    }
}
