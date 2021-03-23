package ru.vokazak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.json.RegisterRequest;
import ru.vokazak.json.RegisterResponse;
import ru.vokazak.service.AuthService;
import ru.vokazak.service.UserDTO;

@Service("/register")
@RequiredArgsConstructor
public class RegisterController implements Controller<RegisterRequest, RegisterResponse>{

    private final AuthService authService;

    @Override
    public RegisterResponse handle(RegisterRequest request) {
        UserDTO userDTO = authService.register(request.getEmail(), request.getPassword(), request.getName(), request.getSurname());

        if (userDTO != null) {
            return new RegisterResponse(userDTO.getId(), userDTO.getEmail());
        } else {
            return null;
        }
    }

    @Override
    public Class<RegisterRequest> getRequestClass() {
        return RegisterRequest.class;
    }
}
