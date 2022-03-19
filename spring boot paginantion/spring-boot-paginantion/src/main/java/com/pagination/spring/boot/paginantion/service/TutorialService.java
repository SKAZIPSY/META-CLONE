package com.pagination.spring.boot.paginantion.service;

import com.pagination.spring.boot.paginantion.model.Tutorial;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface TutorialService {
    Sort.Direction getSortDirection(String direction);

    List<Tutorial> getAllTutorials(String[] sort);

    Map<String, Object> getAllTutorialsPage(String title, int page, int size, String[] sort);

    Map<String, Object> findByPublished(int page, int size);

}
