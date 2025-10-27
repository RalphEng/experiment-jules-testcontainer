# Project Setup for Google Jules

This document outlines the necessary steps to configure the development environment for this project when using Google Jules.

## Docker Setup for Testcontainers

This project uses [Testcontainers](https://www.testcontainers.org/) for integration testing with a real database. Testcontainers requires a working Docker environment.

### Granting Docker Permissions

The user running the Maven build needs permission to access the Docker socket. There are two common ways to achieve this:

1.  **Add the user to the `docker` group:**

    ```bash
    sudo usermod -aG docker $USER
    ```

    **Note:** You may need to start a new shell session for this change to take effect.

2.  **Change the group ownership of the Docker socket:**

    If adding the user to the `docker` group is not effective, you can change the group ownership of the Docker socket to the current user's group.

    ```bash
    sudo chgrp $(id -gn) /var/run/docker.sock
    ```

    This is a reliable workaround if you encounter permission errors when running tests.

## Maven Setup

The project uses a custom Maven repository for some of its dependencies. To access this repository, you need to configure Maven to use a `settings.xml` file.

### `settings.xml` Configuration

1.  **Create the `settings.xml` file:**

    Create a file named `settings.xml` in your Maven home directory (`~/.m2/`). In the Google Jules environment, the absolute path is `/home/jules/.m2/settings.xml`.

2.  **Add the following content to the `settings.xml` file:**

    ```xml
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                  https://maven.apache.org/xsd/settings-1.0.0.xsd">
      <servers>
        <server>
          <id>queo-nexus</id>
          <username>${env.queoMavenRepositoryUserName}</username>
          <password>${env.queoMavenRepositoryPassword}</password>
        </server>
      </servers>
      <mirrors>
        <mirror>
          <id>queo-nexus</id>
          <url>https://nexus.dev.queo-group.com/repository/maven-public/</url>
          <mirrorOf>*</mirrorOf>
        </mirror>
      </mirrors>
    </settings>
    ```

    This configuration does two things:

    *   **Configures a mirror:** It directs Maven to download all artifacts from the custom repository at `https://nexus.dev.queo-group.com/repository/maven-public/`.
    *   **Sets up authentication:** It uses the environment variables `queoMavenRepositoryUserName` and `queoMavenRepositoryPassword` for authentication with the repository. These environment variables are securely managed by the Google Jules environment.

Once you have completed these steps, you can build and test the project using the following command:

```bash
./mvnw clean install
```
