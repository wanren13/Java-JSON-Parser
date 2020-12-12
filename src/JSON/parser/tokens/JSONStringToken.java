package JSON.parser.tokens;

import JSON.JSONObj;
import JSON.parser.Source;

import static JSON.parser.Source.EOF;
import static JSON.parser.tokens.JSONTokenStatus.MISSING_QUOTE;
import static JSON.parser.tokens.JSONTokenType.STRING;

public class JSONStringToken extends JSONToken {
    public JSONStringToken(Source source) throws Exception {
        super(source);
    }

    protected void extract() throws Exception {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();
        type = STRING;

        char currentChar = nextChar();  // consume initial quote
        textBuffer.append('"');

        // Get string characters.
        do {
            //if we read a backslash, check what the following escaped char is
            if (currentChar == '\\') {
                nextChar();

                //pass escape-sequence handling off to central function
                //entire escape sequence is consumed by checkEscape
                checkEscape(textBuffer, valueBuffer);
            }

            // get new currentChar
            currentChar = currentChar();

            //keep reading until a quote is read or EOF
            if ((currentChar != '"') && (currentChar != EOF)) {
                textBuffer.append(currentChar);
                valueBuffer.append(currentChar);
                currentChar = nextChar();  // consume character
            }
        } while ((currentChar != '"') && (currentChar != EOF));

        //string ended with quote
        if (currentChar == '"') {
            nextChar();  // consume final quote
            textBuffer.append('"');
            value = new JSONObj(valueBuffer.toString());
        }
        //string ended at EOF
        else status = MISSING_QUOTE;

        text = textBuffer.toString();
    }
}
