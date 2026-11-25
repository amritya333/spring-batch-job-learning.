package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/** Transforms each Person: upper-cases the names. Returning null would filter the item out. */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) {
        Person transformed = new Person(
                person.firstName().toUpperCase(),
                person.lastName().toUpperCase(),
                person.email());
        log.info("Converting ({}) into ({})", person, transformed);
        return transformed;
    }
}
