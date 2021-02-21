package ru.vokazak.commandAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Token> tokens;

    public Lexer() {
        tokens = new ArrayList<>();
    }

    public List<Token> lexLine(String command) throws UnexpectedWordException {
        tokens.clear();

        String[] words = command.split("[ ]+");
        try {
            for (String word : words) {
                lexWord(word);
            }
        } catch (UnexpectedWordException e) {
            throw new UnexpectedWordException("In line \"" + command + "\": " + e.getMessage());
        }

        return tokens;
    }

    private void lexWord(String word) throws UnexpectedWordException {
        for (Lexemes lexeme: Lexemes.values()) {

            if (lexeme.endOfMatch(word) == word.length()) {
                tokens.add(new Token(lexeme, word));
                return;
            }
        }

        throw new UnexpectedWordException("Unexpected word \"" + word + "\"");

    }

}

