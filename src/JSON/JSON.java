package JSON;

import JSON.parser.JSONScanner;
import JSON.parser.Source;
import JSON.parser.parsers.JSONErrorHandler;
import JSON.parser.parsers.JSONParser;
import JSON.parser.tokens.JSONToken;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;

import static JSON.JSONType.*;
import static JSON.parser.tokens.JSONTokenType.END_OF_FILE;

public class JSON {
    private static String indentation = "  ";
    private static final EnumSet<JSONType> SINGLE_VALUE_SET =
            EnumSet.of(INT, FLOAT, BOOL, STRING, NULL);

    public static JSONObj parseFromText(String text) {
        Reader reader = new StringReader(text);
        return parseFromBufferedReader(new BufferedReader(reader));
    }

    public static JSONObj parseFromFile(String filePath) {
        FileReader file = null;
        try {
            file = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parseFromFile(file);
    }

    public static JSONObj parseFromFile(FileReader file) {
        JSONObj obj = null;
        BufferedReader bufferedReader =
                new BufferedReader(file);
        obj = parseFromBufferedReader(bufferedReader);
        return obj;
    }

    public static JSONObj parseFromURL(String urlPath) {
        URL url = null;
        try {
            url = new URL(urlPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return parseFromURL(url);
    }

    public static JSONObj parseFromURL(URL url) {
        JSONObj obj = null;
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(url.openStream()));
            obj = parseFromBufferedReader(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static JSONObj parseFromBufferedReader(BufferedReader bufferedReader) {
        JSONObj obj = null;
        try {
            Source source = new Source(bufferedReader);
            JSONScanner jsonScanner = new JSONScanner(source);
            JSONParser jsonParser = new JSONParser(jsonScanner);
            JSONErrorHandler jsonErrorHandler = new JSONErrorHandler();
            jsonParser.setErrorHandler(jsonErrorHandler);
            obj = jsonParser.parse();

            // check if there is any more token after parsing
            JSONToken token = jsonScanner.nextToken();
            if (token.getType() != END_OF_FILE) {
                token.setUnexpected();
                jsonErrorHandler.flag(token, EnumSet.of(END_OF_FILE));
                // todo
                obj = null;
            }
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void setIndentation(String indentation) {
        JSON.indentation = indentation;
    }

    public static void print(JSONObj obj) {
        print(obj, indentation);
    }

    public static void print(JSONObj obj, String indentation) {
        println(obj, "", "", indentation, false);
    }

    private static void println(JSONObj obj,
                                String head,
                                String tail,
                                String indentation,
                                boolean afterColon) {
        if (SINGLE_VALUE_SET.contains(obj.getType())) {
            if (!afterColon)
                System.out.print(head);
            System.out.println(obj + tail);
        }
        else {
            if (obj.getType() == ARRAY) {
                ArrayList<JSONObj> arr = obj.getArray();
                if (arr.isEmpty()){
                    if (!afterColon)
                        System.out.print(head);
                    System.out.println("[]" + tail);
                }
                else {
                    System.out.println(afterColon?"[":(head + "["));
                    String new_head = head + indentation;
                    int i = 0;
                    for (; i < arr.size() - 1; i++) {
                        println(arr.get(i), new_head, "," , indentation, false);
                    }
                    println(arr.get(i), new_head, "", indentation, false);
                    System.out.println(head + "]" + tail);
                }
            }
            else {
                LinkedHashMap<String, JSONObj> map = obj.getMap();
                if (map.isEmpty()){
                    if (!afterColon)
                        System.out.print(head);
                    System.out.println("{}" + tail);
                }
                else {
                    System.out.println(afterColon?"{":(head + "{"));
                    String new_head = head + indentation;
                    int i = 0;
                    ArrayList<String> keys = new ArrayList<>(map.keySet());
                    for (; i < keys.size() - 1; i++) {
                        System.out.print(new_head + keys.get(i) +": ");
                        println(map.get(keys.get(i)), new_head, ",", indentation, true);
                    }
                    System.out.print(new_head + keys.get(i) +": ");
                    println(map.get(keys.get(i)), new_head, "", indentation, true);
                    System.out.println(head + "}" + tail);
                }
            }
        }
    }

}
