package JSON.parser;

import JSON.parser.tokens.*;

import static JSON.parser.Source.EOF;
import static JSON.parser.tokens.JSONTokenStatus.UNEXPECTED_TOKEN;
import static JSON.parser.tokens.JSONTokenType.SPECIAL_SYMBOLS;

public class JSONScanner {

    private final Source source;     // source
    private JSONToken currentToken;  // current token

    public JSONScanner(Source source) {
        this.source = source;
    }

    public JSONToken currentToken() throws Exception{
        if (currentToken == null)
            nextToken();
        return currentToken;
    }

    public JSONToken nextToken() throws Exception {
        currentToken = extractToken();
        return currentToken;
    }

    public char currentChar() throws Exception {
        return source.currentChar();
    }

    public char nextChar() throws Exception {
        return source.nextChar();
    }

    public String currentWord() {
        return source.currentWord();
    }

    private JSONToken extractToken() throws Exception {
        skipWhiteSpace();

        JSONToken token;
        char currentChar = currentChar();

        // Construct the next token.  The current character determines the
        // token type.
        if (currentChar == EOF) {
            token = new JSONEofToken(source);
        }
        else if (Character.isDigit(currentChar) || currentChar == '-') {
            token = new JSONNumberToken(source);
            if (token.getStatus() == UNEXPECTED_TOKEN) {
                token = new JSONUndefinedToken(token);
            }
        }
        else if (currentChar == '"') {
            token = new JSONStringToken(source);
        }
        else if (SPECIAL_SYMBOLS.containsKey(currentChar + "")) {
            token = new JSONSpecialSymbolToken(source);
        }
        else {
            String word = currentWord();
            switch (word) {
                case "null":
                    token = new JSONNullToken(source);
                    break;
                case "true":
                case "false":
                    token = new JSONBooleanToken(source);
                    break;
                default:
                    token = new JSONUndefinedToken(source);
            }
        }

        return token;
    }

    private void skipWhiteSpace() throws Exception {
        char currentChar = currentChar();
        while ((Character.isWhitespace(currentChar))) {
            currentChar = nextChar();
        }
    }
}
