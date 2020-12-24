package advisor;

import advisor.sevices.AuthService;
import advisor.view.View;

import java.util.*;

public class Main {
    static Map<String, String> urls;
    static Scanner scanner;
    static int itemPerPage;
    static View view;

    public static void main(String[] args) {
        parseUrls(args);
        scanner = new Scanner(System.in);
        AuthService authService = AuthService.get(8081);
        view = new View(itemPerPage);

        while (true) {
            String input = scanner.nextLine();
            Controller.run(urls, authService, input, view);
        }
    }

    public static void parseUrls(String[] args) {
        urls = new HashMap<>();
        urls.put("-access", args.length >= 2 ? args[1] : "https://accounts.spotify.com");
        urls.put("-resource", args.length >= 4 ? args[3] : "https://api.spotify.com");
        itemPerPage = args.length >= 6 ? Integer.parseInt(args[5]) : 5;
    }
}
