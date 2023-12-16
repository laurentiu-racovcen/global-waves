package app.utils;

import app.pages.ArtistPage;
import app.pages.HostPage;
import app.pages.NormalUserPages.HomePage;
import app.pages.NormalUserPages.LikedContentPage;
import app.pages.Page;
import app.user.Artist;
import app.user.Host;
import app.user.NormalUser;
import app.user.User;

public final class Factories {
    public static class PageFactory {
        /**
         *
         * @param pageType the type of page
         * @param user page's user
         * @return requested page
         */
        public static Page createPage(final Enums.PageType pageType, final User user) {
            switch (pageType) {
                case HOMEPAGE: return new HomePage();
                case LIKED_CONTENT_PAGE: return new LikedContentPage(
                        ((NormalUser) user).getLikedSongs(),
                        ((NormalUser) user).getFollowedPlaylists());
                case ARTIST_PAGE: return new ArtistPage(((Artist) user).getAlbums(),
                        ((Artist) user).getMerches(), ((Artist) user).getEvents());
                case HOST_PAGE: return new HostPage(((Host) user).getPodcasts(),
                        ((Host) user).getAnnouncements());
                default:
            }
            throw new IllegalArgumentException("The page type " + pageType + " is not recognized.");
        }
        PageFactory() {
        }
    }

    public static class UserFactory {
        /**
         *
         * @param userType type of user
         * @param username username
         * @param age user's age
         * @param city user's city
         * @return new user instance
         */
        public static User createUser(final String userType,
                                      final String username, final int age, final String city) {
            switch (userType) {
                case "artist": return new Artist(username, age, city);
                case "host": return new Host(username, age, city);
                case "user": return new NormalUser(username, age, city);
                default:
            }
            throw new IllegalArgumentException("The user type " + userType + " is not recognized.");
        }
        UserFactory() {
        }
    }

    private Factories() {
    }
}
