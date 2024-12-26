package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;
import static com.craftinginterpreters.lox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;

    }

    public List<Token> scanTokens() {
        while (!this.isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return this.tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    // found comment
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
            case ' ':
            case '\r':
            case '\t':
                // ignore whitespace
                break;
            case '\n':
                this.line++;
                break;
            case '\"':
                string();
                break;
            default:
                Lox.error(this.line, "Unexpected character (" + c + ")");
                break;
        }
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') this.line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(this.line, "Unterminated string.");
            return;
        }

        // closing "
        advance();

        // trim surrounding quotes
        String value = this.source.substring(this.start+1, this.current-1);
        addToken(STRING, value);
    }

    private char peek() {
        if (this.isAtEnd()) return '\0';
        return this.source.charAt(this.current);
    }

    private boolean match(char expected) {
        if (this.isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        // next character is the expected one - consume it and return true
        this.current++;
        return true;
    }

    private char advance() {
        this.current++;
        return this.source.charAt(this.current-1);
    }

    private void addToken(TokenType type) {
        this.addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(this.start, this.current);
        this.tokens.add(new Token(type, text, literal, this.line));
    }

    private boolean isAtEnd() {
        return this.current >= this.source.length();
    }
}