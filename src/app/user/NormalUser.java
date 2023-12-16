package app.user;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Album;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.NormalUserPages.HomePage;
import app.pages.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Comparator;
import java.util.Collections;

import static app.Admin.getUser;
import static app.utils.Enums.PageType.HOMEPAGE;
import static app.utils.Factories.PageFactory.createPage;

/**
 * The type NormalUser.
 */
public class NormalUser extends User {
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    @Getter
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private Enums.ConnectionStatus connectionStatus;
    @Getter
    private EnumMap<Enums.PageType, Page> pages;
    @Getter
    private Page currentPage;
    @Getter
    private Enums.PageType currentPageType;
    private static final Integer MAX_RESULTS = 5;

    /**
     * Instantiates a new Normal User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserType.NORMAL);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        connectionStatus = Enums.ConnectionStatus.ONLINE;
        pages = new EnumMap<>(Enums.PageType.class);
        pages.put(HOMEPAGE,
                createPage(HOMEPAGE, this));
        pages.put(Enums.PageType.LIKED_CONTENT_PAGE,
                createPage(Enums.PageType.LIKED_CONTENT_PAGE, this));
        currentPage = pages.get(HOMEPAGE);
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> libraryEntries = searchBar.search(filters, type);
        return new ArrayList<>(libraryEntries);
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        String selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }

        if (searchBar.getLastSearchType().equals("artist")) {
            this.currentPageType = Enums.PageType.ARTIST_PAGE;
            currentPage = Artist.getArtistPage(selected);
            this.searchBar.setLastSearchType(null);
            return "Successfully selected %s's page.".formatted(selected);
        }

        if (searchBar.getLastSearchType().equals("host")) {
            this.currentPageType = Enums.PageType.HOST_PAGE;
            currentPage = Host.getHostPage(selected);
            this.searchBar.setLastSearchType(null);
            lastSearched = false;
            return "Successfully selected %s's page.".formatted(selected);
        }

        return "Successfully selected %s.".formatted(selected);
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelectedAudio() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelectedAudio()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelectedAudio(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the result
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song")
                && !player.getType().equals("album")
                && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if ((likedSongs.stream().anyMatch(songIter -> songIter.getName().equals(song.getName())))) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();

        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, super.getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return playlist outputs
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();

        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelectedAudio();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(super.getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();

        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();

        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets most liked index.
     *
     * @return the index
     */
    public int getMostLikedIndex(final Song song, final String[] genres) {
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;
        for (int i = 0; i < genres.length; i++) {
            if (song.getGenre().equals(genres[i])) {
                counts[i]++;
                if (counts[i] > mostLikedCount) {
                    mostLikedCount = counts[i];
                    mostLikedIndex = i;
                }
                break;
            }
        }
        return mostLikedIndex;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int mostLikedIndex = -1;
        for (Song song : likedSongs) {
            mostLikedIndex = getMostLikedIndex(song, genres);
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switches connection status.
     *
     * @return result
     */
    public String switchConnectionStatus() {
        if (connectionStatus == Enums.ConnectionStatus.OFFLINE) {
            connectionStatus = Enums.ConnectionStatus.ONLINE;
        } else {
            connectionStatus = Enums.ConnectionStatus.OFFLINE;
        }

        return super.getUsername() + " has changed status successfully.";
    }

    /**
     * Generates offline status output.
     *
     * @return ObjectNode
     */
    public ObjectNode offlineStatusOutput(final ObjectMapper objectMapper,
                                          final CommandInput commandInput,
                                          final User user) {
        ObjectNode objectNode = objectMapper.createObjectNode();

        if (commandInput.getCommand().equals("printCurrentPage")) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("command", commandInput.getCommand());
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
        }

        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", user.getUsername() + " is offline.");

        if (commandInput.getCommand().equals("search")) {
            objectNode.put("results", objectMapper.valueToTree(new ArrayList<String>()));
        } else if (commandInput.getCommand()
                .matches("showPlaylists|showPreferredSongs|getPreferredGenre"
                        + "|getTop5Songs|getTop5Playlists|getOnlineUsers")) {
            objectNode.put("result", objectMapper.valueToTree(new ArrayList<String>()));
        }

        return objectNode;
    }

    /**
     * Prints current page of normal user.
     *
     * @return ObjectNode
     */
    public String printCurrentPage() {
        updateTop5LikedSongs();
        updateTop5FollowedPlaylists();
        return currentPage.getPageContents();
    }

    /**
     * Change current page.
     *
     * @return ObjectNode
     */
    public String changePage(final String nextPage) {

        if (nextPage.equals("Home")) {
            currentPage = pages.get(HOMEPAGE);
            return getUsername() + " accessed Home successfully.";
        } else if (nextPage.equals("LikedContent")) {
            currentPage = pages.get(Enums.PageType.LIKED_CONTENT_PAGE);
            return getUsername() + " accessed LikedContent successfully.";
        } else {
            return getUsername() + " is trying to access a non-existent page.";
        }

    }

    /**
     * Updates Top5LikedSongs.
     */
    private void updateTop5LikedSongs() {
        ArrayList<Song> songsCopy = new ArrayList<>(this.likedSongs);

        songsCopy.sort(Comparator.comparingInt(Song::getLikes).reversed());

        ((HomePage) this.getPages().get(HOMEPAGE)).getTop5LikedSongs().clear();

        for (int i = 0; i < MAX_RESULTS && ((HomePage) this.getPages().get(HOMEPAGE))
                .getTop5LikedSongs().size() < songsCopy.size(); i++) {

            ((HomePage) this.getPages().get(HOMEPAGE)).getTop5LikedSongs().add(songsCopy.get(i));
        }
    }

    /**
     * Updates Top5FollowedPlaylists.
     */
    private void updateTop5FollowedPlaylists() {
        ArrayList<Playlist> playlistsCopy = new ArrayList<>(this.followedPlaylists);
        Collections.sort(playlistsCopy, new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist o1, final Playlist o2) {
                int likesCount1 = 0;
                int likesCount2 = 0;
                for (Song iterSong : o1.getSongs()) {
                    likesCount1 += iterSong.getLikes();
                }
                for (Song iterSong : o2.getSongs()) {
                    likesCount2 += iterSong.getLikes();
                }

                return likesCount1 - likesCount2;
            }
        });

