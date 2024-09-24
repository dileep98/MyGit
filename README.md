# MyGit - A Simple Version Control System

MyGit is a custom-built, simplified version control system inspired by Git. It allows users to create, track, and manage file changes within a project directory, providing core functionality like initializing a repository, staging files, committing changes, branching, and logging commits.

## Features

- **Initialize Repository (`init`)**: Set up a new repository with a `.mygit/` directory that stores all metadata.
- **Add Files (`add`)**: Stage files by copying them to the staging area, preparing them for the next commit.
- **Commit Changes (`commit`)**: Save a snapshot of staged files with a message and timestamp.
- **Branching (`branch`)**: Create new branches and switch between them using `checkout`.
- **Commit History (`log`)**: View the history of commits, including commit messages and timestamps.
- **Merge Branches (`merge`)**: Combine changes from one branch into another (basic merge).
- **Status (`status`)**: Check the state of your repository, including staged and modified files.
- **File Comparison (`diff`)**: Show differences between staged and working files.

## Getting Started

### Prerequisites

- **Java 17**: Ensure that Java 17 or later is installed on your system.
- **Maven**: Install Apache Maven for building and running the project.

### Setup Instructions

1. Clone the repository to your local machine:

    ```bash
    git clone https://github.com/yourusername/mygit.git
    cd mygit
    ```

2. Build the project using Maven:

    ```bash
    mvn clean compile
    ```

3. Run the project using the Maven exec plugin:

    ```bash
    mvn exec:java
    ```
   If this doesn't work. Try running using run option in IntelliJ

### Commands Overview

1. **Initialize a Repository (`init`)**:
    ```bash
    mygit> init
    ```
   This command initializes a new repository by creating a `.mygit` directory with subdirectories for commits, branches, and the staging area.

2. **Add Files to Staging (`add`)**:
    ```bash
    mygit> add <file>
    ```
   Stages the specified file for the next commit.

3. **Commit Changes (`commit`)**:
    ```bash
    mygit> commit "<message>"
    ```
   Commits all staged changes with a message and updates the `HEAD` to the latest commit.

4. **Create and Switch Branches (`branch`, `checkout`)**:
    - Create a branch:
      ```bash
      mygit> branch <branch-name>
      ```
    - Switch to a branch:
      ```bash
      mygit> checkout <branch-name>
      ```

5. **View Commit History (`log`)**:
    ```bash
    mygit> log
    ```
   Displays a log of all the commits, including commit IDs, messages, and timestamps.

6. **Merge Branches (`merge`)**:
    ```bash
    mygit> merge <branch-name>
    ```
   Merges the specified branch into the current branch.

7. **Check Status (`status`)**:
    ```bash
    mygit> status
    ```
   Shows the status of the working directory, including staged and modified files.

8. **Show Differences (`diff`)**:
    ```bash
    mygit> diff
    ```
   Compares the staged files with the current working directory and shows the differences.

[//]: # (### Project Structure)

[//]: # ()
[//]: # ()
[//]: # (mygit/)

[//]: # (├── src/)

[//]: # (│   ├── main/)

[//]: # (│   │   └── java/)

[//]: # (│   │       └── com/)

[//]: # (│   │           └── dileep/)

[//]: # (│   │               └── mygit/)

[//]: # (│   │                   ├── MyGitApplication.java     # CLI Interface)

[//]: # (│   │                   ├── service/)

[//]: # (│   │                   │   ├── RepositoryService.java  # Handles init, add, commit, branch operations)

[//]: # (│   │                   │   ├── CommitService.java      # Handles commit operations)

[//]: # (│   │                   │   ├── BranchService.java      # Handles branching operations)

[//]: # (│   │                   │   └── MergeService.java       # Handles branch merging)

[//]: # (│   │                   ├── util/)

[//]: # (│   │                   │   └── FileUtils.java          # File utility functions &#40;read/write/copy&#41;)

[//]: # (│   └── test/)

[//]: # (│       └── java/)

[//]: # (│           └── com/)

[//]: # (│               └── dileep/)

[//]: # (│                   └── mygit/)

[//]: # (│                       └── MyGitApplicationTests.java  # Unit tests)

[//]: # (├── .mygit/    # Local repository metadata &#40;created after init&#41;)

[//]: # (├── pom.xml    # Maven build file)

[//]: # (└── README.md)


### Future Enhancements

- **Conflict Resolution**: Implement more advanced merge conflict detection and resolution.
- **Remote Repository Support**: Add the ability to push and pull changes from a remote repository.
- **Performance Optimization**: Optimize file handling and commit history for large projects.

### Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a pull request

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to modify the above file to match the specifics of your project, such as the actual repository link, branch names, or licensing terms!
