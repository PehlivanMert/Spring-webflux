package org.pehlivanmert.springwebflux.service;

import org.pehlivanmert.springwebflux.dto.CourseDto;
import org.pehlivanmert.springwebflux.modal.Course;
import org.pehlivanmert.springwebflux.repository.CourseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Flux<Course> findAll() {
        return courseRepository.findAll();
    }

    public Mono<CourseDto> findById(UUID id) {
        return courseRepository.findById(id)
                .map(course -> new CourseDto(course.getName(), course.getDescription(), course.getDuration(), course.getTeacher(), course.getCourseMetadata()));
    }
}
