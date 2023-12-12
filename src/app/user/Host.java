package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.page.HostPage;
import app.page.Page;
import app.user.HostFeatures.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;

import static app.utils.Factories.PageFactory.createPage;

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
        pages.put(Enums.PageType.HOST_PAGE,
                createPage(Enums.PageType.HOST_PAGE, this));
    }

    public static Page getHostPage(String username) {
        for (User user : Admin.getUsers()) {
            if(user.getUsername().equals(username)) {
                return ((Host)user).getPages().get(Enums.PageType.HOST_PAGE);
            }
        }
        return null;
    }
}
