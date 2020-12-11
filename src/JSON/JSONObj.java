package JSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static JSON.JSONType.*;

public class JSONObj {
    private Boolean value_b;
    private Integer value_i;
    private Double value_d;
    private String value_s;
    private ArrayList<JSONObj> value_a;
    private LinkedHashMap<String, JSONObj> value_m;
    private JSONObj value_o;

    private JSONType type;

    private String indentation = "  ";

    public JSONObj() {
        type = NULL;
        value_o = null;
    }

    public JSONObj(boolean value_b) {
        this(Boolean.valueOf(value_b));
    }

    public JSONObj(Boolean value_b) {
        type = BOOL;
        this.value_b = value_b;
    }

    public JSONObj(int value_i) {
        this(Integer.valueOf(value_i));
    }

    public JSONObj(Integer value_i) {
        type = INT;
        this.value_i = value_i;
    }

    public JSONObj(double value_d) {
        this(Double.valueOf(value_d));
    }

    public JSONObj(Double value_d) {
        type = FLOAT;
        this.value_d = value_d;
    }

    public JSONObj(LinkedHashMap<String, JSONObj> value_m) {
        type = MAP;
        this.value_m = value_m;
    }

    public JSONObj(String value_s) {
        type = STRING;
        this.value_s = value_s;
    }

    public JSONObj(ArrayList<JSONObj> value_a) {
        type = ARRAY;
        this.value_a = value_a;
    }

    // Setters

    private void reset() {
        value_b = null;
        value_i = null;
        value_d = null;
        value_s = null;
        value_a = null;
        value_m = null;
        value_o = null;
    }

    public void resetValue() {
        reset();
        type = NULL;
    }

    public void resetValue(boolean value_b) {
        resetValue(Boolean.valueOf(value_b));
    }

    public void resetValue(Boolean value_b) {
        reset();
        type = BOOL;
        this.value_b = value_b;
    }

    public void resetValue(int value_i) {
        resetValue(Integer.valueOf(value_i));
    }

    public void resetValue(Integer value_i) {
        reset();
        type = INT;
        this.value_i = value_i;
    }

    public void resetValue(double value_d) {
        resetValue(Double.valueOf(value_d));
    }

    public void resetValue(Double value_d) {
        reset();
        type = FLOAT;
        this.value_d = value_d;
    }

    public void resetValue(String value_s) {
        reset();
        type = STRING;
        this.value_s = value_s;
    }

    public void resetValue(ArrayList<JSONObj> value_a) {
        reset();
        type = ARRAY;
        this.value_a = value_a;
    }

    public void resetValue(LinkedHashMap<String, JSONObj> value_m) {
        reset();
        type = MAP;
        this.value_m = value_m;
    }

    // Getters

    public JSONType getType() {
        return type;
    }

    public Object getValue() {
        Object obj;
        switch (type) {
            case BOOL:
                obj = value_b;
                break;
            case INT:
                obj = value_i;
                break;
            case FLOAT:
                obj = value_d;
                break;
            case STRING:
                obj = value_s;
                break;
            case ARRAY:
                obj = value_a;
                break;
            case MAP:
                obj = value_m;
                break;
            case NULL:
                obj = value_o;
                break;
            default:
                obj = null;
        }
        return obj;
    }

    public String getStringValue() {
        return value_s;
    }

    public int getIntValue() {
        return value_i;
    }

    public double getFloatValue() {
        return value_d;
    }

    public boolean getBooleanValue() {
        return value_b;
    }

    public ArrayList<JSONObj> getArray() {
        return value_a;
    }

    public LinkedHashMap<String, JSONObj> getMap() {
        return value_m;
    }

    public String toString() {
        String str;
        if (type == INT)
            str = Integer.toString(value_i);
        else if (type == FLOAT)
            str = Double.toString(value_d);
        else if (type == BOOL)
            str = Boolean.toString(value_b);
        else if (type == STRING)
            str = "\"" + value_s + "\"";
        else if (type == NULL)
            str = "null";
        else if (type == ARRAY) {
            StringBuilder strb = new StringBuilder("[");
            for (JSONObj obj : value_a)
                strb.append(obj + ",");
            if (strb.charAt(strb.length() - 1) == ',')
                strb.deleteCharAt(strb.length() - 1);
            strb.append("]");
            str = strb.toString();
        } else if (type == MAP) {
            StringBuilder strb = new StringBuilder("{");
            for (String key : value_m.keySet()) {
                strb.append(key + ":" + value_m.get(key) + ",");
            }
            if (strb.charAt(strb.length() - 1) == ',')
                strb.deleteCharAt(strb.length() - 1);
            strb.append("}");
            str = strb.toString();
        } else
            str = "Unknown type";
        return str;
    }
}