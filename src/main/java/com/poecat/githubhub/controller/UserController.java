package com.poecat.githubhub.controller;

import com.poecat.githubhub.info.BranchInfo;
import com.poecat.githubhub.info.RepositoryInfo;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final String GITHUB_API_TOKEN = "YOUR_GITHUB_API_KEY";

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
