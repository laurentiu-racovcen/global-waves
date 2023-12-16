package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

import static app.Admin.getUser;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Command Runner Instance
     */
    private static CommandRunner commandRunnerInstance = null;

    private CommandRunner() {
    }

    /**
     * Gets instance of admin
     * @return admin instance
     */
    public static CommandRunner getInstance() {
        if (commandRunnerInstance == null) {
            commandRunnerInstance = new CommandRunner();
        }
        return commandRunnerInstance;
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode search(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = ((NormalUser) user).search(filters, type);
        String message = "Search returned " + results.size() + " results";

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode select(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode load(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode playPause(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode repeat(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode shuffle(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        Integer seed = commandInput.getSeed();
        String message = ((NormalUser) user).shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode forward(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode backward(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode like(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode next(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode prev(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).createPlaylist(commandInput.getPlaylistName(),
                                             commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        String message = ((NormalUser) user).switchPlaylistVisibility(commandInput.getPlaylistId());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = ((NormalUser) user).showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode follow(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode status(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        PlayerStats stats = ((NormalUser) user).getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        ArrayList<String> songs = ((NormalUser) user).showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        String preferredGenre = ((NormalUser) user).getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return top 5 playlists
     */
    public ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return top 5 albums
     */
    public ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 artists.
     *
     * @param commandInput the command input
     * @return top 5 artists
     */
    public ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Artists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Switches connection status.
     *
     * @param commandInput the command input
     * @return objectNode
     */
    public ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (user.getType().equals(Enums.UserType.NORMAL)) {
            message = ((NormalUser) user).switchConnectionStatus();
        } else {
            message = user.getUsername() + " is not a normal user.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets online users.
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> result = new ArrayList<>();

        for (User userIter : Admin.getUsers()) {
            if (userIter.getType().equals(Enums.UserType.NORMAL)) {
                if (((NormalUser) userIter).getConnectionStatus()
                        .equals(Enums.ConnectionStatus.ONLINE)) {
                    result.add(userIter.getUsername());
                }
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(result));

        return objectNode;
    }

    /**
     * Gets all normal users.
     * @return the result
     */
    public List<String> getAllNormalUsers() {
        List<String> result = new ArrayList<>();

        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.NORMAL)) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * Gets all artists.
     * @return All artists
     */
    public List<String> getAllArtists() {
        List<String> result = new ArrayList<>();

        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.ARTIST)) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * Gets all hosts.
     * @return All hosts
     */
    public List<String> getAllHosts() {
        List<String> result = new ArrayList<>();

        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.HOST)) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * Gets all users.
     * @return All users
     */
    public ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> result = new ArrayList<>();
        result.addAll(getAllNormalUsers());
        result.addAll(getAllArtists());
        result.addAll(getAllHosts());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(result));

        return objectNode;
    }

    /**
     * Adds a user.
     * @return the object node
     */
    public ObjectNode addUser(final CommandInput commandInput) {
        String message = Admin.addUser(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Deletes user.
     * @return the output
     */
    public ObjectNode deleteUser(final CommandInput commandInput) {
        String message = Admin.deleteUser(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds album.
     * @return the output
     */
    public ObjectNode addAlbum(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.getType().equals(Enums.UserType.ARTIST)) {
            message = commandInput.getUsername() + " is not an artist.";
        } else {
            message = ((Artist) user).addAlbum(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show albums.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showAlbums(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        ArrayList<AlbumOutput> albums = ((Artist) user).showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Prints current page contents.
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String currentPage = ((NormalUser) user).printCurrentPage();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", objectMapper.valueToTree(currentPage));

        return objectNode;
    }

    /**
     * Adds new Artist event.
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addEvent(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.getType().equals(Enums.UserType.ARTIST)) {
            message = commandInput.getUsername() + " is not an artist.";
        } else {
            message = ((Artist) user).addEvent(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds new Artist merch.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addMerch(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.getType().equals(Enums.UserType.ARTIST)) {
            message = commandInput.getUsername() + " is not an artist.";
        } else {
            message = ((Artist) user).addMerch(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets all podcasts.
     * @return all podcasts
     */
    public ObjectNode addPodcast(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.getType().equals(Enums.UserType.HOST)) {
            message = commandInput.getUsername() + " is not a host.";
        } else {
            message = ((Host) user).addPodcast(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds new Host announcement.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addAnnouncement(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.getType().equals(Enums.UserType.HOST)) {
            message = commandInput.getUsername() + " is not a host.";
        } else {
            message = ((Host) user).addAnnouncement(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes Host announcement.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeAnnouncement(final CommandInput commandInput) {
        String message;
        if (!(getUser(commandInput.getUsername()).getType().equals(Enums.UserType.HOST))) {
            message = commandInput.getUsername() + " is not a host.";
        } else {
            Host host = (Host) getUser(commandInput.getUsername());
            message = host.removeAnnouncement(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showPodcasts(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        ArrayList<PodcastOutput> podcasts = ((Host) user).showPodcasts();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }


    /**
     * Changes current page.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode changePage(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (((NormalUser) user).getConnectionStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            return ((NormalUser) user).offlineStatusOutput(objectMapper, commandInput, user);
        }

        String message = ((NormalUser) user).changePage(commandInput.getNextPage());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes album.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeAlbum(final CommandInput commandInput) {
        String message;

        if (!Admin.getUsers().stream().anyMatch(iterUser
                -> iterUser.getUsername().equals(commandInput.getUsername()))) {

            message = "The username " + commandInput.getUsername() + " doesn't exist.";

        } else if (!(getUser(commandInput.getUsername()).getType().equals(Enums.UserType.ARTIST))) {
                message = commandInput.getUsername() + " is not an artist.";
        } else {
            Artist artist = (Artist) getUser(commandInput.getUsername());
            message = artist.removeAlbum(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes event.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeEvent(final CommandInput commandInput) {
        String message;

        if (Admin.getUsers().stream().noneMatch(iterUser
                -> iterUser.getUsername().equals(commandInput.getUsername()))) {

            message = commandInput.getUsername() + " doesn't exist.";

        } else if (!(getUser(commandInput.getUsername()).getType().equals(Enums.UserType.ARTIST))) {
            message = commandInput.getUsername() + " is not an artist.";

        } else {
            Artist artist = (Artist) getUser(commandInput.getUsername());
            message = artist.removeEvent(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes podcast.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removePodcast(final CommandInput commandInput) {
        String message;

        if (!Admin.getUsers().stream().anyMatch(iterUser
                -> iterUser.getUsername().equals(commandInput.getUsername()))) {

            message = commandInput.getUsername() + " doesn't exist.";

        } else if (!(getUser(commandInput.getUsername()).getType().equals(Enums.UserType.HOST))) {
            message = commandInput.getUsername() + " is not a host.";

        } else {
            Host host = (Host) getUser(commandInput.getUsername());
            message = host.removePodcast(commandInput);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
}
