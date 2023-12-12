package app.utils;

import app.audio.Collections.Album;
import app.page.ArtistPage;
import app.page.HostPage;
import app.page.NormalUserPages.HomePage;
import app.page.NormalUserPages.LikedContentPage;
import app.page.Page;
import app.user.Artist;
import app.user.ArtistFeatures.Event;
import app.user.ArtistFeatures.Merch;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;

import java.util.ArrayList;

public class Factories {
    public class PageFactory {
        public static Page createPage(Enums.PageType pageType, User user) {
            switch (pageType) {
                case HOMEPAGE: return new HomePage(((NormalUser)user).getTop5LikedSongs(),
                        ((NormalUser)user).getTop5FollowedPlaylists());
                case LIKED_CONTENT_PAGE: return new LikedContentPage(
                        ((NormalUser)user).getLikedSongs(),
                        ((NormalUser)user).getFollowedPlaylists());
                case ARTIST_PAGE: return new ArtistPage(((Artist)user).getAlbums(),
                        ((Artist)user).getMerches(), ((Artist)user).getEvents());
                case HOST_PAGE: return new HostPage(((Host)user).getPodcasts(),
                        ((Host)user).getAnnouncements());
            }
            throw new IllegalArgumentException("The page type " + pageType + " is not recognized.");
        }
    }
}
