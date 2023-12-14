package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;
import app.utils.Enums;

import java.util.List;

import static app.user.User.NormalUserInteractsWithAlbum;
import static app.user.User.NormalUserInteractsWithPodcast;

public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    /**
     * Add episode.
     *
     * @param episode the episode
     */
    public void addEpisode(final Episode episode) {
        episodes.add(episode);
    }

    public boolean IsPodcastInteracted() {
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(Enums.UserType.NORMAL)) {
                if (((NormalUser)user).getConnectionStatus().equals(Enums.ConnectionStatus.ONLINE)) {
                    if (NormalUserInteractsWithPodcast((NormalUser) user, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // TODO JAVADOC + hide if-for-if
    public static void deletePodcastFromLibrary(Podcast podcast, Host host) {

        for (int i = 0; i < host.getPodcasts().size(); i++) {
            if (host.getPodcasts().get(i).getName().equals(podcast.getName())) {
                host.getPodcasts().remove(i);
                break;
            }
        }

        for (int i = 0; i < Admin.getPodcasts().size(); i++) {
            if (Admin.getPodcasts().get(i).getName().equals(podcast.getName())) {
                Admin.getPodcasts().remove(i);
                break;
            }
        }

    }

}
