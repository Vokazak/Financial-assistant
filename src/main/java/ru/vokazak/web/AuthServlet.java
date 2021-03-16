package ru.vokazak.web;

import org.springframework.context.ApplicationContext;
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

public class AuthServlet extends HttpServlet {

    private final AuthService authService;

    public AuthServlet() {
        this.authService = SpringContext.getContext().getBean(AuthService.class);
    }

    //http://localhost:8080/login?login=artvas@gmail.com&password=12o34p
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        PrintWriter writer = resp.getWriter();

        try {
            UserDTO userDTO = authService.auth(login, password);
            writer.write(userDTO.toString());

            HttpSession session = req.getSession();
            session.setAttribute("userId", userDTO.getId());
        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
        }
    }
}
