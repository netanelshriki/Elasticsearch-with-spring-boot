package net.dev.demoElasticSearch.repos;

import net.dev.demoElasticSearch.models.Person;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends ElasticsearchRepository<Person,Long> {
    @Query(value = "{\"range\":{\"age\":{\"gt\":30}}}")
    List<Person> findAllOlderThan30();
    @Query("{\"bool\":{\"must\":[{\"wildcard\":{\"name\":\"*a*\"}},{\"range\":{\"age\":{\"gt\":30}}}]}}")
    List<Person> findAllOlderThan30AndNameContainA();
}
