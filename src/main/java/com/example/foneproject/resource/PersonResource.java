package com.example.foneproject.resource;

import com.example.foneproject.model.Person;
import com.example.foneproject.model.Phone;
import com.example.foneproject.repository.PersonRepository;
import com.example.foneproject.repository.filter.PersonFilter;
import com.example.foneproject.service.PersonService;
import com.example.foneproject.service.exception.PhoneNotFoundException;
import com.example.foneproject.service.exception.UniqueCPFException;
import com.example.foneproject.service.exception.UniquePhoneNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonResource {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/{ddd}/{number}")
    public ResponseEntity<Person> findByPhone(@PathVariable("ddd") String ddd,
                                              @PathVariable("number") String phoneNumber) throws PhoneNotFoundException {
        final Phone phone = new Phone();
        phone.setDdd(ddd);
        phone.setNumber(phoneNumber);

        final Person person = personService.findByPhone(phone);

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> savePerson(@RequestBody Person person, HttpServletResponse response) throws UniqueCPFException, UniquePhoneNumberException {

        final Person newPerson = personService.save(person);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{ddd}/{number}")
                .buildAndExpand(person.getPhoneList().get(0).getDdd(), person.getPhoneList().get(0).getNumber()).toUri();

        response.setHeader("Location", uri.toASCIIString());

        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Person>> filter(@RequestBody PersonFilter filter){
        final List<Person> filterList = personRepository.filter(filter);

        return new ResponseEntity<>(filterList, HttpStatus.OK);
    }


    @ExceptionHandler({PhoneNotFoundException.class})
    public ResponseEntity<Error> handlePhoneNotFoundException(PhoneNotFoundException e){
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UniqueCPFException.class})
    public ResponseEntity<Error> handleUniqueCPFException(UniqueCPFException e){
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UniquePhoneNumberException.class})
    public ResponseEntity<Error> handleUniquePhoneNumberException(UniquePhoneNumberException e){
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    class Error{
        private final String error;

        public Error(String error){
            this.error = error;
        }

        public String getError(){
            return error;
        }
    }
}
