package net.dev.demoElasticSearch.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "persons-index")
public class Person {
    @Id
    private long id;
    private String name;
    private int age;

}
