package JSON.parser.parsers;

import JSON.JSONObj;
import JSON.parser.tokens.JSONToken;

import java.util.EnumSet;
import java.util.LinkedHashMap;

import static JSON.parser.tokens.JSONTokenType.*;

public class JSONObjectParser extends JSONParser {
    JSONObjectParser(JSONParser parent) {
        super(parent);
    }

    @Override
    public JSONObj parse() throws Exception {
        JSONToken token = nextToken();  //consume {
        LinkedHashMap<String, JSONObj> map = new LinkedHashMap<>();
        JSONParser parser = new JSONParser(this);

        // to-do
        while (token.getType() != END_OF_FILE &&
                token.getType() != RIGHT_BRACE) {
            if (token.getType() != STRING) {
                token.setUnexpected();
                jsonErrorHandler.flag(token, EnumSet.of(STRING));
                // stop parsing if there is an error
                return null;
            }
            String key = token.getText();
            token = nextToken(); // consume STRING
            if (token.getType() != COLON) {
                token.setUnexpected();
                jsonErrorHandler.flag(token, EnumSet.of(COLON));
                // stop parsing if there is an error
                return null;
            }
            nextToken(); // consume COLON
            JSONObj value = parser.parse();

            // stop parsing if value is null
            if (value == null) return null;

            token = nextToken();
            if (token.getType() != COMMA && token.getType() != RIGHT_BRACE) {
                token.setUnexpected();
                jsonErrorHandler.flag(token, EnumSet.of(COMMA, RIGHT_BRACE));
                // stop parsing if there is an error
                return null;
            }
            // consume comma
            if (token.getType() == COMMA)
                token = nextToken();
            // insert key and value
            map.put(key, value);
        }

        // consume right bracket
        if (token.getType() != RIGHT_BRACE) {
            token.setUnexpected();
            jsonErrorHandler.flag(token, EnumSet.of(RIGHT_BRACE));
            // stop parsing if there is an error
            return null;
        }

        // unexpected EOF
        // missing right brace
//            jsonErrorHandler.flag()

        return new JSONObj(map);
    }
}
