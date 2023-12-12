package app.user.HostFeatures;

import lombok.Getter;

public abstract class HostFeature {
    @Getter
    private final String name;
    @Getter
    private final String description;

    public HostFeature(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
