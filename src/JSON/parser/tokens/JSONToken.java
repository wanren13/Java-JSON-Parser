package JSON.parser.tokens;

import JSON.JSONObj;
import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenStatus.GOOD_TOKEN;
import static JSON.parser.tokens.JSONTokenStatus.UNEXPECTED_TOKEN;

public class JSONToken
{
    protected JSONTokenType type;  // language-specific token type
    protected JSONTokenStatus status; // token status
    protected String text;     // token text
    protected Source source;   // source
    protected JSONObj value;    // token value
    protected String line;      // source line
    protected int lineNum;     // line number of the token's source line
    protected int position;    // position of the first token character

    public JSONToken(Source source) throws Exception {
        status = GOOD_TOKEN;
        value = null;
        this.source = source;
        line = source.getLine();
        lineNum = source.getLineNum();
        position = source.getPosition();
        extract();
    }

    public JSONTokenType getType() {
        return type;
    }

    public JSONTokenStatus getStatus() {
        return status;
    }

    public void setUnexpected() {
        setStatus(UNEXPECTED_TOKEN);
    }

    public void resetStatus() {
        setStatus(GOOD_TOKEN);
    }

    public void setStatus (JSONTokenStatus status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public JSONObj getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNum;
    }

    public int getPosition() {
        return position;
    }

    public String getLine() {
        return line;
    }

    public Source getSource() {
        return source;
    }

    protected void extract() throws Exception {
        text = Character.toString(currentChar());
        value = null;

        nextChar();  // consume current character
    }

    protected char currentChar() throws Exception {
        return source.currentChar();
    }

    protected char nextChar() throws Exception {
        return source.nextChar();
    }

    protected String currentWord() throws Exception {
        return source.currentWord();
    }

    protected void consumeCurrentWord() throws Exception {
        source.consumeCurrentWord();
    }


    protected void checkEscape(StringBuilder textBuffer,
                               StringBuilder valueBuffer) throws Exception {
        char currentChar = currentChar();

        switch(currentChar) {

            //\n = add newline to value buffer
            case 'n':
                textBuffer.append(currentChar);
                valueBuffer.append('\n');
                nextChar();		//consume
                break;

            //\' = add apostrophe to value buffer
            case '\'':
                textBuffer.append(currentChar);
                valueBuffer.append('\'');
                nextChar();		//consume
                break;

            //\" = add quote to value buffer
            case '"':
                textBuffer.append(currentChar);
                valueBuffer.append('"');
                nextChar();		//consume
                break;

            //\\ = add backslash to value buffer
            case '\\':
                textBuffer.append(currentChar);
                valueBuffer.append('\\');
                nextChar();		//consume
                break;

            //\0 = add null character to value buffer
            case '0':
                textBuffer.append(currentChar);
                valueBuffer.append('\0');
                nextChar();		//consume
                break;


            default:
                //add char to value buffer
                valueBuffer.append(currentChar);
                break;
        }
    }
}
