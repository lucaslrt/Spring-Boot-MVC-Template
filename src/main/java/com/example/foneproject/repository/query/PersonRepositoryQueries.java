package com.example.foneproject.repository.query;

import com.example.foneproject.model.Person;
import com.example.foneproject.repository.filter.PersonFilter;

import java.util.List;

public interface PersonRepositoryQueries {
    List<Person> filter(PersonFilter filter);
}
