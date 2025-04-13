package org.pehlivanmert.springwebflux.router;

import org.pehlivanmert.springwebflux.service.StudentService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class StudentHandler {

    private final StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
    }

    public Mono<ServerResponse> handleFindAllStudentWithCourses(ServerRequest serverRequest) {
        return studentService.findAllWithCourses()
                .flatMap(s -> ServerResponse.ok().bodyValue(s))
                .switchIfEmpty(
                        Mono.defer(() -> Mono.error(new RuntimeException("No students found"))));
    }
}
