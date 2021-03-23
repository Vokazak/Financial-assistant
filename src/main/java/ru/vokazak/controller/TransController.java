package ru.vokazak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.json.TransRequest;
import ru.vokazak.json.TransResponse;
import ru.vokazak.service.TransDTO;
import ru.vokazak.service.TransService;

import static ru.vokazak.view.RequestHandler.*;
import static ru.vokazak.view.RequestHandler.parseBalance;

@Service("/transaction")
@RequiredArgsConstructor
public class TransController implements SecureController<TransRequest, TransResponse> {

    private final TransService transService;

    @Override
    public TransResponse handle(TransRequest request, Long userId) {

        String transactionName = request.getTransactionName();
        String accFromName = request.getAccFromName();
        String accToName = request.getAccToName();
        String categoryName = request.getCategoryName();
        String moneyValue = request.getMoneyValue();

        checkAccNamesForTransaction(accFromName, accToName);

        TransDTO transDTO = transService.createTransaction(
                transactionName,
                getAccount(userId, accFromName),
                getAccount(userId, accToName),
                getCategory(categoryName),
                parseBalance(moneyValue)
        );

        return new TransResponse(
                transDTO.getId(),
                transDTO.getDate(),
                transDTO.getDescription(),
                transDTO.getMoney()
        );
    }

    @Override
    public Class<TransRequest> getRequestClass() {
        return TransRequest.class;
    }
}
