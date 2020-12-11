package JSON.parser.tokens;

import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenType.SPECIAL_SYMBOLS;

public class JSONSpecialSymbolToken extends JSONToken {

    public JSONSpecialSymbolToken(Source source) throws Exception {
        super(source);
    }

    protected void extract() throws Exception {
        char currentChar = currentChar();
        nextChar(); // consume current char

        text = Character.toString(currentChar);
        type = SPECIAL_SYMBOLS.get(text);
    }
}
