package JSON.parser.tokens;

import JSON.JSONObj;
import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenType.NULL;

public class JSONNullToken extends JSONToken {
    public JSONNullToken(Source source) throws Exception {
        super(source);
        type = NULL;
    }

    protected void extract() {
        text = source.currentWord();
        source.consumeCurrentWord();
        value = new JSONObj();
    }
}
