package com.example.foneproject.repository;

import com.example.foneproject.model.Person;
import com.example.foneproject.repository.filter.PersonFilter;
import com.example.foneproject.repository.query.PersonRepositoryQueries;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonRepositoryImpl implements PersonRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Person> filter(PersonFilter filter) {
        final StringBuilder sb = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();

        sb.append(" SELECT bean FROM Person bean JOIN bean.phoneList phone WHERE 1=1");

        getPersonByNameParams(filter, sb, params);
        getPersonByCpfParams(filter, sb, params);
        getPersonByDddParams(filter, sb, params);
        getPersonByPhoneNumberParams(filter, sb, params);

        Query query = manager.createQuery(sb.toString(), Person.class);

        fillQueryParams(params, query);

        return query.getResultList();
    }

    private void getPersonByNameParams(PersonFilter filter, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filter.getName())){
            sb.append(" AND bean.name LIKE :name ");
            params.put("name", "%" + filter.getName() + "%");
        }
    }

    private void getPersonByCpfParams(PersonFilter filter, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filter.getCpf())){
            sb.append(" AND bean.cpf LIKE :cpf ");
            params.put("cpf", "%" + filter.getCpf() + "%");
        }
    }

    private void getPersonByDddParams(PersonFilter filter, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filter.getDdd())){
            sb.append(" AND phone.ddd = :ddd ");
            params.put("ddd", filter.getDdd());
        }
    }

    private void getPersonByPhoneNumberParams(PersonFilter filter, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(filter.getPhoneNumber())){
            sb.append(" AND phone.number = :number ");
            params.put("number", filter.getPhoneNumber());
        }
    }

    private void fillQueryParams(Map<String, Object> params, Query query) {
        for(Map.Entry<String, Object> param: params.entrySet()){
            query.setParameter(param.getKey(), param.getValue());
        }
    }
}
