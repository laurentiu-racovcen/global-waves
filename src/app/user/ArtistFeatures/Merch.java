package app.user.ArtistFeatures;

import lombok.Getter;

@Getter
public class Merch extends ArtistFeature {
    private final int price;

    public Merch(final String name, final String description, final int price) {
        super(name, description);
        this.price = price;
    }
}
