package app.page.NormalUserPages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.page.Page;
import app.player.Player;

import java.util.ArrayList;

public class LikedContentPage extends Page {
    ArrayList<Song> likedSongs;
    ArrayList<Playlist> followedPlaylists;

    public LikedContentPage() {
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
    }

    public LikedContentPage(ArrayList<Song> likedSongs, ArrayList<Playlist> followedPlaylists){
        this.likedSongs = likedSongs;
        this.followedPlaylists = followedPlaylists;
    }

    private String getLikedSongsString () {
        String songsContents = "";
        for (int i = 0; i < likedSongs.size(); i++) {
            if (i != likedSongs.size()-1) {
                songsContents = songsContents + likedSongs.get(i).getName() +
                        " - " + likedSongs.get(i).getArtist() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                songsContents = songsContents + likedSongs.get(i).getName() +
                        " - " + likedSongs.get(i).getArtist() + "]";
            }
        }
        return songsContents;
    }

    private String getFollowedPlaylistsString () {
        String podcastsContents = "";
        for (int i = 0; i < followedPlaylists.size(); i++) {
            if (i != followedPlaylists.size()-1) {
                podcastsContents = podcastsContents + followedPlaylists.get(i).getName() +
                        " - " + followedPlaylists.get(i).getOwner() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                podcastsContents = podcastsContents + likedSongs.get(i).getName() +
                        " - " + followedPlaylists.get(i).getOwner() + "]";
            }
        }
        return podcastsContents;
    }

    @Override
    public String getPageContents() {
        String contents;
        contents = "Liked Songs:\n\t[" + getLikedSongsString() +
                "\n\nFollowed Playlists:\n\t[" + getFollowedPlaylistsString();
        return contents;
    }
}
