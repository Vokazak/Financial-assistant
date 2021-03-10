package ru.vokazak.converter;

import ru.vokazak.dao.TransModel;
import ru.vokazak.service.TransDTO;

public class TransModelToTransDTOConverter implements Converter<TransModel, TransDTO> {
    @Override
    public TransDTO convert(TransModel source) {
        TransDTO transDTO = new TransDTO();
        transDTO.setDate(source.getDate());
        transDTO.setDescription(source.getDescription());
        transDTO.setMoney(source.getMoney());
        transDTO.setId(source.getId());

        return transDTO;
    }
}
