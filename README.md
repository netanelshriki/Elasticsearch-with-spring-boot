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


## step 3 - custom @Query 
lets we write more complex queries (which in fact is the reason elasticsearch developed for)
so lets we add some more Persons
in order to run more complex queries, and we save them as we did in section 2.

this is the way we write a queries in Kibana 
devtools (we'll elaborate on this later) - it's called query DSL, and is json based query:
```json lines
GET /persons-index/_search
{
  "query": {
    "range": {
      "age": {
        "gt": 30
      }
    }
  }
}
```
in this query we access to our index we created (and defined in Person model)
and we ask to retrieve us only persons that their age is greater than 30 (gt)

in ElasticsearchRepository, it will be written a bit different.
spring already know the path and http verb we want to use and because it's @Query, we can remove the "query":{} from the json
(since it's obvious..), so we leave with this json:

```json lines
  "range": {
      "age": {
        "gt": 30
      }
    }
```

now we write it in one line and add it to the @Query on our custom query

```java
@Repository
public interface PersonRepository extends ElasticsearchRepository<Person,Long> {
    @Query(value = "{\"range\":{\"age\":{\"gt\":30}}}")
    List<Person> findAllOlderThan30();
}

```
and you can run it and to see how it works :wink:

## step 3 - kibana
first we have to run a kibana image
```shell
docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:7.6.2
```

kibana is data visualization and exploration tool, so now if we navigate to http://localhost5601 
we should see the Kibana page like that:

![This is an image](https://github.com/netanelshriki/Elasticsearch-with-spring-boot/blob/master/src/main/resources/assets/landing-page-kibana.png)


expand the navbar on the right side, and we should be able to see dev tools, click it,
and we should see a json file as we saw earlier, there we can write complex queries.

lets we write a query that count us the number of persons with the exact name "John" 
(name.keyword - means the exact word):
```json lines
GET /persons-index/_count
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "name.keyword": "John"
          }
        },
        {
          "range": {
            "age": {
              "gt": 20
            }
          }
        }
      ]
    }
  }
}
```

notice to the url we send our request - _count instead of _search (that retrieve the whole data).
and look at the results, only one person matched our query:
![This is an image](https://github.com/netanelshriki/Elasticsearch-with-spring-boot/blob/master/src/main/resources/assets/count-query-kibana.png)


this is, an example to a query that return only the people that their name contains the char "a"
and in addition their age is greater than 20:

```json lines
GET /persons-index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "wildcard": {
            "name": "*a*"
          }
        },
        {
          "range": {
            "age": {
              "gt": 20
            }
          }
        }
      ]
    }
  }
}
```

and result return us the whole data on for people who match our query (I won't show the whole result, it's too long :wink:):
![This is an image](https://github.com/netanelshriki/Elasticsearch-with-spring-boot/blob/master/src/main/resources/assets/search-query-kibana.png)

congratulations! :muscle:


