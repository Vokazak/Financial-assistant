package ru.vokazak.web;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.vokazak.view.RequestHandler.*;

public class TransServlet extends HttpServlet {

    private final TransService transService;

    public TransServlet() {
        this.transService = SpringContext.getContext().getBean(TransService.class);
    }

    //http://localhost:8080/trans?name=WebTestTransaction&accFrom=AccForSalary&accTo=AccForEverydayTrans&category=TransferBetweenOwnAccs&money=1234
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();

        try {
            UserDTO user = AuthorizationCheck.getAuthorizedUser(req, resp);
            if (user == null) {
                throw new UnsuccessfulCommandExecutionExc("Access denied!");
            }
            writer.write("Current user: " + user + "\n");

            String transactionName = req.getParameter("name");
            String accFromName = req.getParameter("accFrom");
            String accToName = req.getParameter("accTo");
            String categoryName = req.getParameter("category");
            String moneyValue = req.getParameter("money");

            checkAccNamesForTransaction(accFromName, accToName);

            TransDTO transDTO = transService.createTransaction(
                    transactionName,
                    getAccount(user.getId(), accFromName),
                    getAccount(user.getId(), accToName),
                    getCategory(categoryName),
                    parseBalance(moneyValue)
            );

            writer.write(transDTO.toString());
        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }

    }


}
