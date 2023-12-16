package app.pages;

import app.audio.Collections.Album;
import app.user.ArtistFeatures.Event;
import app.user.ArtistFeatures.Merch;

import java.util.ArrayList;

public class ArtistPage extends Page {
    private ArrayList<Album> albums;
    private ArrayList<Merch> merches;
    private ArrayList<Event> events;

    public ArtistPage() {
        albums = new ArrayList<>();
        merches = new ArrayList<>();
        events = new ArrayList<>();
    }

    public ArtistPage(final ArrayList<Album> albums, final ArrayList<Merch> merches,
                      final ArrayList<Event> events) {
        this.albums = albums;
        this.merches = merches;
        this.events = events;
    }

    /**
     * Gets string of artist's albums.
     * @return string of albums
     */
    private String getAlbumsString() {
        String albumNames = "";
        for (int i = 0; i < albums.size(); i++) {
            if (i != albums.size() - 1) {
                albumNames = albumNames + albums.get(i).getName() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                albumNames = albumNames + albums.get(i).getName();
            }
        }
        return albumNames;
    }

    /**
     * Gets string of artist's merches.
     * @return string of songs
     */
    private String getMerchesString() {
        String merchesContents = "";
        for (int i = 0; i < merches.size(); i++) {
            if (i != merches.size() - 1) {
                merchesContents = merchesContents + merches.get(i).getName()
                        + " - " + merches.get(i).getPrice()
                        + ":\n\t" + merches.get(i).getDescription() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                merchesContents = merchesContents + merches.get(i).getName()
                        + " - " + merches.get(i).getPrice()
                        + ":\n\t" + merches.get(i).getDescription();
            }
        }
        return merchesContents;
    }

    /**
     * Gets string of artist's events.
     * @return string of songs
     */
    private String getEventsString() {
        String eventsContents = "";
        for (int i = 0; i < events.size(); i++) {
            if (i != events.size() - 1) {
                eventsContents = eventsContents + events.get(i).getName()
                        + " - " + events.get(i).getDate()
                        + ":\n\t" + events.get(i).getDescription() + ", ";
            } else {
                /* daca este ultimul element din array, nu se pune virgula */
                eventsContents = eventsContents + events.get(i).getName()
                        + " - " + events.get(i).getDate()
                        + ":\n\t" + events.get(i).getDescription();
            }
        }
        return eventsContents;
    }

    /**
     * extracts artist page contents
     * @return the page results
     */
    @Override
    public String getPageContents() {
        String contents;
        contents = "Albums:\n\t[" + getAlbumsString() + "]" + "\n\nMerch:\n\t["
                + getMerchesString() + "]" + "\n\nEvents:\n\t[" + getEventsString() + "]";
        return contents;
    }
}
