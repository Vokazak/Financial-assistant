package ru.vokazak.web;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.CategoryService;
import ru.vokazak.service.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;

public class StatsServlet extends HttpServlet {

    private final CategoryService categoryService;

    public StatsServlet() {
        categoryService = SpringContext.getContext().getBean(CategoryService.class);
    }

    //http://localhost:8080/stats?days=60
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            UserDTO user = AuthorizationCheck.getAuthorizedUser(req, resp);
            if (user == null) {
                throw new UnsuccessfulCommandExecutionExc("Access denied!");
            }
            writer.write("Current user: " + user + "\n");

            int days;

            try {
                days = Integer.parseInt(req.getParameter("days"));
            } catch (NumberFormatException e) {
                throw new UnsuccessfulCommandExecutionExc(e);
            }

            Map<CategoryDTO, BigDecimal> categoryStats = categoryService.getMoneySpentForEachTransType(user.getId(), days);
            categoryStats.entrySet().forEach(c -> writer.write(c.toString() + "\n"));

        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }
    }
}
