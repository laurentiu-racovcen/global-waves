package app.user.ArtistFeatures;

import lombok.Getter;

public class Event extends ArtistFeature {
    @Getter
    private String date;

    public Event(String name, String description, String date) {
        super(name, description);
        this.date = date;
    }
}
