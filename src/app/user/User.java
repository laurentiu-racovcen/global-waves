package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

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

    // TODO JAVADOC
    public static boolean NormalUserInteractsWithArtist(final NormalUser checkedUser, final Artist searchedArtist) {
        if (checkedUser.getPlayer().getSource() != null) {
            /* verificare in player */
            if (checkedUser.getPlayer().getType().equals("song")) {
                if (((Song) checkedUser.getPlayer().getSource().getAudioFile()).getArtist().equals(searchedArtist.getUsername())) {
                    return true;
                }
            } else if (checkedUser.getPlayer().getType().equals("album")) {
                if ((checkedUser.getPlayer().getSource().getAudioCollection()).getOwner().equals(searchedArtist.getUsername())) {
                    return true;
                }
            }
        }

        /* verificare in currentPage */
        if (checkedUser.getCurrentPage() == searchedArtist.getPages().get(Enums.PageType.ARTIST_PAGE)) {
            return true;
        }

        /* verificare in searchBar */
        // TODO: DE FACUT SUB FORMA DE SWITCH CASES!! SI LA CELE DE MAI SUS TOT DE FACUT.
        if (checkedUser.getSearchBar().getLastSearchType() != null) {
            if (checkedUser.getSearchBar().getLastSearchType().equals("song")) {
                if (((Song) checkedUser.getSearchBar().getLastSelectedAudio()).getArtist().equals(searchedArtist.getUsername()))
                    return true;
            } else if (checkedUser.getSearchBar().getLastSearchType().equals("album")) {
                if (((Album) checkedUser.getSearchBar().getLastSelectedAudio()).getOwner().equals(searchedArtist.getUsername()))
                    return true;
            }

            if (checkedUser.getSearchBar().getLastSearchType().equals("artist")) {
                if ((checkedUser.getSearchBar().getLastSelectedCreator()).getUsername().equals(searchedArtist.getUsername())) {
                    return true;
                }
            }
        }

        return false;
    }

    // TODO JAVADOC
    public static boolean NormalUserInteractsWithHost(final NormalUser checkedUser, final Host searchedHost) {
        /* verificare in player */
        if (checkedUser.getPlayer().getSource().getType().equals("podcast")) {
            if ((checkedUser.getPlayer().getSource().getAudioCollection()).getOwner().equals(searchedHost.getUsername())) {
                return true;
            }
        }

        /* verificare in currentPage */
        if (checkedUser.getCurrentPage() == searchedHost.getPages().get(Enums.PageType.HOST_PAGE)) {
            return true;
        }

        /* verificare in searchBar */
        // TODO: DE FACUT SUB FORMA DE SWITCH CASES!! SI LA CELE DE MAI SUS TOT DE FACUT.
        if (checkedUser.getSearchBar().getLastSearchType().equals("podcast")) {
            if (((Podcast)checkedUser.getSearchBar().getLastSelectedAudio()).getOwner().equals(searchedHost.getUsername()))
                return true;
        }

        if (checkedUser.getSearchBar().getLastSearchType().equals("host")) {
            if ((checkedUser.getSearchBar().getLastSelectedCreator()).getUsername().equals(searchedHost.getUsername())) {
                return true;
            }
        }

        return false;
    }

    // TODO JAVADOC
    public static boolean NormalUserInteractsWithAlbum(final NormalUser user, final Album album) {
        if (user.getPlayer().getSource() != null) {
            /* verificare in player */
            if (user.getPlayer().getType().equals("song")) {
                /* se verifica daca albumul dat contine vreo melodie care ruleaza */
                if (((Song) user.getPlayer().getSource().getAudioFile()).getAlbum().equals(album.getName())) {
                    return true;
                }
            } else if (user.getPlayer().getType().equals("album")) {
                if ((user.getPlayer().getSource().getAudioCollection()).getName().equals(album.getName())) {
                    return true;
                }
            } else if (user.getPlayer().getType().equals("playlist")) {
                if (((Playlist)(user.getPlayer().getSource().getAudioCollection())).containsSongFromAlbum(album)) {
                    return true;
                }
            }
        }

        return false;
    }

    // TODO JAVADOC
    public static void removeArtistMatches(ArrayList<Song> likedSongs, String artist) {
        for (int i = 0; i < likedSongs.size(); i++) {
            if (likedSongs.get(i).getArtist().equals(artist)) {
                likedSongs.remove(i);
                i--;
            }
        }
    }


    // TODO JAVADOC + hide if-for-if
    public static void deleteCreatorFromLibrary(String username, Enums.UserType type) {

        if (type.equals(Enums.UserType.ARTIST)) {
            for (int i = 0; i < Admin.getAlbums().size(); i++) {
                if (Admin.getAlbums().get(i).getOwner().equals(username)) {
                    Admin.getAlbums().remove(i);
                }
            }
            for (int i = 0; i < Admin.getSongs().size(); i++) {
                if (Admin.getSongs().get(i).getArtist().equals(username)) {
                    Admin.getSongs().remove(i);
                }
            }
            // TODO - DELETE SONGS FROM LIKED SONGS ale tuturor userilor - la artist
            for (int i = 0; i < Admin.getUsers().size(); i++) {
                if (Admin.getUsers().get(i).getType().equals(Enums.UserType.NORMAL)) {
                    removeArtistMatches(((NormalUser)Admin.getUsers().get(i)).getLikedSongs(), username);
                }
            }
        }

        if (type.equals(Enums.UserType.HOST)) {
            for (int i = 0; i < Admin.getPodcasts().size(); i++) {
                if (Admin.getPodcasts().get(i).getOwner().equals(username)) {
                    Admin.getPodcasts().remove(i);
                }
            }
        }

        for (int i = 0; i < Admin.getUsers().size(); i++) {
            if (Admin.getUsers().get(i).getUsername().equals(username)) {
                Admin.getUsers().remove(i);
            }
        }
    }

}
