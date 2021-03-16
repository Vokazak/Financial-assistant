package ru.vokazak.web.transTypeServlets;

import ru.vokazak.SpringContext;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TransTypeListServlet extends HttpServlet {

    private final CategoryService categoryService;

    public TransTypeListServlet() {
        categoryService = SpringContext.getContext().getBean(CategoryService.class);
    }

    //http://localhost:8080/listCategory
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            List<CategoryDTO> list = categoryService.getAll();
            list.forEach(c -> writer.write(c.toString() + "\n"));

        } catch (UnsuccessfulCommandExecutionExc e) {
            writer.write(e.getMessage());
        }
    }
}
