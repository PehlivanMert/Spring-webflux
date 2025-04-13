package org.pehlivanmert.springwebflux.service;

import org.pehlivanmert.springwebflux.dto.CourseDto;
import org.pehlivanmert.springwebflux.dto.StudentDto;
import org.pehlivanmert.springwebflux.dto.StudentListDto;
import org.pehlivanmert.springwebflux.modal.Student;
import org.pehlivanmert.springwebflux.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseService courseService;

    public StudentService(StudentRepository studentRepository, CourseService courseService) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
    }

    public Flux<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * This method retrieves all students and their associated courses.
     * It uses reactive programming to fetch the data asynchronously.
     *
     * @return a Mono containing a StudentListDto with all students and their courses
     * But not best practices
     */
    public Mono<StudentListDto> findAllWithCourses() {
        return studentRepository.findAll()
                .flatMap(
                        student -> {
                            List<Mono<CourseDto>> courseDtoList = student.getCourses()
                                    .stream()
                                    .map(courseId -> courseService.findById(UUID.fromString(courseId)))
                                    .collect(Collectors.toList());

                            return Flux.combineLatest(courseDtoList, objects -> {
                                List<CourseDto> courses = Arrays.stream(objects)
                                        .map(obj -> (CourseDto) obj)
                                        .collect(Collectors.toList());

                                return new StudentDto(student.getName(), student.getEmail(), courses);
                            });
                        })
                .collectList()
                .map(StudentListDto::new);
    }
}
