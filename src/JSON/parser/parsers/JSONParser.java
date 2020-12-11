package JSON.parser.parsers;

import JSON.JSONObj;
import JSON.parser.JSONScanner;
import JSON.parser.tokens.JSONToken;
import JSON.parser.tokens.JSONTokenType;

import java.util.EnumSet;

import static JSON.parser.tokens.JSONTokenStatus.GOOD_TOKEN;
import static JSON.parser.tokens.JSONTokenType.*;

public class JSONParser
{
	protected JSONScanner jsonScanner;  // scanner used with this JSON.parser
	protected JSONErrorHandler jsonErrorHandler;
//			= new JSONErrorHandler();
	private static final EnumSet<JSONTokenType> TOKEN_START_SET =
			SINGLE_VALUE_SET.clone();
	static {
		TOKEN_START_SET.add(LEFT_BRACKET);
		TOKEN_START_SET.add(LEFT_BRACE);
	}

	public JSONParser(JSONScanner scanner) {
		this.jsonScanner = scanner;
	}

	public JSONParser(JSONParser parent) {
		this(parent.getScanner());
		this.jsonErrorHandler = parent.getErrorHandler();
	}

	public JSONScanner getScanner() {
		return jsonScanner;
	}

	public JSONToken currentToken() throws Exception {
		return jsonScanner.currentToken();
	}

	public JSONToken nextToken() throws Exception {
		return jsonScanner.nextToken();
	}

	public JSONObj parse() throws Exception {
		JSONObj obj = null;

		try {
			JSONToken token = currentToken();
			if (SINGLE_VALUE_SET.contains(token.getType())) {
				if (token.getStatus() == GOOD_TOKEN) {
					obj = token.getValue();
				}
				else {
					jsonErrorHandler.flag(token);
				}
			}
			else if (token.getType() == LEFT_BRACKET) {
				JSONArrayParser jsonArrayParser =
						new JSONArrayParser(this);
				obj = jsonArrayParser.parse();
			}
			else if (token.getType() == LEFT_BRACE) {
				JSONObjectParser jsonObjectParser =
						new JSONObjectParser(this);
				obj = jsonObjectParser.parse();
			}
			else {
				// undefined token
				jsonErrorHandler.flag(token, TOKEN_START_SET);
			}
		}
		catch (java.io.IOException ex) {
			// todo
			System.exit(0);
		}
		return obj;
	}

	public void setErrorHandler (JSONErrorHandler jsonErrorHandler) {
		this.jsonErrorHandler = jsonErrorHandler;
	}

	public JSONErrorHandler getErrorHandler () {
		return jsonErrorHandler;
	}
}

