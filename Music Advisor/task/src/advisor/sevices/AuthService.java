package advisor.sevices;

import advisor.Client;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthService {
    private static AuthService service;
    private static HttpServer server;
    private boolean authenticated = false;
    private static String authCode;
    private String accessToken;

    private AuthService(){}

    public static AuthService get(int port) {
        if (service == null) {
            service = new AuthService();
            try {
                server = HttpServer.create();
                server.bind(new InetSocketAddress(port), 0);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            server.createContext("/",
                    new HttpHandler() {
                        public void handle(HttpExchange exchange) throws IOException {
                            String query = exchange.getRequestURI().getQuery();
                            String request;
                            if (query != null && query.contains("code")) {
                                authCode = query.substring(5);
                                request = "Got the code. Return back to your program.";
                            } else {
                                request = "Authorization code not found. Try again.";
                            }
                            exchange.sendResponseHeaders(200, request.length());
                            exchange.getResponseBody().write(request.getBytes());
                            exchange.getResponseBody().close();
                        }
                    }
            );
        }
        return service;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void processAccessToken(String url) {
        Client client = new Client();
        String response = client.sendRequest(Client.MethodType.POST, url, authCode);
        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
        accessToken = jo.get("access_token").getAsString();
        authenticated = true;
    }

    public void processAuthCode() {
        server.start();
        while (authCode == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.stop(5);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
