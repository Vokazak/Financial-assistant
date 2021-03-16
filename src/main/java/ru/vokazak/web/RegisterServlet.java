package ru.vokazak.web;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.AuthService;
import ru.vokazak.service.UserDTO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {
    private final AuthService authService;

    public RegisterServlet() {
        this.authService = SpringContext.getContext().getBean(AuthService.class);
    }

    //http://localhost:8080/register?email=webtest@mail.ru&password=abc123&name=Web&surname=Test
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        PrintWriter writer = resp.getWriter();

        try {
            UserDTO userDTO = authService.register(email, password, name, surname);
            writer.write(userDTO.toString());

            HttpSession session = req.getSession();
            session.setAttribute("userId", userDTO.getId());
        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write("Unsuccessful registration");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
        }
    }
}
