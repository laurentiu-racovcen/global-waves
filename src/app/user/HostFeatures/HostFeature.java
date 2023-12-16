package app.user.HostFeatures;

import lombok.Getter;

@Getter
public abstract class HostFeature {
    private final String name;
    private final String description;

    public HostFeature(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
