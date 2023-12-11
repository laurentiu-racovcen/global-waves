package app.user;

import app.utils.Enums;
import lombok.Getter;

public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private Enums.UserType type;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param type     the type
     */
    public User(final String username, final int age, final String city, final Enums.UserType type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
    }

}
