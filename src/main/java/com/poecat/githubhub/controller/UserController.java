package com.poecat.githubhub.controller;

import com.poecat.githubhub.info.RepositoryInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String GITHUB_API_URL = "https://api.github.com";

    @GetMapping("/{username}/repositories")
    @ResponseBody
    public ResponseEntity<List<RepositoryInfo>> getRepositories(@PathVariable String username) {

        return null;
    }

}
