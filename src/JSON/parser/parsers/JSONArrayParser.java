package JSON.parser.parsers;

import JSON.JSONObj;
import JSON.parser.tokens.JSONToken;

import java.util.ArrayList;
import java.util.EnumSet;

import static JSON.parser.tokens.JSONTokenType.*;

public class JSONArrayParser extends JSONParser{
    JSONArrayParser(JSONParser parent) {
        super(parent);
    }

    @Override
    public JSONObj parse() throws Exception {
        JSONToken token = nextToken();  //consume [
        ArrayList<JSONObj> arr = new ArrayList<>();
        JSONParser parser = new JSONParser(this);

        while (token.getType() != END_OF_FILE &&
                token.getType() != RIGHT_BRACKET) {
            JSONObj array_element = parser.parse();

            // stop parsing if array element is null
            if (array_element == null) return null;

            token = nextToken();
            if (token.getType() != COMMA && token.getType() != RIGHT_BRACKET) {
                token.setUnexpected();
                jsonErrorHandler.flag(token, EnumSet.of(COMMA, RIGHT_BRACKET));
                // stop parsing if there is an error
                return null;
            }
            // consume comma
            if (token.getType() == COMMA)
                token = nextToken();
            // add array element to array
            arr.add(array_element);
        }

        // consume right bracket
        if (token.getType() != RIGHT_BRACKET) {
            token.setUnexpected();
            jsonErrorHandler.flag(token, EnumSet.of(RIGHT_BRACKET));
            // stop parsing if there is an error
            return null;
        }

        return new JSONObj(arr);
    }
}
