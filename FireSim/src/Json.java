import java.util.*;

public class Json {
    private HashMap<String, Object> map;

    public Json(String jsonString) {
        map = new HashMap<>();
        parseObject(jsonString.trim(), map);
    }

    private int index;

    private void parseObject(String json, HashMap<String, Object> currentMap) {
        if (json.charAt(index) != '{') throw new IllegalArgumentException("Expected { at position " + index);
        index++; // skip '{'

        while (index < json.length()) {
            skipWhitespace(json);
            if (json.charAt(index) == '}') {
                index++; // skip '}'
                break;
            }

            String key = parseString(json);
            skipWhitespace(json);
            if (json.charAt(index) != ':') throw new IllegalArgumentException("Expected : after key");
            index++; // skip ':'
            skipWhitespace(json);
            Object value = parseValue(json);
            currentMap.put(key, value);

            skipWhitespace(json);
            if (json.charAt(index) == ',') {
                index++; // skip ','
            } else if (json.charAt(index) == '}') {
                index++; // skip '}'
                break;
            }
        }
    }

    private Object parseValue(String json) {
        skipWhitespace(json);
        char c = json.charAt(index);
        if (c == '"') {
            return parseString(json);
        } else if (c == '{') {
            HashMap<String, Object> nestedMap = new HashMap<>();
            parseObject(json, nestedMap);
            return nestedMap;
        } else if (c == '[') {
            return parseArray(json);
        } else {
            return parsePrimitive(json);
        }
    }

    private List<Object> parseArray(String json) {
        if (json.charAt(index) != '[') throw new IllegalArgumentException("Expected [ at position " + index);
        index++; // skip '['
        List<Object> list = new ArrayList<>();

        while (index < json.length()) {
            skipWhitespace(json);
            if (json.charAt(index) == ']') {
                index++; // skip ']'
                break;
            }

            Object value = parseValue(json);
            list.add(value);
            skipWhitespace(json);
            if (json.charAt(index) == ',') {
                index++; // skip ','
            } else if (json.charAt(index) == ']') {
                index++; // skip ']'
                break;
            }
        }
        return list;
    }

    private String parseString(String json) {
        if (json.charAt(index) != '"') throw new IllegalArgumentException("Expected '\"' at position " + index);
        index++; // skip initial '"'
        StringBuilder sb = new StringBuilder();
        while (index < json.length()) {
            char c = json.charAt(index);
            if (c == '\\') {
                index++;
                if (index >= json.length()) break;
                char next = json.charAt(index);
                if (next == 'n') sb.append('\n');
                else if (next == 't') sb.append('\t');
                else if (next == 'r') sb.append('\r');
                else sb.append(next);
            } else if (c == '"') {
                index++; // skip closing '"'
                break;
            } else {
                sb.append(c);
            }
            index++;
        }
        return sb.toString();
    }

    private Object parsePrimitive(String json) {
        int start = index;
        while (index < json.length() && ",}]".indexOf(json.charAt(index)) == -1) {
            index++;
        }
        String token = json.substring(start, index).trim();
        if (token.equals("true") || token.equals("false")) return Boolean.parseBoolean(token);
        try {
            if (token.contains(".")) return Double.parseDouble(token);
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid primitive: " + token);
        }
    }

    private void skipWhitespace(String json) {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) index++;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public static void main(String[] args) {
        String jsonString = "{ \"results\" : [ { \"elevation\" : 1608.637939453125, \"location\" : { \"lat\" : 39.7391536, \"lng\" : -104.9847034 }, \"resolution\" : 4.771975994110107 } ], \"status\" : \"OK\" }";
        Json json = new Json(jsonString);

        System.out.println(json.get("status")); // OK
        List<?> results = (List<?>) json.get("results");
        Map<?, ?> firstResult = (Map<?, ?>) results.get(0);
        System.out.println(firstResult.get("elevation")); // 1608.637939453125
        System.out.println(((Map<?, ?>) firstResult.get("location")).get("lat")); // 39.7391536
    }
}
