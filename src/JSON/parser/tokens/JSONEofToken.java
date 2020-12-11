package JSON.parser.tokens;

import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenType.END_OF_FILE;

public class JSONEofToken extends JSONToken
{
    public JSONEofToken(Source source) throws Exception {
        super(source);
        type = END_OF_FILE;
    }

    protected void extract() {
        text = END_OF_FILE.getText();
    }
}