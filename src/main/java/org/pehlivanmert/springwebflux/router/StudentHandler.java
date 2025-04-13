package org.pehlivanmert.springwebflux.router;

import org.pehlivanmert.springwebflux.service.StudentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StudentHandler {

    private final StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Handles the request to find all students with their courses.
     *
     * @param serverRequest the server request
     * @return a Mono of ServerResponse containing the list of students with their courses
     */
    public Mono<ServerResponse> handleFindAllStudentWithCourses(ServerRequest serverRequest) {
        return studentService.findAllWithCourses()
                .flatMap(s -> ServerResponse.ok().bodyValue(s))
                .switchIfEmpty(
                        Mono.defer(() -> Mono.error(new RuntimeException("No students found"))));
    }
}
