package ru.vokazak.web.accServlets;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.AccService;
import ru.vokazak.service.AccountDTO;
import ru.vokazak.service.UserDTO;
import ru.vokazak.web.AuthorizationCheck;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AccListServlet extends HttpServlet {

    private final AccService accService;

    public AccListServlet() {
        this.accService = SpringContext.getContext().getBean(AccService.class);
    }

    //http://localhost:8080/listAcc
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            UserDTO user = AuthorizationCheck.getAuthorizedUser(req, resp);
            if (user == null) {
                throw new UnsuccessfulCommandExecutionExc("Access denied!");
            }
            writer.write("Current user: " + user + "\n");


            List<AccountDTO> list = accService.getAccList(
                    user.getId()
            );
            list.forEach(a -> writer.write(a.toString() + "\n"));

        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }
    }
}
