package app.user.ArtistFeatures;

import lombok.Getter;

@Getter
public class Event extends ArtistFeature {
    private final String date;

    public Event(final String name, final String description, final String date) {
        super(name, description);
        this.date = date;
    }
}
