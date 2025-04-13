package org.pehlivanmert.springwebflux.repository;

import org.pehlivanmert.springwebflux.modal.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CourseRepository extends ReactiveCrudRepository<Course, UUID> {

}
