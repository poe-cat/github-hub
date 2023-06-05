# GitHub Hub Application

## General info

The GitHub Hub application is a REST API that serves information about the repositories and their branches of a given GitHub user. It retrieves the data directly from GitHub using the GitHub API. The information served includes:

- repository names,
- repository owners,
- repository branches,
- last commit SHA for each branch.

In case of an unsupported content type or if a user doesn't exist, it returns an appropriate error response.

## Technologies

The application is built with:

- Java version: 17
- Spring Boot version: 3.1.0 (used to create the REST API)
- GitHub API for Java (org.kohsuke): library used to communicate with GitHub
- Apache Maven 3.8.6: dependency management

## Setup

1. Clone the repository:
   
   ```
   gh repo clone poe-cat/github-hub
   ```

2. Navigate to the project directory:
   
   ```
   cd github-hub
   ```

3. The application uses a GitHub Personal Access Token for authentication. When making API requests to the GitHub API, especially for accessing user-specific data like repositories, it's often necessary to authenticate to ensure that the API calls are associated with a specific user or application. Replace the placeholder in the `UserController` with your personal access token.

If you don't know how to obtain one, follow these steps: https://docs.github.com/en/enterprise-server@3.4/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens

   ```java
   private static final String GITHUB_API_TOKEN = "your-github-token-here";
   ```

   Note: Treat your tokens like passwords and keep them secret. When working with the API, use tokens as environment variables instead of hardcoding them into your programs.

4. Build the application using Maven:

   ```
   mvn clean install
   ```

## Running the Application Locally

1. You can run the application from the command line with the Spring Boot Maven plugin. Navigate to the project directory and run:

   ```
   mvn spring-boot:run
   ```

2. Once the application is running, you can access the API with the following endpoint:

   ```
   http://localhost:8080/api/repositories/{username}
   ```

   Replace `{username}` with the GitHub username that you want to fetch the repository information for.

If the application runs successfully, you will see the JSON responses with the repository information when you hit the API endpoint. If the username does not exist or if the content type is not acceptable, an appropriate error message will be displayed.
