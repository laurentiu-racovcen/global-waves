package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.page.HostPage;
import app.page.Page;
import app.user.ArtistFeatures.Event;
import app.user.HostFeatures.Announcement;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static app.Admin.getUser;
import static app.user.Artist.IsArtistInteracted;
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

    // JAVADOC
    public static Page getHostPage(String username) {
        for (User user : Admin.getUsers()) {
            if(user.getUsername().equals(username)) {
                return ((Host)user).getPages().get(Enums.PageType.HOST_PAGE);
            }
        }
        return null;
    }

    // TODO JAVADOC
    public static boolean IsHostInteracted(final String username) {
        Host host = (Host) getUser(username);
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.NORMAL)) {
                if (((NormalUser)user).getConnectionStatus().equals(Enums.ConnectionStatus.ONLINE)) {
                    if (NormalUserInteractsWithHost((NormalUser) user, host) == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Add album.
     *
     * @param commandInput      the command
     * @return the string
     */
    public String AddPodcast(final CommandInput commandInput) {
        if (podcasts.stream().anyMatch(podcast -> podcast.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has another podcast with the same name.";
        } else {
            /* se verifica daca sunt duplicate in albumul citit */

            Set inputSet = new HashSet();
            for (Episode episode : commandInput.getEpisodes()) {
                if (!inputSet.add(episode.getName())) {
                    return commandInput.getUsername() + " has the same episode in this podcast.";
                }
            }

            Podcast podcast = new Podcast(commandInput.getName(), commandInput.getUsername(),
                    commandInput.getEpisodes());

            podcasts.add(podcast);
            Admin.addPodcast(podcast);
            return commandInput.getUsername() + " has added new podcast successfully.";
        }
    }

    /**
     * Add announcement.
     *
     * @return the message
     */
    public String addAnnouncement(final CommandInput commandInput) {
        if (announcements.stream().anyMatch(announcement -> announcement.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has already added an announcement with this name.";
        }

        Announcement announcement = new Announcement(commandInput.getName(),
                commandInput.getDescription());

        /* in annnouncements se adauga evenimentul dat */
        announcements.add(announcement);
        return commandInput.getUsername() + " has successfully added new announcement.";
    }

    /**
     * Adds new user
     * @param commandInput
     * @return message
     */
    public String removeAnnouncement(final CommandInput commandInput) {

        boolean existsUser = false;

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                existsUser = true;
            }
        }

        if (existsUser == false) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        Announcement foundAnnouncement = null;

        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(commandInput.getName())) {
                foundAnnouncement = announcement;
            }
        }

        if (foundAnnouncement == null) {
            return commandInput.getUsername() + " has no announcement with the given name.";
        }

        announcements.remove(foundAnnouncement);

        return commandInput.getUsername() + " has successfully deleted the announcement.";
    }

    /**
     * Show podcasts array list.
     *
     * @return the array list
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }


}
