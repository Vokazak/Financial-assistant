package ru.vokazak.web.transTypeServlets;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TransTypeCreateServlet extends HttpServlet {

    private final CategoryService categoryService;

    public TransTypeCreateServlet() {
        categoryService = SpringContext.getContext().getBean(CategoryService.class);
    }

    //http://localhost:8080/createCategory?name=WebTestCategoty
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            CategoryDTO categoryDTO = categoryService.create(req.getParameter("name"));
            writer.write(categoryDTO.toString());

        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }
    }
}
