import com.sun.net.httpserver.HttpServer;
import controllers.*;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main Application Entry Point
 * Sets up HTTP server and registers all endpoints
 */
public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Register endpoints
            server.createContext("/login", new LoginController());
            server.createContext("/dashboard", new DashboardController());
            server.createContext("/deposit", new DepositController());
            server.createContext("/withdraw", new WithdrawController());
            server.createContext("/transfer", new TransferController());
            server.createContext("/transactions", new TransactionHistoryController());

            // Set executor (null = default)
            server.setExecutor(null);

            // Start server
            server.start();

            System.out.println("========================================");
            System.out.println("Online Banking System Backend Server");
            System.out.println("========================================");
            System.out.println("Server started on port " + PORT);
            System.out.println("Available endpoints:");
            System.out.println("  POST   /login");
            System.out.println("  GET    /dashboard");
            System.out.println("  POST   /deposit");
            System.out.println("  POST   /withdraw");
            System.out.println("  POST   /transfer");
            System.out.println("  GET    /transactions");
            System.out.println("========================================");
            System.out.println("Server is running... Press Ctrl+C to stop");
            System.out.println("========================================");

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

