package main;

import app.Admin;
import app.CommandRunner;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                               + "library/library.json"),
                                                               LibraryInput.class);
        CommandInput[] commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                                  + filePath1),
                                                                  CommandInput[].class);
        ArrayNode outputs = objectMapper.createArrayNode();

        Admin.setUsers(library.getUsers());
        Admin.setSongs(library.getSongs());
        Admin.setPodcasts(library.getPodcasts());

        CommandRunner commandRunner = CommandRunner.getInstance();

        for (CommandInput command : commands) {
            Admin.updateTimestamp(command.getTimestamp());

            String commandName = command.getCommand();

            switch (commandName) {
                case "search" -> outputs.add(commandRunner.search(command));
                case "select" -> outputs.add(commandRunner.select(command));
                case "load" -> outputs.add(commandRunner.load(command));
                case "playPause" -> outputs.add(commandRunner.playPause(command));
                case "repeat" -> outputs.add(commandRunner.repeat(command));
                case "shuffle" -> outputs.add(commandRunner.shuffle(command));
                case "forward" -> outputs.add(commandRunner.forward(command));
                case "backward" -> outputs.add(commandRunner.backward(command));
                case "like" -> outputs.add(commandRunner.like(command));
                case "next" -> outputs.add(commandRunner.next(command));
                case "prev" -> outputs.add(commandRunner.prev(command));
                case "createPlaylist" -> outputs.add(commandRunner.createPlaylist(command));
                case "addRemoveInPlaylist" ->
                        outputs.add(commandRunner.addRemoveInPlaylist(command));
                case "switchVisibility" -> outputs.add(commandRunner.switchVisibility(command));
                case "showPlaylists" -> outputs.add(commandRunner.showPlaylists(command));
                case "follow" -> outputs.add(commandRunner.follow(command));
                case "status" -> outputs.add(commandRunner.status(command));
                case "showPreferredSongs" -> outputs.add(commandRunner.showLikedSongs(command));
                case "getPreferredGenre" -> outputs.add(commandRunner.getPreferredGenre(command));
                case "getTop5Songs" -> outputs.add(commandRunner.getTop5Songs(command));
                case "getTop5Playlists" -> outputs.add(commandRunner.getTop5Playlists(command));
                case "switchConnectionStatus" ->
                        outputs.add(commandRunner.switchConnectionStatus(command));
                case "addUser" -> outputs.add(commandRunner.addUser(command));
                case "addEvent" -> outputs.add(commandRunner.addEvent(command));
                case "addMerch" -> outputs.add(commandRunner.addMerch(command));
                case "printCurrentPage" -> outputs.add(commandRunner.printCurrentPage(command));
                case "addPodcast" -> outputs.add(commandRunner.addPodcast(command));
                case "addAnnouncement" -> outputs.add(commandRunner.addAnnouncement(command));
                case "removeAnnouncement" -> outputs.add(commandRunner.removeAnnouncement(command));
                case "showPodcasts" -> outputs.add(commandRunner.showPodcasts(command));
                case "changePage" -> outputs.add(commandRunner.changePage(command));
                case "deleteUser" -> outputs.add(commandRunner.deleteUser(command));
                case "addAlbum" -> outputs.add(commandRunner.addAlbum(command));
                case "showAlbums" -> outputs.add(commandRunner.showAlbums(command));
                case "removeAlbum" -> outputs.add(commandRunner.removeAlbum(command));
                case "removePodcast" -> outputs.add(commandRunner.removePodcast(command));
                case "removeEvent" -> outputs.add(commandRunner.removeEvent(command));
                case "getOnlineUsers" -> outputs.add(commandRunner.getOnlineUsers(command));
                case "getAllUsers" -> outputs.add(commandRunner.getAllUsers(command));
                case "getTop5Albums" -> outputs.add(commandRunner.getTop5Albums(command));
                case "getTop5Artists" -> outputs.add(commandRunner.getTop5Artists(command));
                default -> System.out.println("Invalid command " + commandName);
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        Admin.reset();
    }
}
