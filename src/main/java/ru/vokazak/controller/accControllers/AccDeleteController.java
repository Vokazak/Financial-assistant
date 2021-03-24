package ru.vokazak.controller.accControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.controller.SecureController;
import ru.vokazak.json.AccountRequest;
import ru.vokazak.json.AccountResponse;
import ru.vokazak.service.AccService;
import ru.vokazak.service.AccountDTO;

import java.util.Collections;

@Service("/account/delete")
@RequiredArgsConstructor
public class AccDeleteController implements SecureController<AccountRequest, AccountResponse> {

    private final AccService accService;

    @Override
    public AccountResponse handle(AccountRequest request, Long userId) {
        AccountDTO accountDTO = accService.delete(
                request.getName(),
                userId
        );

        //if acc == null UnsuccessfulCommandExecExc will be already thrown in .create(...)
        return new AccountResponse(Collections.singletonList(accountDTO));
    }

    @Override
    public Class<AccountRequest> getRequestClass() {
        return AccountRequest.class;
    }

}
