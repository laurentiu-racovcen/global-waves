# **Proiect Global Waves - Etapa 2**

>În cadrul acestei teme, am continuat implementarea unui Audio Player.
Prin intermediul acestuia, pot fi selectate diverse fișiere audio, care pot fi ascultate.



    Notă: Am folosit ca schelet rezolvarea oficială a etapei I.


## **CUPRINS**

1. [Metoda "main"](#metoda-main)
2. [Clase noi față de prima etapă](#clase-noi-față-de-prima-etapă)
3. [Ierarhiile de clase](#ierarhiile-de-clase)
4. [Interacțiunea dintre clase](#interacțiunea-dintre-clase)
5. [Statistici generale](#statistici-generale)

## **Metoda "MAIN"**

**1.** Se citesc datele de intrare din fișierul cu biblioteca de tip `Library`;

**2.** Se execută, pe rând, fiecare comandă din fișierul de intrare;

## **Clase noi față de prima etapă**

- `"Artist"` - clasă care conține datele caracteristice unui artist:
   - Câmpuri: `albums`, `merches`, `events`, `pagina caracteristică unui artist`;
   - Metode: `AddAlbum`, `RemoveAlbum`, `AddEvent`, `RemoveEvent`, `AddMerch`;

- `"Host"` - clasă care conține datele caracteristice unui host:
   - Câmpuri: `podcasts`, `announcements`, `pagina caracteristică unui host`;
   - Metode: `AddPodcast`, `RemovePodcast`, `AddAnnouncement`, `RemoveAnnouncement`;

- `"Album"` - clasă care conține datele specifice unui album:
   - Câmpuri: `songs`, `releaseYear`, `description`;
   - Metode: `addSong`, `removeSong`, `getNumberOfTracks`, `getTrackByIndex`, `getAlbumLikes` ,`isAlbumInteracted`;

- `"Page"` - clasă abstractă care este moștenită de către clasele:
   - `HomePage` - pagină care aparține unui utilizator normal, ce conține câmpurile: `top5LikedSongs`, `top5FollowedPlaylists`;
   - `LikedContentPage` - pagină care aparține unui utilizator normal, ce conține câmpurile: `likedSongs`, `releaseYear`;
   - `ArtistPage` - pagină care aparține unui artist, ce conține câmpurile: `albums`, `merches`, `events`;
   - `HostPage` - pagină care aparține unui host, ce conține câmpurile: `podcasts`, `announcements`;   

## **Ierarhiile de clase**
- Clasa `User` este extinsă de către clasele `NormalUser`, `Host` și `Artist`.
- Clasa `Page` este extinsă de către clasele `HomePage`, `LikedContentPage`, `ArtistPage` și `HostPage`.
- Clasa `ArtistFeature` este extinsă de către clasele `Event` și `Merch`.
- Clasa `HostFeature` este extinsă de către clasa `Announcement`.

## **Interacțiunea dintre clase**
>În momentul în care programul primește o comandă, aceasta este procesată de către instanța unică de commandRunner.
În funcție de comandă, commandRunner interacționează cu celelalte clase pentru a îndeplini cerința.

Acestea sunt unele cazuri de procesare a comenzilor:

În continuare voi prezenta câteva cazuri de procesare și îndeplinire a unor comenzi:

1) `addUser`: În momentul când commandRunner primește această comandă, acesta apelează metoda `"AddUser"` din cadrul clasei `Admin`. Apoi, în această metodă, în funcție de tipul citit al utilizatorului, se creează o instanță corespunzătoare, a cărei referință se păstrează în lista `users` a admin-ului.

2) `printCurrentPage`: Aici, commandRunner apelează metoda `printCurrentPage` din clasa `NormalUser`, unde se apelează metoda `currentPage.getPageContents()`, definită în clasa abstractă `Page`. Aceasta este o metodă comună a tuturor tipurilor de pagini, fiecare din ele având o implementare proprie a acesteia. Astfel, când metoda este apelată, se va alege o implementare în funcție de tipul paginii și se vor obține doar informațiile paginii respective.

3) `deleteUser`: commandRunner apelează metoda `Admin.deleteUser`, unde are loc verificarea tipului utilizatorului. În funcție de tip, se apelează una din metodele: `isArtistInteractedBy`, `isHostInteractedBy`, `isNormalUserInteractedBy`, care verifică dacă obiectele deținute de utilizator sunt interacționate de către alții. Dacă sunt interacționate, ștergerea utilizatorului va eșua, dar dacă nu sunt interacționate, utilizatorul va fi șters cu success.


## **Statistici generale**

1) `getTop5Albums`: Aici, commandRunner apelează metoda `Admin.getTop5Albums`, care ulterior va itera prin toate albumele din bibliotecă și va returna o listă de albume sortată după numărul total de like-uri ale melodiilor din album.

2) `getTop5Artists`: se apelează metoda `Admin.getTop5Artists`, care apoi va parcurge datele tuturor utilizatorilor și va alege primii 5 artiști în funcție de numărul total de like-uri ale albumelor lor.

3) `getAllUsers`: commandRunner apelează `getAllNormalUsers()`, `getAllArtists()` și `getAllHosts()` și pune rezultatele obținute într-o listă pentru a fi afișate.

3) `getOnlineUsers`: commandRunner apelează metoda `getOnlineUsers` din cadrul clasei sale și iterează prin lista tuturor utilizatorilor, punând rezultatele obținute într-o listă pentru a fi ulterior afișate.



​

    © Racovcen Laurențiu, 325CD
