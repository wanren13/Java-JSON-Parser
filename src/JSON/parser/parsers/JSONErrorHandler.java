package JSON.parser.parsers;

import JSON.parser.tokens.JSONToken;
import JSON.parser.tokens.JSONTokenStatus;
import JSON.parser.tokens.JSONTokenType;

import java.util.EnumSet;

public class JSONErrorHandler {
    public void flag(JSONToken token) {
        StringBuilder flagBuffer = errorHead(token);
    }

    public void flag(JSONToken token, EnumSet<JSONTokenType> expectedSet) {
//        [1, {	2: sf,	"3": 3}, 3]
//        ------^
//                Expecting 'STRING', '}', got 'NUMBER'
        StringBuilder flagBuffer = errorHead(token);

        flagBuffer.append("Expecting ");

        for (JSONTokenType tokenType : expectedSet) {
            flagBuffer.append("'" + tokenType + "', ");
        }
        flagBuffer.append("got " + "'" + token.getType() + "'.");

        System.out.println(flagBuffer.toString());

//        System.exit(0);
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

        int spaceCount = position;

        // Spaces up to the error position.
        for (int i = 0; i < spaceCount; ++i) {
            flagBuffer.append(' ');
        }

        // A pointer to the error followed by the error message.
        flagBuffer.append("^\n*** " + status.getText() + ". ");
        return flagBuffer;
    }
}

