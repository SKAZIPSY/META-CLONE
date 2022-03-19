package com.pagination.spring.boot.paginantion.service;

import com.pagination.spring.boot.paginantion.model.Tutorial;
import com.pagination.spring.boot.paginantion.repo.TutorialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class TutorialServiceImpl implements TutorialService{
    @Autowired
    TutorialRepo tutorialRepository;

    @Override
    public Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @Override
    public List<Tutorial> getAllTutorials(String[] sort) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }

        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0])); // oder takes two parameter one is diretion and other is field on which sort is required
        }
        List<Tutorial> tutorials = tutorialRepository.findAll(Sort.by(orders));

        return tutorials;
    }

    @Override
    public Map<String, Object> getAllTutorialsPage(String title, int page, int size, String[] sort) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        List<Tutorial> tutorials = new ArrayList<Tutorial>();

        // sorting is done with page object


        // create a page object to store the information if present
        Page<Tutorial> pageTuts;
        if (title == null)
            pageTuts = tutorialRepository.findAll(pagingSort);
        else
            pageTuts = tutorialRepository.findByTitleContaining(title, pagingSort);
        // after the data is present put it in tutorials
        tutorials = pageTuts.getContent();


        // doubt what is the significance of this
        Map<String, Object> response = new HashMap<>();
        response.put("tutorials", tutorials);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return response;
    }

    @Override
    public Map<String, Object> findByPublished(int page, int size) {
        List<Tutorial> tutorials = new ArrayList<Tutorial>();
        Pageable paging = PageRequest.of(page, size);

        Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true, paging);
        tutorials = pageTuts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("tutorials", tutorials);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return response;
    }


}
