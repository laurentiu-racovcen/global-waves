package app.user.ArtistFeatures;

import lombok.Getter;

@Getter
public abstract class ArtistFeature {
    private final String name;
    private final String description;

    public ArtistFeature(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
