package com.mata649.portfolio.content.service;

import com.mata649.portfolio.content.repository.ContentRepository;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    private final ContentRepository contentRepository;


    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public String getAboutMe(){
      return contentRepository.getAboutMe();
    }
}
