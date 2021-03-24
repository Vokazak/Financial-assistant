package ru.vokazak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.json.AuthRequest;
import ru.vokazak.json.AuthResponse;
import ru.vokazak.service.AuthService;
import ru.vokazak.service.UserDTO;

@Service("/login")
@RequiredArgsConstructor
public class AuthController implements Controller<AuthRequest, AuthResponse> {

    private final AuthService authService;

    @Override
    public AuthResponse handle(AuthRequest request) {
        UserDTO userDTO = authService.auth(
                request.getEmail(),
                request.getPassword()
        );

        return new AuthResponse(
                userDTO.getId(),
                userDTO.getEmail()
        );
    }

    @Override
    public Class<AuthRequest> getRequestClass() {
        return AuthRequest.class;
    }

}
