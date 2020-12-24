package advisor;

import advisor.entities.Category;
import advisor.sevices.AuthService;
import advisor.sevices.FetchDataService;
import advisor.view.View;

import java.util.List;
import java.util.Map;

public class Controller {
    private static View currentView;

    private Controller(){}

    public static void run(Map<String, String> urls, AuthService auth, String input, View view) {
        currentView = view;
        switch (input.split(" ")[0]) {
            case "auth":
                System.out.println("use this link to request the access code:");
                System.out.println(urls.get("-access") + "/authorize?client_id=" +
                        "59344c824c574c80981592caf1b3c0bf&redirect_uri=" +
                        "http://localhost:8081&response_type=code");
                System.out.println("waiting for code...");
                auth.processAuthCode();
                System.out.println("code received");
                System.out.println("making http request for access_token...");
                auth.processAccessToken(urls.get("-access"));
                if (auth.isAuthenticated()) {
                    System.out.println("Success!");
                }
                break;
            case "new":
                if (auth.isAuthenticated()) {
                    List newReleases = FetchDataService.fetch(
                            urls.get("-resource") + "/v1/browse/new-releases",
                            auth.getAccessToken());
                    if (newReleases != null) {
                        currentView.bind(newReleases);
                        currentView.display();
                    }
                } else {
                    System.out.println("Authenticate first.");
                }
                break;
            case "featured":
                if (auth.isAuthenticated()) {
                    List featured = FetchDataService.fetch(
                            urls.get("-resource") + "/v1/browse/featured-playlists",
                            auth.getAccessToken());
                    if (featured != null) {
                        currentView.bind(featured);
                        currentView.display();
                    }
                } else {
                    System.out.println("Authenticate first.");
                }
                break;
            case "categories":
                if (auth.isAuthenticated()) {
                    List categories = FetchDataService.fetch(
                            urls.get("-resource") + "/v1/browse/categories",
                            auth.getAccessToken());
                    if (categories != null) {
                        currentView.bind(categories);
                        currentView.display();
                    }
                } else {
                    System.out.println("Authenticate first.");
                }
                break;
            case "playlists":
                if (auth.isAuthenticated()) {
                    String C_NAME = input.substring("playlists ".length());
                    String catId = null;
                    List categories = FetchDataService.fetch(
                            urls.get("-resource") + "/v1/browse/categories",
                            auth.getAccessToken());
                    for (Object obj : categories) {
                        Category cat = (Category) obj;
                        if (C_NAME.equals(cat.getName())) {
                            catId = cat.getId();
                        }
                    }
                    if (catId != null) {
                        List playlists = FetchDataService.fetch(
                                urls.get("-resource") + "/v1/browse/categories/" + catId + "/playlists",
                                auth.getAccessToken()
                        );
                        if (playlists != null) {
                            currentView.bind(playlists);
                            currentView.display();
                        }
                    } else {
                        System.out.println("Unknown category name.");
                    }
                } else {
                    System.out.println("Authenticate first.");
                }
                break;
            case "next":
                if (currentView.nextPage()) {
                    currentView.display();
                }
                break;
            case "prev":
                if (currentView.prevPage()) {
                    currentView.display();
                }
                break;
            case "exit":
                System.out.println("---GOODBYE!---");
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
