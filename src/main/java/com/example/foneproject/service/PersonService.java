package com.example.foneproject.service;

import com.example.foneproject.model.Person;
import com.example.foneproject.model.Phone;
import com.example.foneproject.service.exception.PhoneNotFoundException;
import com.example.foneproject.service.exception.UniqueCPFException;
import com.example.foneproject.service.exception.UniquePhoneNumberException;

public interface PersonService {
    Person save(Person person) throws UniqueCPFException, UniquePhoneNumberException;

    Person findByPhone(Phone phone) throws PhoneNotFoundException;
}
