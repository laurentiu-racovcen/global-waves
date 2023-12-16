package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;
import app.utils.Enums;

import fileio.input.CommandInput;
import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import lombok.Getter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;

import static app.user.Artist.isArtistInteractedBy;
import static app.user.Host.isHostInteractedBy;
import static app.user.NormalUser.deleteUserFromDatabase;
import static app.user.NormalUser.removeFollowedPlaylist;
import static app.utils.Factories.UserFactory.createUser;

/**
 * The type Admin.
 */
public final class Admin {
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

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new NormalUser(userInput.getUsername(),
                      userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
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
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
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
    public static void updateTimestamp(final int newTimestamp) {
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
                .reversed().thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));

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

    /**
     * An albums comparator
     */
    static class AlbumComparator implements Comparator<Album> {
        @Override
        public int compare(final Album album1, final Album album2) {
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
        for (int i = 0; i < getUsers().size(); i++) {
            if (getUsers().get(i).getType().equals(Enums.UserType.ARTIST)) {
                albumsLikes.put(getUsers().get(i).getUsername(), ((Artist) getUsers()
                        .get(i)).getAllAlbumsLikes());
                artistsCount++;
            }
        }

        List<String> sortedArtists = new ArrayList<>();

        for (int i = 0; i < LIMIT && i < artistsCount; i++) {
            sortedArtists.add(Collections.max(albumsLikes.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
            albumsLikes.remove(Collections.max(albumsLikes.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
        }

        return sortedArtists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * Adds new user in database
     * @param commandInput the input command
     * @return message
     */
    public static String addUser(final CommandInput commandInput) {

        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                return "The username " + commandInput.getUsername() + " is already taken.";
            }
        }
        Admin.users.add(createUser(commandInput.getType(), commandInput.getUsername(),
                commandInput.getAge(), commandInput.getCity()));

        return "The username " + commandInput.getUsername() + " has been added successfully.";
    }

    /**
     * Deletes user playlists
     * @param user          checked user
     * @param playlistIter  searched user's playlist
     */
    public static void deleteUserPlaylists(final User user, final Playlist playlistIter) {

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

    /**
     * Deletes user playlists from database
     * @param user          checked user
     */
    public static void deleteUserPlaylistsFromDatabase(final NormalUser user) {

        for (Playlist playlistIter : user.getPlaylists()) {
            for (int i = 0; i < Admin.getUsers().size(); i++) {
                if (Admin.getUsers().get(i) != user
                        && playlistIter.getVisibility().equals(Enums.Visibility.PUBLIC)) {
                    deleteUserPlaylists(Admin.getUsers().get(i), playlistIter);
                }
            }
        }

    }

    /**
     * Deletes creator from library
     * @param username      given username of creator
     * @param type          given type of creator
     */
    public static void deleteCreatorFromLibrary(final String username, final Enums.UserType type) {

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
     * Removes user from database
     * @param commandInput input command
     * @return message
     */
    public static String deleteUser(final CommandInput commandInput) {

        boolean existsUser = false;

        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                existsUser = true;
            }
        }

        if (!existsUser) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.ARTIST)) {
            if (isArtistInteractedBy(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteCreatorFromLibrary(commandInput.getUsername(), Enums.UserType.ARTIST);
        } else if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.HOST)) {
            if (isHostInteractedBy(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteCreatorFromLibrary(commandInput.getUsername(), Enums.UserType.HOST);
        } else if (getUser(commandInput.getUsername()).getType().equals(Enums.UserType.NORMAL)) {
            if (((NormalUser) getUser(commandInput.getUsername()))
                    .isNormalUserInteractedBy(commandInput.getUsername())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
            deleteUserPlaylistsFromDatabase(((NormalUser) getUser(commandInput.getUsername())));
            deleteUserFromDatabase(commandInput.getUsername());
        }

        return commandInput.getUsername() + " was successfully deleted.";
    }

    /**
     * Adds new song in library
     * @param song song to be added in library
     */
    public static void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Adds new podcast in library
     * @param podcast podcast to be added in library
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Adds new album in library
     * @param album album to be added in library
     */
    public static void addAlbum(final Album album) {
        albums.add(album);
    }
}
