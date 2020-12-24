package advisor.sevices;

import advisor.entities.Album;
import advisor.entities.ApiObject;
import advisor.entities.Category;
import advisor.Client;
import advisor.entities.Playlist;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FetchDataService {
    private static Client client;

    private FetchDataService() {
    }

    public static List<? extends ApiObject> fetch(String url, String accessToken) {
        if (client == null) {
            client = new Client();
        }

        String response = client.sendRequest(Client.MethodType.GET, url, accessToken);
        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

        if (jo.has("playlists")) {
            //featured
            List<Playlist> featured = new ArrayList<>();
            for (JsonElement item : jo.getAsJsonObject("playlists").getAsJsonArray("items")) {
                featured.add(new Playlist(
                        item.getAsJsonObject().get("name").getAsString(),
                        item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString()));
            }
            return featured;
        }
        if (jo.has("albums")) {
            //new release
            List<Album> albums = new ArrayList<>();
            for (JsonElement album : jo.getAsJsonObject("albums").getAsJsonArray("items")) {
                List<String> artists = new ArrayList<>();
                for (JsonElement artist : album.getAsJsonObject().getAsJsonArray("artists")) {
                    artists.add(artist.getAsJsonObject().get("name").getAsString());
                }
                albums.add(new Album(
                        album.getAsJsonObject().get("name").getAsString(),
                        Arrays.toString(artists.toArray()),
                        album.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString()));
            }
            return albums;
        }
        if (jo.has("categories")) {
            //categories
            List<Category> categories = new ArrayList<>();
            for (JsonElement category : jo.getAsJsonObject("categories").getAsJsonArray("items")) {
                categories.add(new Category(
                        category.getAsJsonObject().get("name").getAsString(),
                        category.getAsJsonObject().get("id").getAsString()
                ));
            }
            return categories;
        }
        return null;
    }
}
