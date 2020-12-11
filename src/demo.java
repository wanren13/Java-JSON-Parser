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

public class demo {
    public static void main(String[] args) {

        if (false) {
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


        if (false) {
            String dir = "jsonfiles/";
            Source source = null;
            for (int i = 1; i < 8; i++) {
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


        if (false) {
            String dir = "jsonfiles/";
            String url = "https://gist.githubusercontent.com/kinlane/c5e71db5769a6d6b7f221ba89686e3e0/raw/29e9c19907ff36899d4f2b9b6acbc42c1c491dcb/apis-json-example.json";
            String filePath3 = "youtube.json";
            String text = "[1, [12, 3], \"123\", {\"test\":\"test\"}, 1.1, -1.23, true, false, 4.5e3]";

            JSONObj obj = null;

            System.out.println("Parse from URL");
            obj = JSON.parseFromURL(url);
            JSON.print(obj);

            System.out.println("\nParse from file");
            obj = JSON.parseFromFile(dir + filePath3);
            JSON.print(obj);

            System.out.println("\nParse from Text");
            obj = JSON.parseFromText(text);
            JSON.print(obj);
        }


        if (true) {
            String dir = "jsonfiles/";
            JSONObj obj = null;
            for (int i = 1; i < 10; i++) {
                String filePath = dir + "error" + i + ".json";
                System.out.println(filePath + ": \n");
                obj = JSON.parseFromFile(filePath);
                System.out.println("-------------------------------------");
                if (obj == null)
                    System.out.println("obj is null");
                else
                    JSON.print(obj);
                System.out.println("=====================================");
                System.out.println();
            }
        }
    }
}
