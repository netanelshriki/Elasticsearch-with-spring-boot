# Elasticsearch-with-spring-boot
## how to use spring boot with Elasticsearch and Kibana

## step 1 - running docker:
run the following command to run an elasticsearch server locally, with a single node and run on port 9200 
(and disable xpack for easier integration with kibana later)

```shell
docker run --name elasticsearch  --net elastic -p 9200:9200 -e discovery.type=single-node -e ES_JAVA_OPTS="-Xms1g -Xmx1g" -e xpack.security.enabled=false -it docker.elastic.co/elasticsearch/elasticsearch:7.6.2
```
## step 2 - spring boot app: 

we add the spring-boot-starter-data-elasticsearch to our pom.xml
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
		</dependency>
```

now we create our elasticsearch configuration class (the directory under EnableElasticsearchRepositories pointing to your repository class)

```java
@Configuration
@EnableElasticsearchRepositories(basePackages = "net.dev.demoElasticSearch.repos")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();

        return RestClients.create(clientConfiguration)
                .rest();
    }
}
```

this is our repository class as mentioned earlier

```java
@Repository
public interface PersonRepository extends ElasticsearchRepository<Person,Long> {
}
```
 now we define our model for example, Person (indexName can be replaced but any name you would like to)

```java
@Data
@Builder
@Document(indexName = "persons-index")
public class Person {
    @Id
    private long id;
    private String name;
    private int age;
}
```

let we just add some code to create an index and document
```java
@Component
@Order(1)
public class InitData implements CommandLineRunner {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(String... args) {
        Person person = Person.builder()
                .id(1)
                .name("John")
                .age(25)
                .build();
        
        personRepository.save(person);
        
        // printing to see if we indeed saved this person
        System.out.println(personRepository.findById(1L));
    }
}
```

And that's it!

now we just run our application, and we should be able to see John details :smiley:
