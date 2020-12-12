import JSON.JSON;
import JSON.JSONObj;
import JSON.parser.JSONScanner;
import JSON.parser.Source;
import JSON.parser.tokens.JSONToken;
import JSON.parser.tokens.JSONTokenType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class demo {

    static int beginIndex = 1;
    static int endIndex = 19;

    public static void main(String[] args) {
        String ans = "0";
        while (!ans.equalsIgnoreCase("5")) {
            System.out.println("Demo menu");
            System.out.println("1. jsonObj demo");
            System.out.println("2. jsonScanner demo");
            System.out.println("3. jsonParser demo");
            System.out.println("4. jsonErrorHandler demo");
            System.out.println("5. exit");
            System.out.println("Input a number (1-5):");
            Scanner scanner = new Scanner(System.in);
            ans = scanner.next();
            switch(ans) {
                case "1":
                    JSONObjDemo();
                    break;
                case "2":
                    JSONScannerDemo();
                    break;
                case "3":
                    JSONParserDemo();
                    break;
                case "4":
                    JSONErrorHandlerDemo();
                    break;
                default:
                    break;
            }
        }
    }

    public static void JSONObjDemo () {
        JSONObj jarr = new JSONObj();
        ArrayList<JSONObj> arr = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arr.add(new JSONObj(i));
        }
        jarr.resetValue(arr);
        System.out.println(jarr);

        JSONObj jobj = new JSONObj();
        LinkedHashMap<String, JSONObj> map = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            String str = Integer.toString(i);
            map.put(str, new JSONObj(str));
        }
        jobj.resetValue(map);
        System.out.println(jobj);

        jobj.getMap().put("1", jarr);
        System.out.println(jobj);

        jobj.getMap().get("1").getArray().add(new JSONObj(10));
        System.out.println(jobj);

        System.out.println("=======================");
    }

    public static void JSONScannerDemo () {
        String dir = "jsonfiles/badJSON/";
        Source source = null;
        for (int i = beginIndex; i < endIndex; i++) {
            String filePath = dir + "error" + i + ".json";
            try {
                source = new Source(new BufferedReader(new FileReader(filePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONScanner scanner = new JSONScanner(source);
            JSONToken token = null;
            System.out.println(filePath + ":");
            do {
                try {
                    token = scanner.currentToken();
                    scanner.nextToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Type: " + token.getType() + "    " +
                        "Text: " + token.getText() + "    " +
                        "Value: " + token.getValue() + "    " +
                        "LineNumber: " + token.getLineNumber() + "    " +
                        "Position: " + token.getPosition());
            } while (token.getType() != JSONTokenType.END_OF_FILE);
            System.out.println("----------------------------------------");
            System.out.println();
        }
    }

    public static void JSONParserDemo () {
        String dir = "jsonfiles/";
        String url = "https://raw.githubusercontent.com/wanren13/Java-JSON-Parser/master/jsonfiles/test.json";
        String filePath3 = "youtube.json";
        String text = "[1, [12, 3], \"123\", {\"test\":\"test\"}, 1.1, -1.23, true, false, 4.5e3]";

        JSONObj obj;

        System.out.println("Parse from URL");
        obj = JSON.parseFromURL(url);
        if (obj != null) JSON.print(obj);

        System.out.println("\nParse from file");
        obj = JSON.parseFromFile(dir + filePath3);
        if (obj != null) JSON.print(obj);

        System.out.println("\nParse from Text");
        obj = JSON.parseFromText(text);
        if (obj != null) JSON.print(obj);
    }

    public static void JSONErrorHandlerDemo () {
        String goodDir = "jsonfiles/goodJSON/";
        String badDir = "jsonfiles/badJSON/";
        JSONObj obj;
        String filePath;
        for (int i = beginIndex; i < endIndex; i++) {
            // error demo
            filePath = badDir + "error" + i + ".json";
            System.out.println(filePath + ": \n");
            obj = JSON.parseFromFile(filePath);
            System.out.println("-------------------------------------");
            if (obj == null)
                System.out.println("obj is null");
            else
                JSON.print(obj);

            System.out.println();
            System.out.println();
            // good demo
            filePath = goodDir + "test" + i + ".json";
            System.out.println(filePath + ": \n");
            obj = JSON.parseFromFile(filePath);
            System.out.println("-------------------------------------");
            if (obj == null)
                System.out.println("obj is null");
            else
                JSON.print(obj);

            System.out.println();
            System.out.println();
            System.out.println("=====================================");
            System.out.println();
            System.out.println();
        }
    }
}
