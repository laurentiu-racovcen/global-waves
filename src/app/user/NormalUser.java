package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.page.ArtistPage;
import app.page.NormalUserPages.HomePage;
import app.page.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.*;

import static app.Admin.getUser;
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
    private EnumMap<Enums.PageType,Page> pages;
    @Getter
    private Page currentPage;
    @Getter
    private Enums.PageType currentPageType;
    @Getter
    ArrayList<Song> top5LikedSongs;
    @Getter
    ArrayList<Playlist> top5FollowedPlaylists;
    private static final Integer MAX_RESULTS = 5;

    /**
     * Instantiates a new Normal User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public NormalUser (final String username, final int age, final String city) {
        super(username, age, city, Enums.UserType.NORMAL);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        connectionStatus = Enums.ConnectionStatus.ONLINE;
        top5LikedSongs = new ArrayList<>();
        top5FollowedPlaylists = new ArrayList<>();
        pages = new EnumMap<>(Enums.PageType.class);
        pages.put(Enums.PageType.HOMEPAGE,
                createPage(Enums.PageType.HOMEPAGE, this));
        pages.put(Enums.PageType.LIKED_CONTENT_PAGE,
                createPage(Enums.PageType.LIKED_CONTENT_PAGE, this));
        currentPage = pages.get(Enums.PageType.HOMEPAGE);
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
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> libraryEntries = searchBar.search(filters, type);

//        if (searchBar.getLastSearchType().equals("song")
//        || searchBar.getLastSearchType().equals("podcast")
//        || searchBar.getLastSearchType().equals("playlist")
//        || searchBar.getLastSearchType().equals("album")) {
        for (String libraryEntry : libraryEntries) {
            results.add(libraryEntry);
        }
//        }
        return results;
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
            return "Successfully selected %s's page.".formatted(selected);
        }

        if (searchBar.getLastSearchType().equals("host")) {
            this.currentPageType = Enums.PageType.HOST_PAGE;
            currentPage = Host.getHostPage(selected);
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
     * @return the string
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

        if (!player.getType().equals("playlist")) {
            return "The loaded source is not a playlist.";
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

        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        updateTop5LikedSongs();
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
     * @return the array list
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
        updateTop5FollowedPlaylists();

        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
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
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
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
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switches connection status.
     *
     * @return current connection status
     */
    public String switchConnectionStatus() {
        if (connectionStatus == Enums.ConnectionStatus.OFFLINE) {
            connectionStatus = Enums.ConnectionStatus.ONLINE;
        } else {
            connectionStatus = Enums.ConnectionStatus.OFFLINE;
        }

        return super.getUsername() + " has changed status successfully.";
    }

    public ObjectNode offlineStatusOutput(ObjectMapper objectMapper, CommandInput commandInput, User user) {
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

        // TODO: CHANGE CASES TO A SIMPLE FORM BASED ON ENUM WITH COMMANDS
        if (commandInput.getCommand().equals("search")
        ) {
            objectNode.put("results", objectMapper.valueToTree(new ArrayList<String>()));
        } else if (commandInput.getCommand().equals("showPlaylists")
                || commandInput.getCommand().equals("showPreferredSongs")
                || commandInput.getCommand().equals("getPreferredGenre")
                || commandInput.getCommand().equals("getTop5Songs")
                || commandInput.getCommand().equals("getTop5Playlists")
                || commandInput.getCommand().equals("getOnlineUsers")
        ) {
            objectNode.put("result", objectMapper.valueToTree(new ArrayList<String>()));
        }

        return objectNode;
    }

    public String printCurrentPage() {
//        switch (currentPageType) {
//            case HOMEPAGE: return (currentPage).getPageContents();
//        }
        return currentPage.getPageContents();
    }

    public String changePage(String nextPage) {
        if (nextPage.equals("Home")) {
            currentPage = pages.get(Enums.PageType.HOMEPAGE);
            return getUsername() + " accessed Home successfully.";
        } else if (nextPage.equals("LikedContent")) {
            currentPage = pages.get(Enums.PageType.LIKED_CONTENT_PAGE);
            return getUsername() + " accessed LikedContent successfully.";
        } else {
            return getUsername() + " is trying to access a non-existent page.";
        }
    }

    private void updateTop5LikedSongs() {
        List<Song> songs = this.likedSongs;
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {

                return o1.getLikes() - o2.getLikes();
            }
        });

        this.top5LikedSongs.clear();

        while (this.top5LikedSongs.size() < MAX_RESULTS && this.top5LikedSongs.size() < songs.size()) {
            this.top5LikedSongs.add(songs.get(top5LikedSongs.size()));
        }
    }

    private void updateTop5FollowedPlaylists() {
        List<Playlist> playlists = this.followedPlaylists;
        Collections.sort(playlists, new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
                 // se parcurg ambele playlist-uri curente
                 // si se aduna like-urile acestora
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

        this.top5FollowedPlaylists.clear();

        while (this.top5FollowedPlaylists.size() < MAX_RESULTS &&
                this.top5FollowedPlaylists.size() < playlists.size()) {
            this.top5FollowedPlaylists.add(playlists.get(top5FollowedPlaylists.size()));
        }
    }

    // TODO JAVADOC
    public boolean InteractsWithNormalUser(final NormalUser interactedUser) {
        /* verificare in player */
        if (this.getPlayer().getSource() != null) {
            if (this.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PLAYLIST)) {
                if ((this.getPlayer().getSource().getAudioCollection()).getOwner().equals(interactedUser.getUsername())) {
                    return true;
                }
            }
        }

        return false;
    }

    // TODO JAVADOC, aici se verifica daca un user este interactionat de alt user dat prin parametru
    public boolean IsNormalUserInteractedBy(final String username) {
        NormalUser normalUser = (NormalUser) getUser(username);
        for (User iterUser : Admin.getUsers()) {
            if (iterUser.getType().equals(Enums.UserType.NORMAL)) {
                if (((NormalUser)iterUser).getConnectionStatus().equals(Enums.ConnectionStatus.ONLINE)) {
                    if (((NormalUser)iterUser).InteractsWithNormalUser(normalUser)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // TODO JAVADOC + hide if-for-if
    public static void removeFollowedPlaylist(String playlistName, NormalUser user) {

        for (int i = 0; i < user.getFollowedPlaylists().size(); i++) {
            if (user.getFollowedPlaylists().get(i).getName().equals(playlistName)) {
                user.getFollowedPlaylists().remove(i);
                user.updateTop5FollowedPlaylists();
                break;
            }
        }

    }

    // TODO JAVADOC
    public static void deleteUserFromDatabase(String username) {

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
