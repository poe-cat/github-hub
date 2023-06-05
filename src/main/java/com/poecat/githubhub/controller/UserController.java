package com.poecat.githubhub.controller;

import com.poecat.githubhub.errors.ErrorResponse;
import com.poecat.githubhub.info.BranchInfo;
import com.poecat.githubhub.info.RepositoryInfo;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private GitHub gitHub;
    private static final String GITHUB_API_TOKEN = "YOUR_GITHUB_API_TOKEN";

    public UserController() {
    }

    public UserController(GitHub gitHub) {
        this.gitHub = gitHub;
    }

    @GetMapping(value = "/repositories/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRepositories(@PathVariable String username,
                                             @RequestHeader(value = "Accept") String acceptHeader) {

        try {
            GitHub github = GitHub.connectUsingOAuth(GITHUB_API_TOKEN);
            GHRepository[] repositories = github.getUser(username).getRepositories().values().toArray(new GHRepository[0]);

            // not include forked repositories
            List<RepositoryInfo> result = new ArrayList<>();
            for (GHRepository repository : repositories) {
                if (!repository.isFork()) {
                    String repoName = repository.getName();
                    String ownerLogin = repository.getOwnerName();

                    List<BranchInfo> branches = new ArrayList<>();
                    for (GHBranch branch : repository.getBranches().values()) {
                        String branchName = branch.getName();
                        String lastCommitSha = branch.getSHA1();
                        branches.add(new BranchInfo(branchName, lastCommitSha));
                    }

                    result.add(new RepositoryInfo(repoName, ownerLogin, branches));
                }
            }

            return ResponseEntity.ok(result);

        } catch (HttpClientErrorException.NotAcceptable e) {
            // Return 406 response for unsupported content type
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "We only serve JSON here"));
        } catch (HttpClientErrorException.NotFound | IOException e) {
            // Return 404 response if the user does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
        }
    }
}
