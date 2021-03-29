package ru.vokazak.converter;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Account;
import ru.vokazak.service.AccountDTO;

@Service
public class AccEntityToAccDTOConverter implements Converter<Account, AccountDTO> {

    @Override
    public AccountDTO convert(Account source) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName(source.getName());
        accountDTO.setUserId(source.getId());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setId(source.getId());

        return accountDTO;
    }
}
