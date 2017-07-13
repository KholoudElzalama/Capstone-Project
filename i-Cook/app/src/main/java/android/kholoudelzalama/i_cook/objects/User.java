package android.kholoudelzalama.i_cook.objects;

import java.util.List;

/**
 * Created by win on 09/07/2017.
 */

public class User {
    private String name;
    private String email;
    private String photo;
    private List<Recipe> Favourites;
    private List<String> msgs;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Recipe> getFavourites() {
        return Favourites;
    }

    public void setFavourites(List<Recipe> favs) {
        this.Favourites = favs;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }
}
