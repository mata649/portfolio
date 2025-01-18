package com.mata649.portfolio.content.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class ContentGistRepository implements ContentRepository {
    @Value("${custom.content.gist.about-me-url}")
    private String ABOUT_ME_URL;
    private final WebClient client;

    public ContentGistRepository() {
        client = WebClient.create();
    }

    @Override
    public String getAboutMe() {
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(ABOUT_ME_URL)
                .retrieve();

        return responseSpec.bodyToMono(String.class).block();
    }
}
