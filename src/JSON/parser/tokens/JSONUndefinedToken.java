package JSON.parser.tokens;

import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenStatus.UNEXPECTED_TOKEN;
import static JSON.parser.tokens.JSONTokenType.UNDEFINED;

public class JSONUndefinedToken extends JSONToken {
    public JSONUndefinedToken(Source source) throws Exception {
        super(source);
        type = UNDEFINED;
        status = UNEXPECTED_TOKEN;
    }

    public JSONUndefinedToken(JSONToken token) throws Exception {
        this(token.getSource());
        text = token.getText();
        line = token.getText();
        lineNum = token.getLineNumber();
        position = token.getPosition();
    }

    // override only, do nothing
    protected void extract() throws Exception{
        text = currentWord();
        consumeCurrentWord();
        value = null;
    }
}
