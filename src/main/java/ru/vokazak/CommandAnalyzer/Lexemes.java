package ru.vokazak.CommandAnalyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Lexemes {

    CMD_REGISTER("register"),
    CMD_LOGIN("login"),
    CMD_GET_ACCS("show_acc_list"),
    CMD_CREATE_ACC("create_acc"),
    CMD_DELETE_ACC("del_acc"),

    CMD_DISCONNECT("disconnect"),

    ARG_EMAIL("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[ " +
            "\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-" +
            "\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
            "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
            "\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
            "\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|" +
            "[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
            "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
            "\\x0c\\x0e-\\x7f])+)\\])"),

    ARG_NAME("[A-Z]{1}[a-z]{1,30}"), //person's name or surname
    ARG_NUM("\\d*\\.?\\d+"), //account balance
    ARG_STRING("[A-Za-z0-9]{1,30}"); //Password or account name

    private final Pattern pattern;

    Lexemes(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }

}


