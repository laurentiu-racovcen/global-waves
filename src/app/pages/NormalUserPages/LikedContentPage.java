package app.pages.NormalUserPages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.pages.Page;

import java.util.ArrayList;

public class LikedContentPage extends Page {
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;

    public LikedContentPage() {
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
    }

    public LikedContentPage(final ArrayList<Song> likedSongs,
                            final ArrayList<Playlist> followedPlaylists) {
        this.likedSongs = likedSongs;
        this.followedPlaylists = followedPlaylists;
    }

    /**
     * Gets string of liked songs.
     * @return string of songs
     */
    private String getLikedSongsString() {
        String songsContents = "";
        for (int i = 0; i < likedSongs.size(); i++) {
            if (i != likedSongs.size() - 1) {
                songsContents = songsContents + likedSongs.get(i).getName()
                        + " - " + likedSongs.get(i).getArtist() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                songsContents = songsContents + likedSongs.get(i).getName()
                        + " - " + likedSongs.get(i).getArtist();
            }
        }
        return songsContents;
    }

    /**
     * Gets string of followed playlists.
     * @return string of playlists
     */
    private String getFollowedPlaylistsString() {
        String podcastsContents = "";
        for (int i = 0; i < followedPlaylists.size(); i++) {
            if (i != followedPlaylists.size() - 1) {
                podcastsContents = podcastsContents + followedPlaylists.get(i).getName()
                        + " - " + followedPlaylists.get(i).getOwner() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                podcastsContents = podcastsContents + followedPlaylists.get(i).getName()
                        + " - " + followedPlaylists.get(i).getOwner();
            }
        }
        return podcastsContents;
    }

    /**
     * extracts Liked Content Page contents
     * @return the page results
     */
    @Override
    public String getPageContents() {
        String contents;
        contents = "Liked songs:\n\t[" + getLikedSongsString() + "]"
                + "\n\nFollowed playlists:\n\t[" + getFollowedPlaylistsString() + "]";
        return contents;
    }
}
