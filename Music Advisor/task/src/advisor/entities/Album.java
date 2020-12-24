package advisor.entities;

public class Album extends ApiObject {
    private String artists;
    private String url;

    public Album(String name, String artists, String url) {
        super(name);
        this.artists = artists;
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName() + "\n" + getArtists() + "\n" + getUrl() + "\n";
    }
}
