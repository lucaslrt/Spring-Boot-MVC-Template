INSERT INTO person (name, cpf)
VALUES ('Samanta', '86730543540');
INSERT INTO person (name, cpf)
VALUES ('Maria', '55565893569');
INSERT INTO person (name, cpf)
VALUES ('Regina', '38767897100');
INSERT INTO person (name, cpf)
VALUES ('Bruna', '78673781620');
INSERT INTO person (name, cpf)
VALUES ('Ester', '72788740417');

INSERT INTO phone (ddd, number, person_id)
VALUES ('41', '999570146', (SELECT id FROM person WHERE cpf = '86730543540'));
INSERT INTO phone (ddd, number, person_id)
VALUES ('82', '39945903', (SELECT id FROM person WHERE cpf = '55565893569'));
INSERT INTO phone (ddd, number, person_id)
VALUES ('86', '35006330', (SELECT id FROM person WHERE cpf = '38767897100'));
INSERT INTO phone (ddd, number, person_id)
VALUES ('21', '997538804', (SELECT id FROM person WHERE cpf = '78673781620'));
INSERT INTO phone (ddd, number, person_id)
VALUES ('95', '38416516', (SELECT id FROM person WHERE cpf = '72788740417'));

INSERT INTO address (street, number, complement, neighborhood, city, state, person_id)
VALUES ('Rua dos Gerânios', 497, 'XXXX', 'Pricumã', 'Boa Vista', 'RR',
        (SELECT id FROM person WHERE cpf = '86730543540'));
INSERT INTO address (street, number, complement, neighborhood, city, state, person_id)
VALUES ('Rua dos Gerânios', 497, 'XXXX', 'Pricumã', 'Boa Vista', 'RR',
        (SELECT id FROM person WHERE cpf = '55565893569'));
INSERT INTO address (street, number, complement, neighborhood, city, state, person_id)
VALUES ('Rua dos Gerânios', 497, 'XXXX', 'Pricumã', 'Boa Vista', 'RR',
        (SELECT id FROM person WHERE cpf = '38767897100'));
INSERT INTO address (street, number, complement, neighborhood, city, state, person_id)
VALUES ('Rua dos Gerânios', 497, 'XXXX', 'Pricumã', 'Boa Vista', 'RR',
        (SELECT id FROM person WHERE cpf = '78673781620'));
INSERT INTO address (street, number, complement, neighborhood, city, state, person_id)
VALUES ('Rua dos Gerânios', 497, 'XXXX', 'Pricumã', 'Boa Vista', 'RR',
        (SELECT id FROM person WHERE cpf = '72788740417'));