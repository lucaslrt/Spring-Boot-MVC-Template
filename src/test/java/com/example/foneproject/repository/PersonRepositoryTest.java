package com.example.foneproject.repository;

import com.example.foneproject.model.Person;
import com.example.foneproject.repository.filter.PersonFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void find_person_by_cpf() throws Exception {
        Optional<Person> optional = personRepository.findByCpf("38767897100");

        assertThat(optional.isPresent()).isTrue();

        Person person = optional.get();
        assertThat(person.getId()).isEqualTo(3L);
        assertThat(person.getName()).isEqualTo("Regina");
        assertThat(person.getCpf()).isEqualTo("38767897100");
    }

    @Test
    public void not_find_person_using_not_saved_cpf() throws Exception {
        Optional<Person> optional = personRepository.findByCpf("123");

        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void find_person_by_phone() throws Exception {
        Optional<Person> optional = personRepository.findByPhone("86","35006330");

        assertThat(optional.isPresent()).isTrue();

        Person person = optional.get();
        assertThat(person.getId()).isEqualTo(3L);
        assertThat(person.getName()).isEqualTo("Regina");
        assertThat(person.getCpf()).isEqualTo("38767897100");
    }

    @Test
    public void not_find_person_using_not_saved_phone() throws Exception {
        Optional<Person> optional = personRepository.findByPhone("66", "9938082");

        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void filter_person_by_name() throws Exception {
        PersonFilter filter = new PersonFilter();

        filter.setName("a");

        List<Person> filterList = personRepository.filter(filter);

        assertThat(filterList.size()).isEqualTo(4);
    }

    @Test
    public void filter_person_by_cpf() throws Exception {
        PersonFilter filter = new PersonFilter();

        filter.setCpf("78");

        List<Person> filterList = personRepository.filter(filter);

        assertThat(filterList.size()).isEqualTo(3);
    }

    @Test
    public void filter_person_by_name_and_cpf() throws Exception {
        PersonFilter filter = new PersonFilter();

        filter.setCpf("78");
        filter.setName("a");

        List<Person> filterList = personRepository.filter(filter);

        assertThat(filterList.size()).isEqualTo(2);
    }

    @Test
    public void filter_person_by_ddd() throws Exception {
        PersonFilter filter = new PersonFilter();

        filter.setDdd("21");

        List<Person> filterList = personRepository.filter(filter);

        assertThat(filterList.size()).isEqualTo(1);
    }

    @Test
    public void filter_person_by_phone_number() throws Exception {
        PersonFilter filter = new PersonFilter();

        filter.setPhoneNumber("999570146");

        List<Person> filterList = personRepository.filter(filter);

        assertThat(filterList.size()).isEqualTo(1);
    }
}
