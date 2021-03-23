package ru.vokazak.controller.accControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.controller.SecureController;
import ru.vokazak.json.AccountRequest;
import ru.vokazak.json.AccountResponse;
import ru.vokazak.service.AccService;
import ru.vokazak.service.AccountDTO;

import java.util.List;

@Service("/account/list")
@RequiredArgsConstructor
public class AccListController implements SecureController<AccountRequest, AccountResponse> {

    private final AccService accService;

    @Override
    public AccountResponse handle(AccountRequest request, Long userId) {
        List<AccountDTO> accList = accService.getAccList(userId);

        //if acc == null UnsuccessfulCommandExecExc will be already thrown in .create(...)
        return new AccountResponse(accList);
    }

    @Override
    public Class<AccountRequest> getRequestClass() {
        return AccountRequest.class;
    }

}
