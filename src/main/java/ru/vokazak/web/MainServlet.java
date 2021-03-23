package ru.vokazak.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import ru.vokazak.SpringContext;
import ru.vokazak.controller.AuthController;
import ru.vokazak.controller.Controller;
import ru.vokazak.controller.RegisterController;
import ru.vokazak.controller.SecureController;
import ru.vokazak.controller.categoryControllers.CategoryCreateController;
import ru.vokazak.json.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {

    private final ObjectMapper om = new ObjectMapper();
    private final Map<String, Controller> controllers = new HashMap<>();
    private final Map<String, SecureController> secureControllers = new HashMap<>();

    public MainServlet() {
        ApplicationContext context = SpringContext.getContext();

        for (String beanName : context.getBeanNamesForType(Controller.class)) {
            controllers.put(
                    beanName,
                    context.getBean(beanName, Controller.class)
            );
        }

        for (String beanName : context.getBeanNamesForType(SecureController.class)) {
            secureControllers.put(
                    beanName,
                    context.getBean(beanName, SecureController.class)
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String uri = req.getRequestURI();
        res.setContentType("application/json"); //for postman

        try {
            Controller controller = controllers.get(uri);

            if (controller != null) {

                if (controller instanceof AuthController) {
                    AuthController authController = (AuthController) controller;

                    AuthRequest authRequest = om.readValue(
                            req.getInputStream(),
                            authController.getRequestClass()
                    );

                    AuthResponse authResponse = authController.handle(authRequest);

                    HttpSession session = req.getSession();
                    session.setAttribute("userId", authResponse.getId());
                    om.writeValue(res.getWriter(), authResponse);

                } else if (controller instanceof RegisterController) {

                    RegisterController registerController = (RegisterController) controller;

                    RegisterRequest registerRequest = om.readValue(
                            req.getInputStream(),
                            registerController.getRequestClass()
                    );

                    RegisterResponse registerResponse = registerController.handle(registerRequest);

                    HttpSession session = req.getSession();
                    session.setAttribute("userId", registerResponse.getId());
                    om.writeValue(res.getWriter(), registerResponse);

                } else {
                    om.writeValue(
                            res.getWriter(),
                            controller.handle(
                                    om.readValue(
                                            req.getInputStream(),
                                            controller.getRequestClass()
                                    )
                            )
                    );
                }
            } else {
                SecureController secureController = secureControllers.get(uri);

                if (secureController != null) {
                    HttpSession session = req.getSession();
                    Long userId = (Long) session.getAttribute("userId");

                    if (userId == null) {
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        om.writeValue(
                                res.getWriter(),
                                secureController.handle(
                                        om.readValue(
                                                req.getInputStream(),
                                                secureController.getRequestClass()
                                        ),
                                        userId
                                )
                        );
                    }

                } else {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND); //404
                }
            }
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        }

    }
}
