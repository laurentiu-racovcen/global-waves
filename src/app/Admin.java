package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.page.Page;
import app.user.Artist;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;
import app.utils.Enums;
import fileio.input.*;
import lombok.Getter;
import org.apache.commons.collections.map.MultiValueMap;

import java.util.*;

import static app.user.Artist.IsArtistInteracted;
import static app.user.Host.IsHostInteracted;
import static app.user.NormalUser.*;
import static app.user.User.*;

/**
 * The type Admin.
 */
public final class Admin {
    private static Admin adminInstance = null;
    @Getter
    private static List<User> users = new ArrayList<>();
    @Getter
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    public static Admin getInstance() {
        if (adminInstance == null) {
            adminInstance = new Admin();
        }
        return adminInstance;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new NormalUser(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                         episodeInput.getDuration(),
                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals(Enums.UserType.NORMAL)) {
                playlists.addAll(((NormalUser) user).getPlaylists());
            }
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }


    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.getType().equals(Enums.UserType.NORMAL)) {
                ((NormalUser) user).simulateTime(elapsed);
            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    static class AlbumComparator implements Comparator<Album> {
        @Override
        public int compare(Album album1, Album album2) {
            int likesCompare = album2.getAlbumLikes() - album1.getAlbumLikes();
            if (likesCompare == 0) {
                likesCompare = album1.getName().compareTo(album2.getName());
            }
            return likesCompare;
        }
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {

        List<Album> checkedAlbums = new ArrayList<>(albums);
        checkedAlbums.sort(new AlbumComparator());

        List<String> sortedAlbums = new ArrayList<>();

        for (int i = 0; (i < checkedAlbums.size()) && (i < LIMIT); i++) {
            sortedAlbums.add(checkedAlbums.get(i).getName());
        }

        return sortedAlbums;
    }

    /**
     * Gets top 5 artists.
     *
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {

        TreeMap<String, Integer> albumsLikes = new TreeMap<>();

        int artistsCount = 0;
        for (int i=0; i < getUsers().size(); i++){
            if (getUsers().get(i).getType().equals(Enums.UserType.ARTIST)) {
                albumsLikes.put(getUsers().get(i).getUsername(),((Artist)getUsers().get(i)).getAllAlbumsLikes());
                artistsCount++;
            }
        }

        List<String> sortedArtists = new ArrayList<>();

        for (int i=0; i < LIMIT && i < artistsCount; i++) {
            sortedArtists.add(Collections.max(albumsLikes.entrySet(), Map.Entry.comparingByValue()).getKey());
            albumsLikes.remove(Collections.max(albumsLikes.entrySet(), Map.Entry.comparingByValue()).getKey());
        }

        return sortedArtists;
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * Adds new user
     * @param commandInput
     * @return message
     */
    public static String addUser(final CommandInput commandInput) {

        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                return "The username " + commandInput.getUsername() + " is already taken.";
            }
        }

        if (commandInput.getType().equals("artist")) {
            Admin.users.add(new Artist(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
        } else if (commandInput.getType().equals("host")) {
            Admin.users.add(new Host(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
        } else {
            Admin.users.add(new NormalUser(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
        }

        return "The username " + commandInput.getUsername() + " has been added successfully.";
    }

    // TODO JAVADOC + hide if-for-if
    public static void deleteUserPlaylists(User user, Playlist playlistIter) {

        if (user.getType().equals(Enums.UserType.NORMAL)) {

            /* se verifica daca user are vreun playlist cu numele lui PlaylistIter */
            for (Playlist playlist : ((NormalUser) user).getFollowedPlaylists()) {
                if (playlist.getName().equals(playlistIter.getName())) {
                    removeFollowedPlaylist(playlistIter.getName(), ((NormalUser) user));
                    break;
                }
            }

        }

    }

    // TODO JAVADOC + hide if-for-if
    public static void deleteUserPlaylistsFromDatabase(NormalUser user) {

        for (Playlist playlistIter : user.getPlaylists()) { // TODO: ADD: ONLY IF PLAYLIST IS PUBLIC, pt a nu face iteratii degeaba
            for (int i = 0; i < Admin.getUsers().size(); i++) {
                if (Admin.getUsers().get(i) != user) {
                    deleteUserPlaylists(Admin.getUsers().get(i), playlistIter);
                }
            }
        }

    }

    // TODO JAVADOC + hide if-for-if
    public static void deleteCreatorFromLibrary(String username, Enums.UserType type) {

        if (type.equals(Enums.UserType.ARTIST)) {

            for (int i = 0; i < Admin.albums.size(); i++) {
                if (albums.get(i).getOwner().equals(username)) {
                    Admin.albums.remove(i);
                    i--;
                }
            }

            for (int i = 0; i < Admin.getSongs().size(); i++) {
                if (Admin.getSongs().get(i).getArtist().equals(username)) {
                    Admin.songs.remove(i);
                    i--;
                }
            }

            // TODO - DELETE SONGS FROM LIKED SONGS ale tuturor userilor - la artist

            for (User user : Admin.getUsers()) {
                if (user.getType().equals(Enums.UserType.NORMAL)) {
                    ((NormalUser) user).removeArtistMatches(username);
                }
            }
        }

        if (type.equals(Enums.UserType.HOST)) {
            for (int i = 0; i < Admin.getPodcasts().size(); i++) {
                if (Admin.podcasts.get(i).getOwner().equals(username)) {
                    Admin.podcasts.remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < Admin.getUsers().size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.remove(i);
                break;
            }
        }
    }

    /**
     * Removes a user from database
     * @param commandInput
     * @return message
     */
    public static String deleteUser(final CommandInput commandInput) {

        boolean existsUser = false;

        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                existsUser = true;
            }
        }

        if (existsUser == false) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.ARTIST)) {
            if (IsArtistInteracted(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteCreatorFromLibrary(commandInput.getUsername(), Enums.UserType.ARTIST);
        } else if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.HOST)) {
            if (IsHostInteracted(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteCreatorFromLibrary(commandInput.getUsername(), Enums.UserType.HOST);
        } else if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.NORMAL)) {
            if (((NormalUser)getUser(commandInput.getUsername())).IsNormalUserInteractedBy(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteUserPlaylistsFromDatabase(((NormalUser)getUser(commandInput.getUsername())));
            deleteUserFromDatabase(commandInput.getUsername());
        }

        return commandInput.getUsername() + " was successfully deleted.";
    }

    /**
     * Adds new song in library
     * @param song
     * @return message
     */
    public static void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Adds new podcast in library
     * @param podcast
     * @return message
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Adds new album
     * @param album
     * @return message
     */
    public static void addAlbum(final Album album) {
        albums.add(album);
    }
}
