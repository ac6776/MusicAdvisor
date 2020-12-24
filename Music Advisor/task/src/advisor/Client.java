package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private static final String CLIENT_ID = "59344c824c574c80981592caf1b3c0bf";
    private static final String CLIENT_SECRET = "92f9568356fc4465915ea9eb9833169a";
    private static final String REDIRECT_URI = "http://localhost:8081";
    private HttpClient client;
    private HttpRequest request;

    public Client() {
        client = HttpClient.newBuilder().build();
    }

    public String sendRequest(MethodType type, String url, String code) {
        request = buildRequest(type, url, code);
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest buildRequest(MethodType type, String url, String code) {
        HttpRequest httpRequest = null;
        switch (type) {
            case GET:
                httpRequest = HttpRequest.newBuilder()
                        .header("Authorization", "Bearer " + code)
                        .uri(URI.create(url))
                        .GET()
                        .build();
                break;
            case POST:
                httpRequest = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .uri(URI.create(url + "/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code" +
                            "&code=" + code +
                            "&redirect_uri=" + REDIRECT_URI +
                            "&client_id=" + CLIENT_ID +
                            "&client_secret=" + CLIENT_SECRET))
                    .build();
                break;
        }
        return httpRequest;
    }

//    public void setAuthCode(String authCode) {
//        this.authCode = authCode;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public boolean isAuthenticated() {
//        return authenticated;
//    }
//
//    public void setAuthenticated() {
//        authenticated = true;
//    }

    public enum MethodType {
        POST, GET
    }
}
