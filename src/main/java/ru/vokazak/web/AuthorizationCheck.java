package ru.vokazak.web;

import ru.vokazak.SpringContext;
import ru.vokazak.service.AuthService;
import ru.vokazak.service.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthorizationCheck {

    public static UserDTO getAuthorizedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthService authService = SpringContext.getContext().getBean(AuthService.class);

        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");

        PrintWriter writer = resp.getWriter();

        if (userId == null) {
            writer.write("Access denied!");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        } else {
            return authService.getUserById(userId);
        }
    }

}
