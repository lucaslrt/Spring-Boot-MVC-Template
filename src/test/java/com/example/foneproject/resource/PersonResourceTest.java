package com.example.foneproject.resource;

import com.example.foneproject.FoneProjectApplicationTests;
import com.example.foneproject.model.Address;
import com.example.foneproject.model.Person;
import com.example.foneproject.model.Phone;
import com.example.foneproject.repository.filter.PersonFilter;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

// Usando a lib REST-ASSURED
public class PersonResourceTest extends FoneProjectApplicationTests {

    @Test
    public void find_person_by_ddd_and_phone_number() throws Exception {
        given()
                .pathParam("ddd", "86")
                .pathParam("number", "35006330")
                .when()
                .get("/persons/{ddd}/{number}")
                .then()
                .log().body().and()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(3),
                        "name", equalTo("Regina"),
                        "cpf", equalTo("38767897100"));
    }

    @Test
    public void show_not_found_error_message_when_search_by_unsaved_phone() throws Exception {
        given()
                .pathParam("ddd", "12")
                .pathParam("number", "1234")
                .get("/persons/{ddd}/{number}")
                .then()
                .log().body().and()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", equalTo("Não existe pessoa com o telefone (12)1234"));
    }

    @Test
    public void save_person() throws Exception {
        final Person person = new Person();
        final Phone phone = new Phone();
        final Address address = new Address();

        person.setName("Marli Bruna Marlene da Silva");
        person.setCpf("31042509018");

        phone.setDdd("61");
        phone.setNumber("994751564");

        address.setStreet("Quadra Quadra 102 Conjunto 16");
        address.setNumber("391");
        address.setNeighborhood("Setor Residencial Oeste (São Sebastião)");
        address.setCity("Brasília");
        address.setState("DF");

        person.setPhoneList(Collections.singletonList(phone));
        person.setAddressList(Collections.singletonList(address));

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(person)
            .when()
                .post("/persons")
            .then()
                    .log().headers()
                .and()
                    .log().body()
                .and()
                    .statusCode(HttpStatus.CREATED.value())
                    .headers("Location", equalTo("http://localhost:" + port + "/persons/61/994751564"))
                    .body("id", equalTo(6),
                            "name", equalTo("Marli Bruna Marlene da Silva"),
                            "cpf", equalTo("31042509018"));
    }

    @Test
    public void not_save_person_with_already_saved_cpf() throws Exception {
        final Person person = new Person();
        final Phone phone = new Phone();
        final Address address = new Address();

        person.setName("Marli Bruna Marlene da Silva");
        person.setCpf("78673781620");

        phone.setDdd("61");
        phone.setNumber("994751564");

        address.setStreet("Quadra Quadra 102 Conjunto 16");
        address.setNumber("391");
        address.setNeighborhood("Setor Residencial Oeste (São Sebastião)");
        address.setCity("Brasília");
        address.setState("DF");

        person.setPhoneList(Collections.singletonList(phone));
        person.setAddressList(Collections.singletonList(address));

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(person)
                .when()
                .post("/persons")
                .then()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Cpf " + person.getCpf() + " já cadastrado."));
    }

    @Test
    public void not_save_person_with_already_saved_phone() throws Exception {
        final Person person = new Person();
        final Phone phone = new Phone();
        final Address address = new Address();

        person.setName("Marli Bruna Marlene da Silva");
        person.setCpf("31042509018");

        phone.setDdd("41");
        phone.setNumber("999570146");

        address.setStreet("Quadra Quadra 102 Conjunto 16");
        address.setNumber("391");
        address.setNeighborhood("Setor Residencial Oeste (São Sebastião)");
        address.setCity("Brasília");
        address.setState("DF");

        person.setPhoneList(Collections.singletonList(phone));
        person.setAddressList(Collections.singletonList(address));

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(person)
                .when()
                .post("/persons")
                .then()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Número de telefone (" + person.getPhoneList().get(0).getDdd() + ")" +
                        person.getPhoneList().get(0).getNumber() + " já cadastrado."));
    }

    @Test
    public void filter_by_name() throws Exception {
        final PersonFilter filter = new PersonFilter();
        filter.setName("a");

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(filter)
            .when()
                .post("/persons/filter")
            .then()
                    .log().body()
                .and()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", containsInAnyOrder(1,2,3,4),
                            "name", containsInAnyOrder("Samanta", "Maria", "Regina", "Bruna"),
                            "cpf", containsInAnyOrder("86730543540", "55565893569", "38767897100", "78673781620"));
    }
}
