package ru.vokazak.CommandAnalyzer;

public class Token {

    private final Lexemes lexeme;
    private final String value;

    public Token(Lexemes lexeme, String value) {
        this.lexeme = lexeme;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public Lexemes getLexeme() {
        return lexeme;
    }

}
