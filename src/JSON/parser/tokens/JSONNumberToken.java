package JSON.parser.tokens;

import JSON.JSONObj;
import JSON.parser.Source;

import static JSON.parser.tokens.JSONTokenStatus.*;
import static JSON.parser.tokens.JSONTokenType.INTEGER;
import static JSON.parser.tokens.JSONTokenType.REAL;

public class JSONNumberToken extends JSONToken {
    private static final int MAX_EXPONENT = 37;

    public JSONNumberToken(Source source) throws Exception {
        super(source);
    }

    protected void extract() throws Exception {
        StringBuilder textBuffer = new StringBuilder();  // token's characters
        value = null;
        extractNumber(textBuffer);
        text = textBuffer.toString();
        if (ERROR_STATUS_SET.contains(status)){
            text += currentWord();
            consumeCurrentWord();
        }
    }

    protected void extractNumber(StringBuilder textBuffer) throws Exception {
        String wholeDigits;     // digits before the decimal point
        String fractionDigits = null;  // digits after the decimal point
        String exponentDigits = null;  // exponent digits
        char exponentSign = '+';
        type = INTEGER;  // assume INTEGER token type for now

        // Extract the digits of the whole part of the number.
        wholeDigits = signedIntegerDigits(textBuffer);
        if (ERROR_STATUS_SET.contains(status)) return;

        // Is there a . ?
        char currentChar = currentChar();
        if (currentChar == '.') {
            type = REAL;  // decimal point, so token type is REAL
            textBuffer.append(currentChar);
            nextChar();  // consume decimal point

            // Collect the digits of the fraction part of the number.
            fractionDigits = signedIntegerDigits(textBuffer);
            if (ERROR_STATUS_SET.contains(status)) return;
        }

        // Is there an exponent part?
        // There cannot be an exponent if we already saw a ".." token.
        currentChar = currentChar();
        if (Character.toLowerCase(currentChar) == 'e') {
            type = REAL;  // exponent, so token type is REAL
            textBuffer.append(currentChar);
            currentChar = nextChar();  // consume 'E' or 'e'

            // Exponent sign?
            if ((currentChar == '+') || (currentChar == '-')) {
                textBuffer.append(currentChar);
                exponentSign = currentChar;
                nextChar();  // consume '+' or '-'
            }

            // Extract the digits of the exponent.
            exponentDigits = signedIntegerDigits(textBuffer);
            if (ERROR_STATUS_SET.contains(status)) return;
        }

        // Compute the value of an integer number token.
        if (type == INTEGER) {
            int integerValue = computeIntegerValue(wholeDigits);

            if (!ERROR_STATUS_SET.contains(status)) {
                value = new JSONObj(integerValue);
            }
        }

        // Compute the value of a real number token.
        else if (type == REAL) {
            double realValue = computeFloatValue(wholeDigits, fractionDigits,
                    exponentDigits, exponentSign);

            if (!ERROR_STATUS_SET.contains(status)) {
                value = new JSONObj(realValue);
            }
        }
    }

    private String signedIntegerDigits(StringBuilder textBuffer) throws Exception {
        char currentChar = currentChar();
        boolean isNegative = currentChar == '-';

        StringBuilder digits = new StringBuilder();

        // extract the sign
        if (isNegative) {
            textBuffer.append(currentChar);
            digits.append(currentChar);
            currentChar = nextChar(); // consume negative sign
        }

        while (Character.isWhitespace(currentChar)) {
            currentChar = nextChar();
        }

        // at least one digit
        if (!Character.isDigit(currentChar))
            status = UNEXPECTED_TOKEN;

        // Extract the digits.
        while (Character.isDigit(currentChar)) {
            textBuffer.append(currentChar);
            digits.append(currentChar);
            currentChar = nextChar();  // consume digit
        }

        // invalid number if a number starts with '0'
        if (!isNegative && digits.length() > 1 && digits.charAt(0) == '0')
            status = UNEXPECTED_TOKEN;
        if (isNegative && digits.length() > 2 && digits.charAt(1) == '0')
            status = UNEXPECTED_TOKEN;

        return digits.toString();
    }

    private int computeIntegerValue(String digits) {
        // Return 0 if no digits.
        if (digits == null) {
            return 0;
        }

        int integerValue = 0;
        int prevValue = -1;    // overflow occurred if prevValue > integerValue
        boolean isNegative = digits.charAt(0) == '-';
        int index = isNegative?1:0;

        // Loop over the digits to compute the integer value
        // as long as there is no overflow.
        while ((index < digits.length()) && (integerValue >= prevValue)) {
            prevValue = integerValue;
            integerValue = 10*integerValue +
                    Character.getNumericValue(digits.charAt(index++));
        }

        // No overflow:  Return the integer value.
        if (integerValue >= prevValue) {
            return isNegative?-integerValue:integerValue;
        }

        // Overflow:  Set the integer out of range error.
        else {
            status = RANGE_INTEGER;
            return 0;
        }
    }

    private double computeFloatValue(String wholeDigits, String fractionDigits,
                                    String exponentDigits, char exponentSign) {
        double floatValue = 0.0;
        int exponentValue = computeIntegerValue(exponentDigits);
        String digits = wholeDigits;  // whole and fraction digits

        // Negate the exponent if the exponent sign is '-'.
        if (exponentSign == '-') {
            exponentValue = -exponentValue;
        }

        // If there are any fraction digits, adjust the exponent value
        // and append the fraction digits.
        if (fractionDigits != null) {
            exponentValue -= fractionDigits.length();
            digits += fractionDigits;
        }

        // Check for a real number out of range error.
        if (Math.abs(exponentValue + wholeDigits.length()) > MAX_EXPONENT) {
            status = RANGE_REAL;
            return 0.0f;
        }

        // Loop over the digits to compute the float value.
        boolean isNegative = digits.charAt(0) == '-';
        int index = isNegative?1:0;
        while (index < digits.length()) {
            floatValue = 10 * floatValue +
                    Character.getNumericValue(digits.charAt(index++));
        }

        // Adjust the float value based on the exponent value.
        if (exponentValue != 0) {
            floatValue *= Math.pow(10, exponentValue);
        }

        return isNegative?-floatValue:floatValue;
    }
}

