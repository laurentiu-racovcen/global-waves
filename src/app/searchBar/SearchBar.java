package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> audioResults;
    private List<User> creatorsResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelectedAudio;

    @Getter
    private User lastSelectedCreator;

    public void setLastSearchType(String lastSearchType) {
        this.lastSearchType = lastSearchType;
    }

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.audioResults = new ArrayList<>();
        this.creatorsResults = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelectedAudio = null;
        lastSelectedCreator = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        List<LibraryEntry> audioEntries = new ArrayList<>();
        List<User> creatorsEntries = new ArrayList<>();

        switch (type) {
            case "song":
                audioEntries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    audioEntries = filterByName(audioEntries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    audioEntries = filterByAlbum(audioEntries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    audioEntries = filterByTags(audioEntries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    audioEntries = filterByLyrics(audioEntries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    audioEntries = filterByGenre(audioEntries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    audioEntries = filterByReleaseYear(audioEntries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    audioEntries = filterByArtist(audioEntries, filters.getArtist());
                }

                this.audioResults = audioEntries;

                break;
            case "playlist":
                audioEntries = new ArrayList<>(Admin.getPlaylists());

                audioEntries = filterByPlaylistVisibility(audioEntries, user);

                if (filters.getName() != null) {
                    audioEntries = filterByName(audioEntries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    audioEntries = filterByOwner(audioEntries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    audioEntries = filterByFollowers(audioEntries, filters.getFollowers());
                }

                this.audioResults = audioEntries;

                break;
            case "podcast":
                audioEntries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    audioEntries = filterByName(audioEntries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    audioEntries = filterByOwner(audioEntries, filters.getOwner());
                }

                this.audioResults = audioEntries;

                break;
            case "album":
                audioEntries = new ArrayList<>(Admin.getAlbums());

                if (filters.getName() != null) {
                    audioEntries = filterByName(audioEntries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    audioEntries = filterByOwner(audioEntries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    audioEntries = filterByDescription(audioEntries, filters.getDescription());
                }

                this.audioResults = audioEntries;

                break;
            case "artist":
            case "host":
                creatorsEntries = new ArrayList<>(Admin.getUsers());

                if (filters.getName() != null) {
                    creatorsEntries = filterCreatorsByName(creatorsEntries, filters.getName(), type);
                }
                this.creatorsResults = creatorsEntries;

                break;

            default:
        }

        ArrayList<String> results = new ArrayList<>();

        if (!type.equals("artist") && !type.equals("host")) {
            lastSelectedCreator = null;

            while (audioEntries.size() > MAX_RESULTS) {
                audioEntries.remove(audioEntries.size() - 1);
            }

            for (LibraryEntry entry : audioEntries) {
                results.add(entry.getName());
            }
        } else {
            /* tipul este Artist sau Host */
            lastSelectedAudio = null;

            while (creatorsEntries.size() > MAX_RESULTS) {
                creatorsEntries.remove(creatorsEntries.size() - 1);
            }

            for (User entry : creatorsEntries) {
                results.add(entry.getUsername());
            }
        }

        this.lastSearchType = type;
        return results;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public String select(final Integer itemNumber) {
        /* daca e un array de artisti/hosts */
        if (this.lastSearchType.equals("artist") || this.lastSearchType.equals("host")) {
            if (this.creatorsResults.size() < itemNumber) {
                creatorsResults.clear();

                return null;
            } else {
                lastSelectedCreator = this.creatorsResults.get(itemNumber - 1);
                creatorsResults.clear();

                return lastSelectedCreator.getUsername();
            }
        }

        /* daca e un array de melodii sau un array de colectii de melodii */
        if (this.audioResults.size() < itemNumber) {
            audioResults.clear();

            return null;
        } else {
            lastSelectedAudio =  this.audioResults.get(itemNumber - 1);
            audioResults.clear();

            return lastSelectedAudio.getName();
        }
    }

    // TODO JAVADOC HERE
    public boolean SearchesCreator(User searchedCreator) {
        for (User user : creatorsResults) {
            if (user.getUsername().equals(searchedCreator.getUsername())) {
                return true;
            }
        }
        return false;
    }
}
