package ru.vokazak.CommandAnalyzer;

public class UnexpectedWordException extends Exception {

    UnexpectedWordException(String message) {
        super(message);
    }
}
