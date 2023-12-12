package app.user.ArtistFeatures;

import lombok.Getter;

public abstract class ArtistFeature {
    @Getter
    private String name;
    @Getter
    private String description;

    public ArtistFeature(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
