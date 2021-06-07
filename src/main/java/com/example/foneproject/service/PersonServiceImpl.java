package com.example.foneproject.service;

import com.example.foneproject.model.Person;
import com.example.foneproject.model.Phone;
import com.example.foneproject.repository.PersonRepository;
import com.example.foneproject.service.exception.PhoneNotFoundException;
import com.example.foneproject.service.exception.UniqueCPFException;
import com.example.foneproject.service.exception.UniquePhoneNumberException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) throws UniqueCPFException, UniquePhoneNumberException {
        Optional<Person> optional = personRepository.findByCpf(person.getCpf());

        if(optional.isPresent()){
            throw new UniqueCPFException("Cpf " + person.getCpf() + " já cadastrado.");
        }

        final String ddd = person.getPhoneList().get(0).getDdd();
        final String number = person.getPhoneList().get(0).getNumber();
        optional = personRepository.findByPhone(ddd, number);

        if(optional.isPresent()){
            throw new UniquePhoneNumberException("Número de telefone (" + ddd + ")" +
                    number + " já cadastrado.");
        }

        return personRepository.save(person);
    }

    @Override
    public Person findByPhone(Phone phone) throws PhoneNotFoundException {

        Optional<Person> optional = personRepository.findByPhone(phone.getDdd(), phone.getNumber());
        return optional.orElseThrow(() -> new PhoneNotFoundException("Não existe pessoa com o telefone (" + phone.getDdd() + ")" + phone.getNumber()));
    }
}
