package com.example.foneproject.repository;

import com.example.foneproject.model.Person;
import com.example.foneproject.repository.query.PersonRepositoryQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryQueries {

    Optional<Person> findByCpf(String cpf);

    //Isso aqui é feito pq o método precisa ter o mesmo nome do atributo ??? (Estudar mais sobre)
    @Query("SELECT bean FROM Person bean " +
            "JOIN bean.phoneList phone " +
            "WHERE phone.ddd = :ddd AND phone.number = :number")
    Optional<Person> findByPhone(@Param("ddd") String ddd, @Param("number") String phoneNumber);
}
