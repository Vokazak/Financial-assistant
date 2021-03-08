package ru.vokazak.view;

import ru.vokazak.commandAnalyzer.*;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RequestHandler {

    private final Scanner scanner;
    private final Lexer lexer;
    private final Parser parser;

    private final AuthService authService;
    private final AccService accService;
    private final CategoryService categoryService;
    private final TransService transService;

    private UserDTO currentUser;
    private String request;

    public RequestHandler() {
        scanner = new Scanner(System.in);
        lexer = new Lexer();
        parser = new Parser();

        authService = ServiceFactory.getAuthService();
        accService = ServiceFactory.getAccService();
        categoryService = ServiceFactory.getCategoryService();
        transService = ServiceFactory.getTransService();
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

                    List<AccountDTO> accountList = accService.getAccList(currentUser.getId());
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

                case GET_TRANS_STATS:
                    checkAuthorisation();

                    int days = (int) Double.parseDouble(tokens.get(1).value());
                    Map<CategoryDTO, BigDecimal> categoryStats = categoryService.getMoneySpentForEachTransType(currentUser.getId(), days);

                    System.out.println(currentUser.getName() + "'s transaction stats:");
                    categoryStats.entrySet().forEach(System.out::println);

                    return true;

                case CREATE_TRANS:
                    checkAuthorisation();

                    String accFromName = tokens.get(2).value();
                    String accToName = tokens.get(3).value();

                    //if accFrom == NULL && accTo == NULL
                    if (accFromName.equals(Lexemes.NULL.name()) && accToName.equals(Lexemes.NULL.name())) {
                        throw new UnsuccessfulCommandExecutionExc("No accounts are attached to transaction");
                    }

                    if (accFromName.equals(accToName)) {
                        throw new UnsuccessfulCommandExecutionExc("Transaction has no reason");
                    }

                    AccountDTO accFrom = getAccount(currentUser.getId(), accFromName);
                    AccountDTO accTo = getAccount(currentUser.getId(), accToName);
                    CategoryDTO category = getCategory(tokens.get(4).value());

                    //name, accFrom, accTo, category, description, money
                    TransDTO transaction = transService.createTransaction(
                            tokens.get(1).value(), accFrom, accTo, category, new BigDecimal(tokens.get(5).value()));
                    System.out.println("Transaction " + transaction.getId() + " was successfully created");
                    System.out.println("\t" + transaction);
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

    private CategoryDTO getCategory(String name) {
        List<CategoryDTO> categories = categoryService.getAll();
        for (CategoryDTO c: categories) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        throw new UnsuccessfulCommandExecutionExc("Category \"" + name + "\" does not exist");
    }

    private AccountDTO getAccount(long userId, String name) {
        if (name.equals(Lexemes.NULL.name())) {
            return null;
        }

        List<AccountDTO> accounts = accService.getAccList(userId);
        for (AccountDTO a: accounts) {
            if (a.getName().equals(name)) {
                return a;
            }
        }

        throw new UnsuccessfulCommandExecutionExc(currentUser.getName() + "'s account \"" + name + "\" does not exist");

    }

}

