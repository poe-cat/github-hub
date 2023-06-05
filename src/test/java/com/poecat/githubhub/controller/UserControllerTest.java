package com.poecat.githubhub.controller;

import com.poecat.githubhub.info.BranchInfo;
import com.poecat.githubhub.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private GitHub gitHub;
    @Mock
    private GHUser ghUser;
    @Mock
    private GHRepository repository;
    @Mock
    private GHBranch branch;
    private MockMvc mockMvc;

    private UserController userController;

    @BeforeEach
    void setup() throws IOException {

        // Create and inject the mocks
        gitHub = Mockito.mock(GitHub.class);
        ghUser = Mockito.mock(GHUser.class);
        repository = Mockito.mock(GHRepository.class);
        branch = Mockito.mock(GHBranch.class);

        userController = new UserController(gitHub);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getRepositories_success() throws Exception {

        // Given
        Map<String, GHBranch> branchMap = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            branchMap.put("branchName" + i, branch);
        }

        Map<String, GHRepository> repoMap = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            repoMap.put("repoName" + i, repository);
        }

        // When
        List<RepositoryInfo> expectedResponse = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            List<BranchInfo> branches = new ArrayList<>();
            branches.add(new BranchInfo("branchName" + i, "commitSha" + i));
            expectedResponse.add(new RepositoryInfo("repoName" + i, "ownerLogin", branches));
        }

        // Then
        MvcResult mvcResult = mockMvc.perform(get("/api/repositories/{username}", "poe-cat")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

}
