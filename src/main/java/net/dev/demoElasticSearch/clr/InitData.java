package net.dev.demoElasticSearch.clr;

import net.dev.demoElasticSearch.models.Person;
import net.dev.demoElasticSearch.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class InitData implements CommandLineRunner {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(String... args) {

        personRepository.saveAll(fillData());

        personRepository.findAllOlderThan30().forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
        personRepository.findAllOlderThan30AndNameContainA().forEach(System.out::println);



    }

    private List<Person> fillData() {
        Person person1 = Person.builder()
                .id(1)
                .name("John")
                .age(25)
                .build();

        Person person2 = Person.builder()
                .id(2)
                .name("Alice")
                .age(30)
                .build();

        Person person3 = Person.builder()
                .id(3)
                .name("Bob")
                .age(35)
                .build();

        Person person4 = Person.builder()
                .id(4)
                .name("Sarah")
                .age(28)
                .build();

        Person person5 = Person.builder()
                .id(5)
                .name("Michael")
                .age(40)
                .build();

        Person person6 = Person.builder()
                .id(6)
                .name("Emily")
                .age(32)
                .build();

        Person person7 = Person.builder()
                .id(7)
                .name("David")
                .age(27)
                .build();

        Person person8 = Person.builder()
                .id(8)
                .name("Emma")
                .age(29)
                .build();

        Person person9 = Person.builder()
                .id(9)
                .name("Daniel")
                .age(33)
                .build();

        Person person10 = Person.builder()
                .id(10)
                .name("Olivia")
                .age(31)
                .build();

        return List.of(person1, person2, person3, person4, person5, person6, person7, person8, person9, person10);
    }
}
