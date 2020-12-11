package JSON.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class Source
{
    public static final char EOF = (char) 0;  // end-of-file character

    private final BufferedReader reader;      // reader for the source
    private String line;                      // source line
    private char currentChar;
    private int lineNum;                      // current source line number
    private int currentPos;                   // current source line position
    private boolean isEOF;

    public Source(BufferedReader reader) {
        lineNum = 0;
        currentPos = 0;  // set to -1 to read the first source line
        this.reader = reader;
        isEOF = false;
        currentChar = EOF;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getPosition() {
        return currentPos;
    }

    public String getLine() {
        return line;
    }

    public char currentChar() throws Exception {
        // try to read the first line
        if (line == null)
            line = reader.readLine();

        // it is EOF if there is nothing to read
        isEOF = line == null;

        if (!isEOF) {
            if (currentPos < line.length()) {
                currentChar = line.charAt(currentPos);
            }
            else {
                // read a new line
                String newline = reader.readLine();
                if (newline != null) {
                    line = newline;
                    currentPos = 0;
                    lineNum++;
                    currentChar = currentChar();
                }
                else isEOF = true;
            }
        }
        currentChar = isEOF?EOF:currentChar;
        return currentChar;
    }

    public String currentWord() {
        if (currentChar == EOF) return "";
        int beginIndex = currentPos;
        int endIndex = getEndIndex();
        return line.substring(beginIndex, endIndex);
    }

    public void consumeCurrentWord() {
        currentPos = getEndIndex();
    }

    private int getEndIndex() {
        int endIndex = currentPos;
        if (line == null) return endIndex;

        Set<Character> AFTER_VALUE_SET = Set.of(',', ']', '}');

        while (endIndex < line.length() &&
                !Character.isWhitespace(line.charAt(endIndex)) &&
                !AFTER_VALUE_SET.contains(line.charAt(endIndex))) {
            endIndex++;
        }
        return endIndex;
    }

    public char nextChar() throws Exception {
        if (!isEOF)
            ++currentPos;
        return currentChar();
    }

    public void close() throws Exception {
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }
}
