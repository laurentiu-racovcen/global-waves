package app.user.ArtistFeatures;

import lombok.Getter;

public class Merch extends ArtistFeature {
    @Getter
    private int price;

    public Merch(String name, String description, int price) {
        super(name, description);
        this.price = price;
    }
}
