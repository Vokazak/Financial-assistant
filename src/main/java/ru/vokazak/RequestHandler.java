package ru.vokazak;

import ru.vokazak.CommandAnalyzer.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class RequestHandler {

    private final Scanner scanner;
    private final Lexer lexer;
    private final Parser parser;
    private final Executor executor;

    RequestHandler() {
        scanner = new Scanner(System.in);
        lexer = new Lexer();
        parser = new Parser();
        executor = new Executor();
    }

    public boolean processNewRequest() throws UnsuccessfulCommandExecutionExc {
        try {
            List<Token> tokens = lexer.lexLine(scanner.nextLine());
            OperationType ot = parser.parse(tokens);

            switch (ot) {
                case LOGIN:
                    executor.login(tokens.get(1).value(), tokens.get(2).value());
                    return true;

                case REGISTER:
                    executor.register(tokens.get(1).value(), tokens.get(2).value(), tokens.get(3).value(), tokens.get(4).value());
                    return true;

                case CREATE_ACC:
                    executor.createAcc(tokens.get(1).value(), new BigDecimal(tokens.get(2).value()));
                    return true;

                case DELETE_ACC:
                    executor.deleteAcc(tokens.get(1).value());
                    return true;

                case GET_ACCS:
                    executor.listAccs();
                    return true;

                case DISCONNECT:
                    executor.closeConnection();
                    return false;
            }
        } catch (UnexpectedWordException | IllegalArgumentException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
        return false;
    }

}

class UnsuccessfulCommandExecutionExc extends Exception {

    UnsuccessfulCommandExecutionExc(Throwable e) {
        super(e);
    }
    UnsuccessfulCommandExecutionExc(String message) {
        super(message);
    }

    public UnsuccessfulCommandExecutionExc(String message, Throwable cause) {
        super(message, cause);
    }
}
