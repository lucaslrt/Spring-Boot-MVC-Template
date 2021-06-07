package com.example.foneproject.service;

import com.example.foneproject.model.Person;
import com.example.foneproject.model.Phone;
import com.example.foneproject.repository.PersonRepository;
import com.example.foneproject.service.exception.PhoneNotFoundException;
import com.example.foneproject.service.exception.UniqueCPFException;
import com.example.foneproject.service.exception.UniquePhoneNumberException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 *  Regras de negócio:
 * - Uma pessoa não pode ter o mesmo cpf de outra pessoa
 * - Uma pessoa não pode ter o mesmo número de telefone de outra pessoa
 * - Deve ser possível filtrar pessoas por letra de nome
 */

// O primeiro teste foi feito pela camada de serviço
@RunWith(SpringRunner.class)
public class PersonServiceShould {

    private final String CPF = "78673781620";
    private final String NAME = "Teste da Silva";
    private final String DDD = "61";
    private final String PHONE_NUMBER = "12345678";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @MockBean
    private PersonRepository personRepository;
    private PersonService personService;
    private Person person;
    private Phone phone;

    @Before
    public void setUp() throws Exception {
        personService = new PersonServiceImpl(personRepository);
        person = new Person();
        person.setName(NAME);
        person.setCpf(CPF);

        phone = new Phone();
        phone.setDdd(DDD);
        phone.setNumber(PHONE_NUMBER);

        person.setPhoneList(Collections.singletonList(phone));
    }

    @Test
    public void save_person_in_repository() throws Exception {
        personService.save(person);

        verify(personRepository).save(person);
    }

    @Test(expected = UniqueCPFException.class)
    public void not_save_more_than_one_person_with_the_same_cpf() throws Exception {
        when(personRepository.findByCpf(CPF)).thenReturn(Optional.of(person));

        personService.save(person);
    }

    @Test
    public void return_exception_message_when_save_more_than_one_person_with_the_same_cpf() throws Exception {
        when(personRepository.findByCpf(CPF)).thenReturn(Optional.of(person));

        Throwable t = assertThrows(UniqueCPFException.class, () -> personService.save(person));
        assertEquals("Cpf " + CPF + " já cadastrado.", t.getMessage());
    }

    @Test(expected = UniquePhoneNumberException.class)
    public void not_save_more_than_one_person_with_the_same_phone() throws Exception {
        when(personRepository.findByPhone(DDD, PHONE_NUMBER)).thenReturn(Optional.of(person));

        personService.save(person);
    }

    @Test
    public void return_exception_message_when_save_more_than_one_person_with_the_same_phone() throws Exception {
        when(personRepository.findByPhone(DDD, PHONE_NUMBER)).thenReturn(Optional.of(person));

        Throwable t = assertThrows(UniquePhoneNumberException.class, () -> personService.save(person));
        assertEquals("Número de telefone (" + DDD + ")" +
                PHONE_NUMBER + " já cadastrado.", t.getMessage());
    }

    @Test
    public void find_person_by_phone() throws Exception {
        when(personRepository.findByPhone(DDD, PHONE_NUMBER)).thenReturn(Optional.of(person));

        Person findedPerson = personService.findByPhone(phone);

        verify(personRepository).findByPhone(DDD, PHONE_NUMBER);

        assertThat(findedPerson).isNotNull();
        assertThat(findedPerson.getName()).isEqualTo(NAME);
        assertThat(findedPerson.getCpf()).isEqualTo(CPF);
    }

    @Test(expected = PhoneNotFoundException.class)
    public void return_exception_when_not_find_person_by_phone() throws Exception {
        personService.findByPhone(phone);
    }

    @Test
    public void return_exception_message_when_not_find_person_by_phone() throws Exception {

        Throwable t = assertThrows(PhoneNotFoundException.class, () -> personService.findByPhone(phone));
        assertEquals("Não existe pessoa com o telefone (" + DDD + ")" + PHONE_NUMBER, t.getMessage());
    }
}
