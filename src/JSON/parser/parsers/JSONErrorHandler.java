package JSON.parser.parsers;

import JSON.parser.tokens.JSONToken;
import JSON.parser.tokens.JSONTokenStatus;
import JSON.parser.tokens.JSONTokenType;

import java.util.EnumSet;

public class JSONErrorHandler {
    private int errorCount = 0;
    public void flag(JSONToken token) {
        // only show the first error
        if (errorCount == 0) {
            errorCount++;
            StringBuilder flagBuffer = errorHead(token);
            System.out.println(flagBuffer.toString());
        }
    }

    public void flag(JSONToken token, EnumSet<JSONTokenType> expectedSet) {
        // only show the first error
        if (errorCount == 0) {
            errorCount++;
            StringBuilder flagBuffer = errorHead(token);
            flagBuffer.append("Expecting ");

            for (JSONTokenType tokenType : expectedSet) {
                flagBuffer.append("'").append(tokenType).append("', ");
            }

            flagBuffer.append("got '").append(token.getType()).append("'.");
            System.out.println(flagBuffer.toString());
        }
    }

    private StringBuilder errorHead(JSONToken token) {
        StringBuilder flagBuffer = new StringBuilder();
        int position = token.getPosition();
        int lineNum = token.getLineNumber() + 1;
        String line = token.getLine();
        JSONTokenStatus status = token.getStatus();

        System.out.println("Error: parse error on line " + lineNum);
        // print empty string instead of "null"
        System.out.println(line == null?"":line);

        // Spaces up to the error position.
        flagBuffer.append(" ".repeat(position));

        // A pointer to the error followed by the error message.
        flagBuffer.append("^\n*** ").append(status.getText()).append(". ");
        return flagBuffer;
    }
}

