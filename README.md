# **Global Waves**

>This project consists in implementing the simulation of an Audio Streaming Platform.
Through it, various audio files can be selected and "listened".

## **Table of contents**

1. ["main" method](#main-method)
2. [Added classes](#added-classes)
3. [Class hierarchies](#class-hierarchies)
4. [Interaction between classes](#interaction-between-classes)
5. [General statistics](#general-statistics)

## **"MAIN" method**

**1.** The input data is read from the library file of `Library` type;

**2.** Executes each command in the input file;

## **Added classes**

- `"Artist"` - class containing the characteristic data of an artist:
   - Fields: `albums`, `merches`, `events`, `pagina caracteristică unui artist`;
   - Methods: `AddAlbum`, `RemoveAlbum`, `AddEvent`, `RemoveEvent`, `AddMerch`;

- `"Host"` - class containing the characteristic data of a host:
   - Fields: `podcasts`, `announcements`, `pagina caracteristică unui host`;
   - Methods: `AddPodcast`, `RemovePodcast`, `AddAnnouncement`, `RemoveAnnouncement`;

- `"Album"` - class containing album-specific data:
   - Fields: `songs`, `releaseYear`, `description`;
   - Methods: `addSong`, `removeSong`, `getNumberOfTracks`, `getTrackByIndex`, `getAlbumLikes` ,`isAlbumInteracted`;

- `"Page"` - abstract class that is inherited by the classes:
   - `HomePage` - page belonging to a normal user, containing the fields: `top5LikedSongs`, `top5FollowedPlaylists`;
   - `LikedContentPage` - page belonging to a normal user, containing the fields: `likedSongs`, `releaseYear`;
   - `ArtistPage` - page belonging to an artist, containing the fields: `albums`, `merches`, `events`;
   - `HostPage` - page belonging to a host, containing the fields: `podcasts`, `announcements`;

## **Class hierarchies**
- The `User` class is extended by the `NormalUser`, `Host` and `Artist` classes.
- The `Page` class is extended by the classes `HomePage`, `LikedContentPage`, `ArtistPage` and `HostPage`.
- The `ArtistFeature` class is extended by the `Event` and `Merch` classes.
- The `HostFeature` class is extended by the `Announcement` class.

## **Interaction between classes**
>When the program receives a command, it is processed by the single instance of commandRunner.
Depending on the command, commandRunner interacts with the other classes to fulfill the requirement.

Here are some cases of processing and fulfilling the commands:

1) `addUser`: When the commandRunner receives this command, it calls the `"AddUser"` method of the `"Admin"` class. Then, in this method, depending on the read type of the user, a corresponding instance is created, the reference of which is kept in the admin's `users` list.

2) `printCurrentPage`: Here, commandRunner calls the `printCurrentPage` method of the `NormalUser` class, where the `currentPage.getPageContents()` method, defined in the `Page` abstract class, is called. This is a method common to all page types, each of which has its own implementation of it. Thus, when the method is called, an implementation based on the page type will be chosen and only that page's information will be obtained.

3) `deleteUser`: commandRunner calls the `Admin.deleteUser` method, where the user type check takes place. Depending on the type, one of the methods: `isArtistInteractedBy`, `isHostInteractedBy`, `isNormalUserInteractedBy` is called, which checks if user-owned objects are interacted with by others. If they are interacted with, deleting the user will fail, but if they are not interacted with, the user will be deleted successfully.

## **General statistics**

1) `getTop5Albums`: Here, commandRunner calls the `Admin.getTop5Albums` method, which will then iterate through all the albums in the library and return a list of albums sorted by the total number of likes of the songs in the album.

2) `getTop5Artists`: the `Admin.getTop5Artists` method is called, which will then loop through all users' data and pick the top 5 artists based on the total number of likes of their albums.

3) `getAllUsers`: commandRunner calls `getAllNormalUsers()`, `getAllArtists()` and `getAllHosts()` and puts the results into a list for display.

3) `getOnlineUsers`: commandRunner calls the `getOnlineUsers` method of its class and iterates through the list of all users, putting the results into a list in order for them to be displayed.
