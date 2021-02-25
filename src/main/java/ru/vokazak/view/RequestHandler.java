package ru.vokazak.view;

import ru.vokazak.commandAnalyzer.*;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class RequestHandler {

    private final Scanner scanner;
    private final Lexer lexer;
    private final Parser parser;
    private final AuthService authService;
    private final AccService accService;
    private final CategoryService categoryService;

    private UserDTO currentUser;
    private String request;

    public RequestHandler() {
        scanner = new Scanner(System.in);
        lexer = new Lexer();
        parser = new Parser();

        authService = ServiceFactory.getAuthService();
        accService = new AccService();
        categoryService = new CategoryService();
    }

    public boolean processNewRequest() throws UnsuccessfulCommandExecutionExc {
        try {
            request = scanner.nextLine();
            List<Token> tokens = lexer.lexLine(request);
            OperationType ot = parser.parse(tokens);

            switch (ot) {
                case LOGIN:
                    currentUser = authService.auth(tokens.get(1).value(), tokens.get(2).value());
                    System.out.println("User " + currentUser.getName() + " (id = " + currentUser.getId() + ") successfully logged in");
                    return true;

                case REGISTER:
                    currentUser = authService.register(tokens.get(1).value(), tokens.get(2).value(), tokens.get(3).value(), tokens.get(4).value());
                    System.out.println("User " + currentUser.getName() + " (id = " + currentUser.getId() + ") successfully registered");
                    return true;

                case CREATE_ACC:
                    checkAuthorisation();

                    AccountDTO accountDTO = accService.create(tokens.get(1).value(), new BigDecimal(tokens.get(2).value()), currentUser.getId());
                    System.out.println("Account " + accountDTO.getName() + "\" (id = " + accountDTO.getId() + ") was successfully created");
                    return true;

                case DELETE_ACC:
                    checkAuthorisation();

                    AccountDTO accountDTO1 = accService.delete(tokens.get(1).value(), currentUser.getId());
                    System.out.println(currentUser.getName() + "'s account \"" + tokens.get(1).value() + "\" (id = " + accountDTO1.getId() + ")  was successfully deleted");
                    return true;

                case GET_ACCS:
                    checkAuthorisation();

                    List<AccountDTO> accountList = accService.find(currentUser.getId());
                    System.out.println(currentUser.getName() + "'s accounts:");
                    accountList.forEach(System.out::println);
                    return true;

                case CREATE_TRANS_TYPE:

                    CategoryDTO categoryDTO = categoryService.create(tokens.get(1).value());
                    System.out.println("New transaction type \"" + categoryDTO.getName() + "\" (id = " + categoryDTO.getId() + ") was successfully created");
                    return true;

                case DELETE_TRANS_TYPE:

                    CategoryDTO categoryDTO1 = categoryService.delete(tokens.get(1).value());
                    System.out.println("Transaction type \"" + categoryDTO1.getName() + "\" (id = " + categoryDTO1.getId() + ") was successfully deleted");
                    return true;

                case MODIFY_TRANS_TYPE:

                    CategoryDTO categoryDTO2 = categoryService.modify(tokens.get(1).value(), tokens.get(2).value());
                    System.out.println("Transaction type \"" + tokens.get(1).value()  + "\" (id = " + categoryDTO2.getId()
                            + ") was successfully renamed to " + categoryDTO2.getName());
                    return true;

                case DISCONNECT:
                    System.out.println("Disconnected");
                    return false;
            }
        } catch (UnexpectedWordException | IllegalArgumentException | UnsuccessfulCommandExecutionExc e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while processing \"" + request + "\"", e);
        }
        return false;
    }

    private void checkAuthorisation() {
        if (currentUser == null) {
            throw new UnsuccessfulCommandExecutionExc("You must login/register first");
        }
    }

}

