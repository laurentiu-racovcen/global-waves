package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Getter;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class Artist extends User {

    @Getter
    private ArrayList<Album> albums;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserType.ARTIST);
        albums = new ArrayList<>();
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
}
