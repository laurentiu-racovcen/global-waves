package app.pages.NormalUserPages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.pages.Page;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class HomePage extends Page {
    private ArrayList<Song> top5LikedSongs;
    private ArrayList<Playlist> top5FollowedPlaylists;

    public HomePage() {
        top5LikedSongs = new ArrayList<>();
        top5FollowedPlaylists = new ArrayList<>();
    }

    /**
     * Gets String of liked songs.
     * @return string of songs
     */
    private String getLikedSongsString() {
        String songsNames = "";
        for (int i = 0; i < top5LikedSongs.size(); i++) {
            if (i != top5LikedSongs.size() - 1) {
                songsNames = songsNames + top5LikedSongs.get(i).getName() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                songsNames = songsNames + top5LikedSongs.get(i).getName();
            }
        }
        return songsNames;
    }

    /**
     * Gets String of followed playlists.
     * @return string of playlists
     */
    private String getFollowedPlaylistsString() {
        String playlistsNames = "";
        for (int i = 0; i < top5FollowedPlaylists.size(); i++) {
            if (i != top5FollowedPlaylists.size() - 1) {
                playlistsNames = playlistsNames + top5FollowedPlaylists.get(i).getName() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                playlistsNames = playlistsNames + top5FollowedPlaylists.get(i).getName();
            }
        }
        return playlistsNames;
    }

    /**
     * extracts homepage contents
     * @return the page results
     */
    @Override
    public String getPageContents() {
        String contents;
        contents = "Liked songs:\n\t[" + getLikedSongsString()
                + "]" + "\n\nFollowed playlists:\n\t[" + getFollowedPlaylistsString() + "]";
        return contents;
    }
}
