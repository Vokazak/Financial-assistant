package ru.vokazak.commandAnalyzer;

public class UnexpectedWordException extends Exception {

    UnexpectedWordException(String message) {
        super(message);
    }
}
