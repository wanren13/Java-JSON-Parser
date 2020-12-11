package JSON.parser.tokens;

import JSON.JSONObj;
import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenType.BOOLEAN;

public class JSONBooleanToken extends JSONToken {
    public JSONBooleanToken(Source source) throws Exception {
        super(source);
        type = BOOLEAN;
    }

    protected void extract() {
        text = source.currentWord();
        source.consumeCurrentWord();
        value = new JSONObj(Boolean.valueOf(text));
    }
}
