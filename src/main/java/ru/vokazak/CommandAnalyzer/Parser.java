package ru.vokazak.CommandAnalyzer;

import java.util.List;

public class Parser {

    public OperationType parse(List<Token> tokens) {
        switch (tokens.get(0).getLexeme()) {
            case CMD_CREATE_ACC:
                checkArgs(tokens.subList(1, tokens.size()), Lexemes.ARG_STRING, Lexemes.ARG_NUM);
                return OperationType.CREATE_ACC;

            case CMD_DELETE_ACC:
                checkArgs(tokens.subList(1, tokens.size()), Lexemes.ARG_STRING);
                return OperationType.DELETE_ACC;

            case CMD_GET_ACCS:
                if (tokens.size() > 1) {
                    throw new IllegalArgumentException("\"show_acc_list\" can't have arguments");
                }
                return OperationType.GET_ACCS;

            case CMD_LOGIN:
                checkArgs(tokens.subList(1, tokens.size()), Lexemes.ARG_EMAIL, Lexemes.ARG_STRING);
                return OperationType.LOGIN;

            case CMD_REGISTER:
                checkArgs(tokens.subList(1, tokens.size()), Lexemes.ARG_EMAIL, Lexemes.ARG_STRING, Lexemes.ARG_NAME, Lexemes.ARG_NAME);
                return OperationType.REGISTER;

            case CMD_DISCONNECT:
                if (tokens.size() > 1) {
                    throw new IllegalArgumentException("\"disconnect\" can't have arguments");
                }
                return OperationType.DISCONNECT;

            default:
                throw new IllegalArgumentException("Arguments without a command");
        }
    }

    private void checkArgs(List<Token> args, Lexemes ... expected) {
        if (args.size() != expected.length) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != args.get(i).getLexeme()) {
                throw new IllegalArgumentException("Wrong " + i + " argument, expected " + expected[i].name());
            }
        }

    }

}



