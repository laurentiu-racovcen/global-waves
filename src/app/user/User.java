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

                /* daca in creatorResults se afla artistul dat, se returneaza true */
                if (checkedUser.getSearchBar().SearchesCreator(searchedArtist)) {
                    return true;
                }

                if ((checkedUser.getSearchBar().getLastSelectedCreator() != null)) {
                    if ((checkedUser.getSearchBar().getLastSelectedCreator()).getUsername().equals(searchedArtist.getUsername())) {
                        return true;
                    }
                }

            }
        }

        return false;
    }

    // TODO JAVADOC
    public static boolean NormalUserInteractsWithHost(final NormalUser checkedUser, final Host searchedHost) {
        /* verificare in player */
        if (checkedUser.getPlayer().getSource() != null) {
            // aici alice trb sa asculte podcastul Rhythmic Dialog!!!
            if (checkedUser.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PODCAST)) {
                if ((checkedUser.getPlayer().getSource().getAudioCollection()).getOwner().equals(searchedHost.getUsername())) {
                    return true;
                }
            }
        }

        /* verificare in currentPage */
        if (checkedUser.getCurrentPage() == searchedHost.getPages().get(Enums.PageType.HOST_PAGE)) {
            return true;
        }

        /* verificare in searchBar */
        // TODO: DE FACUT SUB FORMA DE SWITCH CASES!! SI LA CELE DE MAI SUS TOT DE FACUT.
        if (checkedUser.getSearchBar().getLastSearchType() != null) {

            if (checkedUser.getSearchBar().getLastSearchType().equals("podcast")) {
                if (((Podcast) checkedUser.getSearchBar().getLastSelectedAudio()).getOwner().equals(searchedHost.getUsername()))
                    return true;
            }

            if (checkedUser.getSearchBar().getLastSearchType().equals("host")) {

                /* daca in creatorResults se afla artistul dat, se returneaza true */
                if (checkedUser.getSearchBar().SearchesCreator(searchedHost)) {
                    return true;
                }

//                if (checkedUser.getSearchBar().getLastSelectedCreator() != null) {
//                    if ((checkedUser.getSearchBar().getLastSelectedCreator()).getUsername().equals(searchedHost.getUsername())) {
//                        return true;
//                    }
//                }
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
    public static boolean NormalUserInteractsWithPodcast(final NormalUser user, final Podcast podcast) {
        if (user.getPlayer().getSource() != null) {
            /* verificare in player */
            if (user.getPlayer().getType().equals("podcast")) {
                /* se verifica daca podcast-ul ruleaza */
                if (((Podcast) user.getPlayer().getSource().getAudioCollection()).getName().equals(podcast.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

}
