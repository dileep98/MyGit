package com.dileep.mygit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class MyGitApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to MyGit!");
        while (true) {
            System.out.print("mygit> ");
            String command = scanner.nextLine().trim();
            String[] commandParts = command.split("\\s+");

            switch (commandParts[0].toLowerCase()) {
                case "init":
                    initRepo();
                    break;
                case "add":
                    if (commandParts.length > 1) {
                        stageFile(commandParts[1]);
                    } else {
                        System.out.println("Usage: add <file>");
                    }
                    break;
                case "commit":
                    if (commandParts.length > 1) {
                        commit(commandParts[1]);
                    } else {
                        System.out.println("Usage: commit <message>");
                    }
                    break;
                case "exit":
                    System.out.println("Exiting MyGit.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command.");
                    break;
            }
        }
    }

    private static void initRepo() {
        File repoDir = new File(".mygit");
        if (repoDir.exists()) {
            System.out.println("Repository already initialized.");
        } else {
            boolean created = repoDir.mkdir();
            if (created) {
                new File(".mygit/commits").mkdir();
                new File(".mygit/branches").mkdir();
                new File(".mygit/staging").mkdir();
                System.out.println("Initialized empty repository in .mygit");
            } else {
                System.out.println("Failed to initialize repository.");
            }
        }
        System.out.println("Repository initialized.");
    }

    private static void stageFile(String filePath) {
        File sourceFile = new File(filePath);
        File stagingDir = new File(".mygit/staging");

        if (!sourceFile.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try {
            Files.copy(sourceFile.toPath(), new File(stagingDir, sourceFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Staged file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to stage file: " + filePath);
        }
    }

    private static void commit(String message) {
        File stagingDir = new File(".mygit/staging");
        if (stagingDir.list().length == 0) {
            System.out.println("No files staged for commit.");
            return;
        }

        String commitId = UUID.randomUUID().toString();
        File commitDir = new File(".mygit/commits/" + commitId);
        commitDir.mkdir();

        // Move staged files to the new commit directory
        for (File file : stagingDir.listFiles()) {
            file.renameTo(new File(commitDir, file.getName()));
        }

        // Save commit metadata
        try (FileWriter commitFile = new FileWriter(new File(commitDir, "commit.txt"))) {
            commitFile.write("Commit Message: " + message + "\n");
            commitFile.write("Timestamp: " + LocalDateTime.now().toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Committed with message: " + message);
    }
}
