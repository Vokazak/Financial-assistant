package ru.vokazak.exception;

public class UnsuccessfulCommandExecutionExc extends RuntimeException {

    public UnsuccessfulCommandExecutionExc(Throwable e) {
        super(e);
    }

    public UnsuccessfulCommandExecutionExc(String message) {
        super(message);
    }

    public UnsuccessfulCommandExecutionExc(String message, Throwable cause) {
        super(message, cause);
    }
}
