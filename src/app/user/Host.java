package app.user;

import app.audio.Collections.*;
import app.page.HostPage;
import app.page.Page;
import app.user.HostFeatures.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class Host extends User {
    @Getter
    private ArrayList<Podcast> podcasts;
    @Getter
    ArrayList<Announcement> announcements;
    @Getter
    private EnumMap<Enums.PageType,Page> pages;

    /**
     * Instantiates a new Host User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserType.HOST);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        pages = new EnumMap<>(Enums.PageType.class);
        pages.put(Enums.PageType.HOST_PAGE, new HostPage());
    }
}
