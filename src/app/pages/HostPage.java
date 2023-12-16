package app.pages;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.HostFeatures.Announcement;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class HostPage extends Page {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;

    public HostPage() {
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    public HostPage(final ArrayList<Podcast> podcasts,
                    final ArrayList<Announcement> announcements) {
        this.podcasts = podcasts;
        this.announcements = announcements;
    }

    /**
     * Gets string of podcast episodes.
     * @return string of episodes
     */
    private String getEpisodesString(final ArrayList<Episode> episodes) {
        String episodesContents = "";
        for (int i = 0; i < episodes.size(); i++) {
            if (i != episodes.size() - 1) {
                episodesContents = episodesContents + episodes.get(i).getName()
                        + " - " + episodes.get(i).getDescription() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                episodesContents = episodesContents + episodes.get(i).getName()
                        + " - " + episodes.get(i).getDescription() + "]\n";
            }
        }
        return episodesContents;
    }

    /**
     * Gets string of host's podcasts.
     * @return string of podcasts
     */
    private String getPodcastsString() {
        String podcastsContents = "";
        for (int i = 0; i < podcasts.size(); i++) {
            if (i != podcasts.size() - 1) {
                podcastsContents = podcastsContents + podcasts.get(i).getName() + ":\n\t["
                   + getEpisodesString((ArrayList<Episode>) podcasts.get(i).getEpisodes()) + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                podcastsContents = podcastsContents + podcasts.get(i).getName() + ":\n\t["
                   + getEpisodesString((ArrayList<Episode>) podcasts.get(i).getEpisodes());
            }
        }
        return podcastsContents;
    }

    /**
     * Gets string of host's announcements.
     * @return string of announcements
     */
    private String getAnnouncementsString() {
        String announcementsContents = "";
        for (int i = 0; i < announcements.size(); i++) {
            if (i != announcements.size() - 1) {
                announcementsContents = announcementsContents + announcements.get(i).getName()
                        + "\n\t" + announcements.get(i).getDescription() + "\n";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                announcementsContents = announcementsContents + announcements.get(i).getName()
                        + ":\n\t" + announcements.get(i).getDescription() + "\n";
            }
        }
        return announcementsContents;
    }

    /**
     * extracts host page contents
     * @return the page results
     */
    @Override
    public String getPageContents() {
        String contents;
        contents = "Podcasts:\n\t[" + getPodcastsString() + "]"
                + "\n\nAnnouncements:\n\t[" + getAnnouncementsString() + "]";
        return contents;
    }
}
