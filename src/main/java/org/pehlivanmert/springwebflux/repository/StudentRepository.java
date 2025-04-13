package org.pehlivanmert.springwebflux.repository;

import org.pehlivanmert.springwebflux.modal.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {
}
