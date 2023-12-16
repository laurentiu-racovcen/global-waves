package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Files.Song;
import app.pages.Page;
import app.user.ArtistFeatures.Event;
import app.user.ArtistFeatures.Merch;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;

import static app.Admin.deleteCreatorFromLibrary;
import static app.Admin.getUser;
import static app.utils.Factories.PageFactory.createPage;

@Getter
public class Artist extends User {
    private ArrayList<Album> albums;
    private ArrayList<Merch> merches;
    private ArrayList<Event> events;
    private EnumMap<Enums.PageType, Page> pages;
    private static final int DAYS_LOWER_LIMIT = 28;
    private static final int DAYS_HIGHER_LIMIT = 31;
    private static final int YEAR_LOWER_LIMIT = 1900;
    private static final int YEAR_HIGHER_LIMIT = 2023;
    private static final int MAX_MONTH = 12;

    /**
     * Instantiates a new Artist User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserType.ARTIST);
        albums = new ArrayList<>();
        merches = new ArrayList<>();
        events = new ArrayList<>();
        pages = new EnumMap<>(Enums.PageType.class);
        pages.put(Enums.PageType.ARTIST_PAGE,
                createPage(Enums.PageType.ARTIST_PAGE, this));
    }
    /**
     * Add album.
     *
     * @param commandInput      the command
     * @return the string
     */
    public String addAlbum(final CommandInput commandInput) {
        if (albums.stream().anyMatch(album -> album.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has another album with the same name.";
        } else {
            /* se verifica daca sunt duplicate in albumul citit */

            Set inputSet = new HashSet();
            for (Song song : commandInput.getSongs()) {
                if (!inputSet.add(song.getName())) {
                    return commandInput.getUsername()
                            + " has the same song at least twice in this album.";
                }
            }

            Album album = new Album(commandInput.getName(), commandInput.getUsername(),
                    commandInput.getReleaseYear(), commandInput.getDescription());

            /* se adauga in album toate melodiile citite */
            for (Song song : commandInput.getSongs()) {
                album.addSong(song);
                /* daca melodia data NU exista in library, aceasta se adauga */
                if (Admin.getSongs().stream().noneMatch(iterSong -> iterSong.getName()
                        .equals(song.getName()))) {
                    Admin.addSong(song);
                }
            }

            Admin.addAlbum(album);
            albums.add(album);

            return commandInput.getUsername() + " has added new album successfully.";
        }
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    /**
     * Checks if date is valid.
     *
     * @return the result
     */
    private boolean isDateValid(final String date) {
        String[] formattedDate = date.split("-");

        if (Integer.parseInt(formattedDate[0]) > DAYS_LOWER_LIMIT) {
            if (Integer.parseInt(formattedDate[0]) > DAYS_HIGHER_LIMIT) {
                return false;
            }
            if (Integer.parseInt(formattedDate[1]) == 2) {
                return false;
            }
        }

        if (Integer.parseInt(formattedDate[1]) > MAX_MONTH) {
            return false;
        }

        if (Integer.parseInt(formattedDate[2]) < YEAR_LOWER_LIMIT
                || Integer.parseInt(formattedDate[2]) > YEAR_HIGHER_LIMIT) {
            return false;
        }

        return true;
    }

    /**
     * Adds artist event.
     *
     * @return the result
     */
    public String addEvent(final CommandInput commandInput) {
        if (!isDateValid(commandInput.getDate())) {
            return "Event for " + commandInput.getUsername() + " does not have a valid date.";
        }

        if (events.stream().anyMatch(event -> event.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has another event with the same name.";
        }

        Event event = new Event(commandInput.getName(),
                commandInput.getDescription(), commandInput.getDate());

        /* in events se adauga evenimentul dat */
        events.add(event);
        return commandInput.getUsername() + " has added new event successfully.";
    }

    /**
     * Removes an artist event
     * @param commandInput the input command
     * @return message
     */
    public String removeEvent(final CommandInput commandInput) {

        boolean existsUser = false;

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                existsUser = true;
            }
        }

        if (!existsUser) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        Event foundEvent = null;

        for (Event event : events) {
            if (event.getName().equals(commandInput.getName())) {
                foundEvent = event;
            }
        }

        if (foundEvent == null) {
            return commandInput.getUsername() + " has no event with the given name.";
        }

        events.remove(foundEvent);

        return commandInput.getUsername() + " deleted the event successfully.";
    }

    /**
     * Adds merch to artist.
     *
     * @return the result
     */
    public String addMerch(final CommandInput commandInput) {
        if (commandInput.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        if (merches.stream().anyMatch(merch -> merch.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has merchandise with the same name.";
        }

        Merch merch = new Merch(commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());

        /* in events se adauga evenimentul dat */
        merches.add(merch);
        return commandInput.getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Gets artist page.
     *
     * @return the artist's page
     */
    public static Page getArtistPage(final String username) {
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)
                    && user.getType().equals(Enums.UserType.ARTIST)) {
                return ((Artist) user).getPages().get(Enums.PageType.ARTIST_PAGE);
            }
        }
        return null;
    }

    /**
     * Checks if artist is interacted.
     *
     * @return the result
     */
    public static boolean isArtistInteractedBy(final String username) {
        Artist artist = (Artist) getUser(username);
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.NORMAL)
                && ((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.ONLINE)
                && ((NormalUser) user).interactsWithArtist(artist)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes artist album
     * @param commandInput
     * @return message
     */
    public String removeAlbum(final CommandInput commandInput) {

        Artist artist = (Artist) getUser(commandInput.getUsername());
        Album album = null;

        for (Album albumIter : artist.getAlbums()) {
            if (albumIter.getName().equals(commandInput.getName())) {
                album = albumIter;
            }
        }

        if (album == null) {
            return commandInput.getUsername() + " doesn't have an album with the given name.";
        }

        if (album.isAlbumInteracted()) {
            return commandInput.getUsername() + " can't delete this album.";
        }

        deleteCreatorFromLibrary(commandInput.getUsername(), Enums.UserType.ARTIST);

        return commandInput.getUsername() + " deleted the album successfully.";
    }

    /**
     * Gets all albums likes
     * @return album likes
     */
    public Integer getAllAlbumsLikes() {
        Integer albumsLikes = 0;
        for (Album album : getAlbums()) {
            albumsLikes += album.getAlbumLikes();
        }
        return albumsLikes;
    }
}
