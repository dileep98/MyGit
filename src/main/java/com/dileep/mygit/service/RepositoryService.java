package com.dileep.mygit.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;



public class RepositoryService {

    public static void initRepo() {
        File repoDir = new File(".mygit");
        if (repoDir.exists()) {
            System.out.println("Repository already initialized.");
            return;
        }
        boolean created = repoDir.mkdir();
        if (created) {
            // Create necessary directories
            new File(".mygit/commits").mkdir();
            new File(".mygit/branches").mkdir();
            new File(".mygit/staging").mkdir();

            // Create HEAD file pointing to the master branch
            File head = new File(".mygit/HEAD");
            try {
                Files.write(head.toPath(), "master".getBytes());
                System.out.println("Initialized empty repository in .mygit");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to initialize repository.");
        }
    }

    public static void stageFile(String filePath) {
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        File stagingDir = new File(".mygit/staging");
        File destinationFile = new File(stagingDir, sourceFile.getName());

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Update the index to track the staged file
            File indexFile = new File(".mygit/index");
            try (FileWriter writer = new FileWriter(indexFile, true)) {
                writer.write(filePath + "\n");
            }
            System.out.println("Staged file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to stage file: " + filePath);
        }
    }

    public static void commit(String message) {
        File stagingDir = new File(".mygit/staging");
        if (stagingDir.list().length == 0) {
            System.out.println("No files staged for commit.");
            return;
        }

        // Create a new commit directory with a unique commit ID
        String commitHash = UUID.randomUUID().toString();
        File commitDir = new File(".mygit/commits/" + commitHash);
        commitDir.mkdir();

        // Move staged files to the commit directory
        for (File file : stagingDir.listFiles()) {
            file.renameTo(new File(commitDir, file.getName()));
        }

        // Write commit metadata (message, timestamp)
        try (FileWriter commitFile = new FileWriter(new File(commitDir, "commit.txt"))) {
            commitFile.write("Commit Message: " + message + "\n");
            commitFile.write("Timestamp: " + LocalDateTime.now() + "\n");

            // Store the parent commit (if exists)
            File head = new File(".mygit/HEAD");
            String parentCommit = Files.readString(head.toPath()).trim();
            commitFile.write("Parent Commit: " + parentCommit + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update HEAD to point to this new commit
        try {
            Files.write(new File(".mygit/HEAD").toPath(), commitHash.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clean up the staging area
        for (File file : stagingDir.listFiles()) {
            file.delete();
        }

        System.out.println("Committed with message: " + message);
    }


    public static void log() {
        File commitDir = new File(".mygit/commits");
        File[] commits = commitDir.listFiles();
        if (commits == null || commits.length == 0) {
            System.out.println("No commits found.");
            return;
        }

        for (File commit : commits) {
            File commitFile = new File(commit, "commit.txt");
            try {
                String commitDetails = Files.readString(commitFile.toPath());
                System.out.println("Commit ID: " + commit.getName());
                System.out.println(commitDetails);
                System.out.println("------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void branch(String branchName) {
        File branchFile = new File(".mygit/branches/" + branchName);
        if (branchFile.exists()) {
            System.out.println("Branch already exists.");
            return;
        }

        try {
            // Point the new branch to the current commit in HEAD
            String currentCommit = Files.readString(new File(".mygit/HEAD").toPath());
            Files.write(branchFile.toPath(), currentCommit.getBytes());
            System.out.println("Branch created: " + branchName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void merge(String branchName) {
        File branchFile = new File(".mygit/branches/" + branchName);
        if (!branchFile.exists()) {
            System.out.println("Branch does not exist.");
            return;
        }

        try {
            String currentCommit = Files.readString(new File(".mygit/HEAD").toPath());
            String targetCommit = Files.readString(branchFile.toPath());

            // Simple merge logic for now, you can expand it later for conflict resolution
            if (currentCommit.equals(targetCommit)) {
                System.out.println("Both branches are at the same commit. Nothing to merge.");
                return;
            }

            // Apply target branch changes to the current branch
            File targetCommitDir = new File(".mygit/commits/" + targetCommit.trim());
            File[] targetFiles = targetCommitDir.listFiles();

            if (targetFiles != null) {
                for (File file : targetFiles) {
                    File destinationFile = new File(".mygit/staging/" + file.getName());
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Merged file: " + file.getName());
                }
            }

            System.out.println("Merge completed. Please commit the changes.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void status() {
        File stagingDir = new File(".mygit/staging");
        File[] stagedFiles = stagingDir.listFiles();

        System.out.println("Staged for commit:");
        if (stagedFiles != null && stagedFiles.length > 0) {
            for (File file : stagedFiles) {
                System.out.println("  " + file.getName());
            }
        } else {
            System.out.println("  No files staged.");
        }

        System.out.println("\nModified but not staged:");
        try {
            File workingDir = new File(".");
            File[] workingFiles = workingDir.listFiles();
            if (workingFiles != null) {
                for (File file : workingFiles) {
                    if (!file.isDirectory() && !isFileStaged(file)) {
                        Path filePath = Paths.get(file.getPath());
                        Path stagedPath = Paths.get(".mygit/staging", file.getName());

                        if (Files.exists(stagedPath) && !(Files.mismatch(filePath, stagedPath) == -1L)) {
                            System.out.println("  " + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isFileStaged(File file) {
        File stagedFile = new File(".mygit/staging", file.getName());
        return stagedFile.exists();
    }

    public static void diff() {
        File stagingDir = new File(".mygit/staging");
        File[] stagedFiles = stagingDir.listFiles();

        if (stagedFiles != null) {
            for (File file : stagedFiles) {
                try {
                    File workingFile = new File(file.getName());
                    List<String> workingFileLines = Files.readAllLines(workingFile.toPath());
                    List<String> stagedFileLines = Files.readAllLines(file.toPath());

                    System.out.println("Diff for file: " + file.getName());
                    for (int i = 0; i < Math.max(workingFileLines.size(), stagedFileLines.size()); i++) {
                        if (i < workingFileLines.size() && i < stagedFileLines.size()) {
                            if (!workingFileLines.get(i).equals(stagedFileLines.get(i))) {
                                System.out.println("- " + stagedFileLines.get(i));
                                System.out.println("+ " + workingFileLines.get(i));
                            }
                        } else if (i < workingFileLines.size()) {
                            System.out.println("+ " + workingFileLines.get(i));
                        } else {
                            System.out.println("- " + stagedFileLines.get(i));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No files staged for diff.");
        }
    }




}
