package ru.vokazak.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.vokazak.service.AccountDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountResponse {
    private List<AccountDTO> accounts;
}
