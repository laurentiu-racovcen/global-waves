package app.utils;

public class Enums {
    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONCE, REPEAT_INFINITE, REPEAT_CURRENT_SONG, NO_REPEAT,
    }

    public enum PlayerSourceType {
        LIBRARY, PLAYLIST, PODCAST, ALBUM
    }

    public enum ConnectionStatus {
        ONLINE,
        OFFLINE
    }

    public enum UserType {
        NORMAL,
        HOST,
        ARTIST
    }

    public enum PageType {
        HOMEPAGE,
        LIKED_CONTENT_PAGE,
        ARTIST_PAGE,
        HOST_PAGE
    }
}
