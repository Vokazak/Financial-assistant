package ru.vokazak.commandAnalyzer;

import java.util.List;

public class Parser {

    public OperationType parse(List<Token> tokens) {
        List<Token> args = tokens.subList(1, tokens.size());

        switch (tokens.get(0).getLexeme()) {
            case CMD_CREATE_ACC:
                checkArgs(args, Lexemes.ARG_STRING, Lexemes.ARG_NUM);
                return OperationType.CREATE_ACC;

            case CMD_DELETE_ACC:
                checkArgs(args, Lexemes.ARG_STRING);
                return OperationType.DELETE_ACC;

            case CMD_GET_ACCS:
                if (tokens.size() > 1) {
                    throw new IllegalArgumentException("\"show_acc_list\" can't have arguments");
                }
                return OperationType.GET_ACCS;

            case CMD_LOGIN:
                checkArgs(args, Lexemes.ARG_EMAIL, Lexemes.ARG_STRING);
                return OperationType.LOGIN;

            case CMD_REGISTER:
                checkArgs(args, Lexemes.ARG_EMAIL, Lexemes.ARG_STRING, Lexemes.ARG_NAME, Lexemes.ARG_NAME);
                return OperationType.REGISTER;

            case CMD_CREATE_TRANS_TYPE:
                checkArgs(args, Lexemes.ARG_NAME);
                return OperationType.CREATE_TRANS_TYPE;

            case CMD_DELETE_TRANS_TYPE:
                checkArgs(args, Lexemes.ARG_NAME);
                return OperationType.DELETE_TRANS_TYPE;

            case CMD_MODIFY_TRANS_TYPE:
                checkArgs(args, Lexemes.ARG_NAME, Lexemes.ARG_NAME);
                return OperationType.MODIFY_TRANS_TYPE;

            case CMD_GET_TRANSACTIONS_STATS:
                checkArgs(args, Lexemes.ARG_NUM);
                return OperationType.GET_TRANS_STATS;

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



