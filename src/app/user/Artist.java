package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.Song;
import app.page.ArtistPage;
import app.page.Page;
import app.user.ArtistFeatures.Event;
import app.user.ArtistFeatures.Merch;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.*;

import static app.utils.Factories.PageFactory.createPage;

public class Artist extends User {
    @Getter
    private ArrayList<Album> albums;
    @Getter
    ArrayList<Merch> merches;
    @Getter
    ArrayList<Event> events;
    @Getter
    private EnumMap<Enums.PageType,Page> pages;

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
    public String AddAlbum(final CommandInput commandInput) {
        if (albums.stream().anyMatch(album -> album.getName().equals(commandInput.getName()))) {
            return commandInput.getUsername() + " has another album with the same name.";
        } else {
            /* se verifica daca sunt duplicate in albumul citit */

            Set inputSet = new HashSet();
            for (Song song : commandInput.getSongs()) {
                if (!inputSet.add(song.getName())) {
                    return commandInput.getUsername() + " has the same song at least twice in this album.";
                }
            }

            Album album = new Album(commandInput.getName(), commandInput.getUsername(),
                    commandInput.getReleaseYear(), commandInput.getDescription());

            /* se adauga in album toate melodiile citite */
            for (Song song : commandInput.getSongs()) {
                album.addSong(song);
                /* daca melodia data NU exista in library, aceasta se adauga */
                if (!Admin.getSongs().stream().anyMatch(iterSong -> iterSong.getName().equals(song.getName()))) {
                    Admin.addSong(song);
                }
            }

            albums.add(album);
            Admin.addAlbum(album);
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
    private boolean isDateValid(String date) {
        String [] formattedDate = date.split("-");
        System.out.println(formattedDate[0]);

        if (Integer.parseInt(formattedDate[0]) > 28) {
            if (Integer.parseInt(formattedDate[0]) > 31) {
                return false;
            }
            if (Integer.parseInt(formattedDate[1]) == 2) {
                return false;
            }
        }

        if (Integer.parseInt(formattedDate[1]) > 12) {
            return false;
        }

        if (Integer.parseInt(formattedDate[2]) < 1900 || Integer.parseInt(formattedDate[2]) > 2023) {
            return false;
        }

        return true;
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public String addEvent(final CommandInput commandInput) {
        if(!isDateValid(commandInput.getDate())) {
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
     * Show albums array list.
     *
     * @return the array list
     */
    public String addMerch(final CommandInput commandInput) {
        if(commandInput.getPrice() < 0) {
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

    // TODO: ADD JAVADOC
    public static Page getArtistPage(String username) {
        for (User user : Admin.getUsers()) {
            if(user.getUsername().equals(username)) {
                return ((Artist)user).getPages().get(Enums.PageType.ARTIST_PAGE);
            }
        }
        return null;
    }


}
