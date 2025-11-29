package utils;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * JSON Utilities
 * Simple JSON string builder for responses
 */
public class JSONUtils {
    
    /**
     * Create a JSON object string
     */
    public static String toJSON(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(escape(entry.getKey())).append("\":");
            
            Object value = entry.getValue();
            if (value == null) {
                json.append("null");
            } else if (value instanceof String) {
                json.append("\"").append(escape(value.toString())).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else if (value instanceof List) {
                json.append(arrayToJSON((List<?>) value));
            } else if (value instanceof Map) {
                json.append(toJSON((Map<String, Object>) value));
            } else {
                json.append("\"").append(escape(value.toString())).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Create a JSON array string
     */
    public static String arrayToJSON(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            if (item == null) {
                json.append("null");
            } else if (item instanceof String) {
                json.append("\"").append(escape(item.toString())).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof List) {
                json.append(arrayToJSON((List<?>) item));
            } else if (item instanceof Map) {
                json.append(toJSON((Map<String, Object>) item));
            } else {
                json.append("\"").append(escape(item.toString())).append("\"");
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Create a success response
     */
    public static String successResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return toJSON(response);
    }
    
    /**
     * Create an error response
     */
    public static String errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return toJSON(response);
    }
    
    /**
     * Escape special characters in JSON strings
     */
    private static String escape(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Parse JSON request body (simple implementation)
     * Handles simple JSON objects with string and number values
     */
    public static Map<String, String> parseJSON(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return map;
        }
        
        // Remove braces and whitespace
        json = json.trim().replaceAll("^\\{|\\}$", "").trim();
        
        if (json.isEmpty()) {
            return map;
        }
        
        // Split by comma, but be careful with commas inside quoted strings
        // Simple approach: split by comma and process each pair
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;
            
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("^\"|\"$", "").trim();
                String value = keyValue[1].trim();
                
                // Remove quotes if present, otherwise keep as-is (for numbers)
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                // Unescape common escape sequences
                value = value.replace("\\\"", "\"")
                             .replace("\\\\", "\\")
                             .replace("\\n", "\n")
                             .replace("\\r", "\r")
                             .replace("\\t", "\t");
                
                map.put(key, value);
            }
        }
        
        return map;
    }
}

