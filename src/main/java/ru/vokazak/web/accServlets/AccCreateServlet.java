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

import static ru.vokazak.view.RequestHandler.parseBalance;

public class AccCreateServlet extends HttpServlet {

    private final AccService accService;

    public AccCreateServlet() {
        this.accService = SpringContext.getContext().getBean(AccService.class);
    }

    //http://localhost:8080/createAcc?name=WebTestAcc&money=123.4
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            UserDTO user = AuthorizationCheck.getAuthorizedUser(req, resp);
            if (user == null) {
                throw new UnsuccessfulCommandExecutionExc("Access denied!");
            }
            writer.write("Current user: " + user + "\n");

            String accountName = req.getParameter("name");
            String moneyValue = req.getParameter("money");

            AccountDTO accountDTO = accService.create(
                    accountName,
                    parseBalance(moneyValue),
                    user.getId()
            );
            writer.write(accountDTO.toString());

        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }
    }
}
