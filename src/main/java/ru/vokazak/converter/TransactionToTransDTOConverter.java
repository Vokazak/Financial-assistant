package ru.vokazak.converter;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Transaction;
import ru.vokazak.service.TransDTO;

@Service
public class TransactionToTransDTOConverter implements Converter<Transaction, TransDTO> {
    @Override
    public TransDTO convert(Transaction source) {
        TransDTO transDTO = new TransDTO();
        transDTO.setDate(source.getDate());
        transDTO.setDescription(source.getDescription());
        transDTO.setMoney(source.getMoney());
        transDTO.setId(source.getId());

        return transDTO;
    }
}
