package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.NormalUser;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;


/**
 * The type Album.
 */

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs;
    private int releaseYear;
    private String description;

    /**
     * Instantiates a new Album.
     *
     * @param name  the name
     * @param owner the artist
     */
    public Album(final String name, final String owner,
                 final int releaseYear, final String description) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.releaseYear = releaseYear;
        this.description = description;
    }

    /**
     * Contains song boolean.
     *
     * @param song the song
     * @return the boolean
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * Add song.
     *
     * @param song the song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Remove song.
     *
     * @param song the song
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * Remove song.
     *
     * @param index the index
     */
    public void removeSong(final int index) {
        songs.remove(index);
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Checks if album is interacted.
     *
     * @return the result
     */
    public boolean isAlbumInteracted() {
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.NORMAL)
                    && ((NormalUser) user).getConnectionStatus()
                        .equals(Enums.ConnectionStatus.ONLINE)
                    && ((NormalUser) user).interactsWithAlbum(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets album likes.
     *
     * @return the number of likes
     */
    public Integer getAlbumLikes() {
        Integer likes = 0;
        for (Song song : songs) {
            likes += song.getLikes();
        }
        return likes;
    }

}
