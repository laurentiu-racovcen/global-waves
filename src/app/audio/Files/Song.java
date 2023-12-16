package app.audio.Files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Song.
 */
@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;

    /**
     * Instantiates a new Song.
     *
     * @param name        the name
     * @param duration    the duration
     * @param album       the album
     * @param tags        the tags
     * @param lyrics      the lyrics
     * @param genre       the genre
     * @param releaseYear the release year
     * @param artist      the artist
     */
    public Song(@JsonProperty("name") final String name,
                 @JsonProperty("duration") final Integer duration,
                 @JsonProperty("album") final String album,
                 @JsonProperty("tags") final ArrayList<String> tags,
                 @JsonProperty("lyrics") final String lyrics,
                 @JsonProperty("genre") final String genre,
                 @JsonProperty("releaseYear") final Integer releaseYear,
                 @JsonProperty("artist") final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    /**
     * Checks if this song matches with the given album name.
     * @param albumName the album
     * @return result
     */
    @Override
    public boolean matchesAlbum(final String albumName) {
        return this.getAlbum().equalsIgnoreCase(albumName);
    }

    /**
     * Checks if this song matches with the given tags.
     * @param tagsList the tags
     * @return result
     */
    @Override
    public boolean matchesTags(final ArrayList<String> tagsList) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tagsList) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this song matches with the given lyrics.
     * @param lyricFilter the tags
     * @return result
     */
    @Override
    public boolean matchesLyrics(final String lyricFilter) {
        return this.getLyrics().toLowerCase().contains(lyricFilter.toLowerCase());
    }

    /**
     * Checks if this song matches with the given genre.
     * @param genreFilter the genre filter
     * @return result
     */
    @Override
    public boolean matchesGenre(final String genreFilter) {
        return this.getGenre().equalsIgnoreCase(genreFilter);
    }

    /**
     * Checks if this song matches with the given artist.
     * @param artistFilter the artist filter
     * @return result
     */
    @Override
    public boolean matchesArtist(final String artistFilter) {
        return this.getArtist().equalsIgnoreCase(artistFilter);
    }

    /**
     * Checks if this song matches with the given release year.
     * @param releaseYearFilter the release year filter
     * @return result
     */
    @Override
    public boolean matchesReleaseYear(final String releaseYearFilter) {
        return filterByYear(this.getReleaseYear(), releaseYearFilter);
    }

    /**
     * Checks if this song matches with the given year.
     * @param year the year filter
     * @return result
     */
    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * Like.
     */
    public void like() {
        likes++;
    }

    /**
     * Dislike.
     */
    public void dislike() {
        likes--;
    }
}
