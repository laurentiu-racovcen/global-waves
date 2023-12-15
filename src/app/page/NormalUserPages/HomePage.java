package app.page.NormalUserPages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.page.Page;
import lombok.Getter;

import java.util.ArrayList;

public class HomePage extends Page {
    @Getter
    ArrayList<Song> top5LikedSongs;
    @Getter
    ArrayList<Playlist> top5FollowedPlaylists;

    public HomePage() {
        top5LikedSongs = new ArrayList<>();
        top5FollowedPlaylists = new ArrayList<>();
    }

    private String getLikedSongsString () {
        String songsNames = "";
        for (int i = 0; i < top5LikedSongs.size(); i++) {
            if (i != top5LikedSongs.size()-1) {
                songsNames = songsNames + top5LikedSongs.get(i).getName() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                songsNames = songsNames + top5LikedSongs.get(i).getName();
            }
        }
        return songsNames;
    }

    private String getFollowedPlaylistsString () {
        String playlistsNames = "";
        for (int i = 0; i < top5FollowedPlaylists.size(); i++) {
            if (i != top5FollowedPlaylists.size()-1) {
                playlistsNames = playlistsNames + top5FollowedPlaylists.get(i).getName() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                playlistsNames = playlistsNames + top5FollowedPlaylists.get(i).getName();
            }
        }
        return playlistsNames;
    }

    @Override
    public String getPageContents() {
        String contents;
        contents = "Liked songs:\n\t[" + getLikedSongsString() +
                "]" + "\n\nFollowed playlists:\n\t[" + getFollowedPlaylistsString() + "]";
        return contents;
    }
}
