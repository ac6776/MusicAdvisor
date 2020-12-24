package advisor.entities;

public class Playlist extends ApiObject {
    private String url;

    public Playlist(String name, String url) {
        super(name);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName() + "\n" + getUrl() + "\n";
    }
}