        ((HomePage) this.getPages().get(HOMEPAGE)).getTop5FollowedPlaylists().clear();

        for (int i = 0; i < MAX_RESULTS && ((HomePage) this.getPages().get(HOMEPAGE))
                .getTop5FollowedPlaylists().size() < playlistsCopy.size(); i++) {

            ((HomePage) this.getPages().get(HOMEPAGE)).getTop5FollowedPlaylists()
                    .add(playlistsCopy.get(i));

        }
    }

    /**
     * Checks if interacts with normal user
     *
     * @return ObjectNode
     */
    public boolean interactsWithNormalUser(final NormalUser interactedUser) {
        /* verificare in player */
        if (this.getPlayer().getSource() != null
               && this.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PLAYLIST)) {
            return (this.getPlayer().getSource().getAudioCollection()).getOwner()
                    .equals(interactedUser.getUsername());
        }
        return false;
    }

    /**
     * Checks if normal user interacts with artist
     *
     * @return result
     */
    public boolean interactsWithArtist(final Artist searchedArtist) {
        if (this.getPlayer().getSource() != null) {
            /* verificare in player */
            if (this.getPlayer().getType().equals("song")) {
                if (((Song) this.getPlayer().getSource().getAudioFile()).getArtist()
                        .equals(searchedArtist.getUsername())) {
                    return true;
                }
            } else if (this.getPlayer().getType().equals("album")) {
                if ((this.getPlayer().getSource().getAudioCollection()).getOwner()
                        .equals(searchedArtist.getUsername())) {
                    return true;
                }
            }
        }

        /* verificare in currentPage */
        if (this.getCurrentPage() == searchedArtist.getPages().get(Enums.PageType.ARTIST_PAGE)) {
            return true;
        }

        /* verificare in searchBar */
        if (this.getSearchBar().getLastSearchType() != null) {
            switch (this.getSearchBar().getLastSearchType()) {
                case "song":
                    if (((Song) this.getSearchBar().getLastSelectedAudio())
                            .getArtist().equals(searchedArtist.getUsername())) {
                        return true;
                    }
                    break;
                case "album":
                    if (((Album) this.getSearchBar().getLastSelectedAudio())
                            .getOwner().equals(searchedArtist.getUsername())) {
                        return true;
                    }
                    break;
                case "artist":
                    if (this.getSearchBar().searchesCreator(searchedArtist)) {
                        return true;
                    }
                    if ((this.getSearchBar().getLastSelectedCreator() != null)) {
                        if ((this.getSearchBar().getLastSelectedCreator())
                                .getUsername().equals(searchedArtist.getUsername())) {
                            return true;
                        }
                    }
                    break;
                default:
            }
        }

        return false;
    }


    /**
     * Checks if normal user interacts with host
     *
     * @return result
     */
    public boolean interactsWithHost(final Host searchedHost) {
        /* verificare in player */
        if (this.getPlayer().getSource() != null) {
            if (this.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PODCAST)
                && (this.getPlayer().getSource().getAudioCollection()).getOwner()
                        .equals(searchedHost.getUsername())) {
                return true;
            }
        }

        /* verificare in currentPage */
        if (this.getCurrentPage() == searchedHost.getPages().get(Enums.PageType.HOST_PAGE)) {
            return true;
        }

        /* verificare in searchBar */
        if (this.getSearchBar().getLastSearchType() != null) {
            switch (this.getSearchBar().getLastSearchType()) {
                case "podcast":
                    if (((Podcast) this.getSearchBar().getLastSelectedAudio())
                            .getOwner().equals(searchedHost.getUsername())) {
                        return true;
                    }
                    break;
                case "host":
                    if (this.getSearchBar().searchesCreator(searchedHost)) {
                        return true;
                    }
                    break;
                default:
            }
        }

        return false;
    }

    /**
     * Checks if normal user interacts with album
     *
     * @return result
     */
    public boolean interactsWithAlbum(final Album album) {
        if (this.getPlayer().getSource() != null) {
            switch (this.getPlayer().getType()) {
                case "song":
                    /* se verifica daca albumul dat contine vreo melodie care ruleaza */
                    if (((Song) this.getPlayer().getSource().getAudioFile())
                            .getAlbum().equals(album.getName())) {
                        return true;
                    }
                    break;
                case "album":
                    if ((this.getPlayer().getSource().getAudioCollection())
                            .getName().equals(album.getName())) {
                        return true;
                    }
                    break;
                case "playlist":
                    if (((Playlist) (this.getPlayer().getSource()
                            .getAudioCollection())).containsSongFromAlbum(album)) {
                        return true;
                    }
                    break;
                default:
            }
        }

        return false;
    }

    /**
     * Checks if normal user interacts with podcast
     *
     * @return result
     */
    public boolean interactsWithPodcast(final Podcast podcast) {
        if (this.getPlayer().getSource() != null) {
            /* verificare in player */
            if (this.getPlayer().getType().equals("podcast")) {
                /* se verifica daca podcast-ul ruleaza */
                if (((Podcast) this.getPlayer().getSource().getAudioCollection()).getName()
                        .equals(podcast.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if a user is interacted by another user
     *
     * @return result
     */
    public boolean isNormalUserInteractedBy(final String username) {
       NormalUser normalUser = (NormalUser) getUser(username);
       for (User iterUser : Admin.getUsers()) {
          if (iterUser.getType().equals(Enums.UserType.NORMAL)) {
             if (((NormalUser) iterUser).getConnectionStatus().equals(Enums.ConnectionStatus.ONLINE)
                    && ((NormalUser) iterUser).interactsWithNormalUser(normalUser)) {
                    return true;
             }
          }
       }
        return false;
    }

    /**
     * Removes followed playlist.
     */
    public static void removeFollowedPlaylist(final String playlistName, final NormalUser user) {

        for (int i = 0; i < user.getFollowedPlaylists().size(); i++) {
            if (user.getFollowedPlaylists().get(i).getName().equals(playlistName)) {
                user.getFollowedPlaylists().remove(i);
                user.updateTop5FollowedPlaylists();
                break;
            }
        }

    }

    /**
     * Deletes user from database.
     */
    public static void deleteUserFromDatabase(final String username) {

        NormalUser user = (NormalUser) getUser(username);

        /* se decrementeaza numarul de follows a playlist-urilor urmarite de user */
        for (int i = 0; i < user.getFollowedPlaylists().size(); i++) {
            user.getFollowedPlaylists().get(i).decreaseFollowers();
        }

        /* se decrementeaza numarul de likes a melodiilor apreciate de user */
        for (int i = 0; i < user.getLikedSongs().size(); i++) {
            (user.getLikedSongs().get(i)).dislike();
        }

        for (int i = 0; i < Admin.getUsers().size(); i++) {
            if (Admin.getUsers().get(i).getUsername().equals(username)) {
                Admin.getUsers().remove(i);
                break;
            }
        }

    }

    /**
     * Deletes songs from likedSongs that match with the artist.
     */
    public void removeArtistMatches(final String artist) {
        for (int i = 0; i < likedSongs.size(); i++) {
            if (likedSongs.get(i).getArtist().equals(artist)) {
                likedSongs.remove(i);
                updateTop5LikedSongs();
                i--;
            }
        }
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (connectionStatus.equals(Enums.ConnectionStatus.ONLINE)) {
            player.simulatePlayer(time);
        }
    }
}
