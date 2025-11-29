package utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Response Utilities
 * Helper methods for HTTP responses
 */
public class ResponseUtils {
    
    /**
     * Send JSON response
     */
    public static void sendJSONResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        byte[] response = json.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
    
    /**
     * Send success response
     */
    public static void sendSuccess(HttpExchange exchange, String message, Object data) throws IOException {
        String json = JSONUtils.successResponse(message, data);
        sendJSONResponse(exchange, 200, json);
    }
    
    /**
     * Send error response
     */
    public static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String json = JSONUtils.errorResponse(message);
        sendJSONResponse(exchange, statusCode, json);
    }
    
    /**
     * Handle CORS preflight requests
     */
    public static void handleCORS(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.sendResponseHeaders(200, -1);
        exchange.close();
    }
    
    /**
     * Read request body as string
     */
    public static String readRequestBody(HttpExchange exchange) throws IOException {
        java.io.InputStream is = exchange.getRequestBody();
        java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}

