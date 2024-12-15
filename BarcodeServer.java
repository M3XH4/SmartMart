import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarcodeServer {
    private static String scannedBarcode = null;
    private CartManager cartManager; // Use your existing CartManager instance
    private InventoryManager inventoryManager; // Use your existing InventoryManager instance
    private AtomicBoolean isBarcodeProcessed = new AtomicBoolean(false); // Ensure thread safety

    public BarcodeServer(InventoryManager inventoryManager, CartManager cartManager) {
        this.inventoryManager = inventoryManager;
        this.cartManager = cartManager;
    }
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/barcode_receiver", new BarcodeHandler());
        server.setExecutor(null);
        server.start();
    }
    public static String getScannedBarcode() {
        return scannedBarcode;
    }

    public static void clearScannedBarcode() {
        scannedBarcode = null;
    }

    private class BarcodeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Only accept POST method
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the barcode data from the request body
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"));
                String barcode = reader.readLine(); // Read the barcode from the request body
                reader.close();

                scannedBarcode = barcode;
                // Send response back to the Android client
                String response = "Barcode received: " + barcode;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "ERROR: Unsupported HTTP method. Only POST is allowed.";
                exchange.sendResponseHeaders(405, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}


