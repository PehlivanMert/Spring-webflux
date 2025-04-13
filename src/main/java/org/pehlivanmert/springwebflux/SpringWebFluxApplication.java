package org.pehlivanmert.springwebflux;

import org.pehlivanmert.springwebflux.modal.Course;
import org.pehlivanmert.springwebflux.modal.Student;
import org.pehlivanmert.springwebflux.modal.metadata.EnglishCourseMetadata;
import org.pehlivanmert.springwebflux.modal.metadata.SpringCourseMetadata;
import org.pehlivanmert.springwebflux.repository.CourseRepository;
import org.pehlivanmert.springwebflux.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class SpringWebFluxApplication implements CommandLineRunner {


    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    public SpringWebFluxApplication(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxApplication.class, args);
    }


    /**
     * This method is used to initialize the database with some data.
     * It creates a course and a student and saves them to the database.
     *
     * @param args command line arguments
     * @throws Exception if an error occurs while saving the data
     */
    @Override
    public void run(String... args) throws Exception {

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Webflux")
                .description("Spring Webflux")
                .duration(10)
                .teacher("Mert Pehlivan")
                .courseMetadata(
                        SpringCourseMetadata.builder()
                                .type("spring")
                                .language("Java")
                                .github("https://github.com/PehlivanMert")
                                .prerequisites(List.of("Java", "Spring"))
                                .build())
                .isUpdated(false)
                .build();

        //courseRepository.save(course).block();

        Student student = Student.builder()
                .id(UUID.randomUUID())
                .name("John")
                .email("j@j.com")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .courses(Set.of(course.getId().toString()))
                .isUpdated(false)
                .build();

        //studentRepository.save(student).block();


        Course course2 = Course.builder()
                .id(UUID.randomUUID())
                .name("English")
                .description("English with Pehlivan")
                .duration(100)
                .teacher("Rabia Pehlivan")
                .courseMetadata(
                        EnglishCourseMetadata.builder()
                                .type("spring")
                                .level("B1")
                                .books(List.of("B1", "B2"))
                                .build())
                .isUpdated(false)
                .build();

        courseRepository.save(course2).block();

        Student student2 = Student.builder()
                .id(UUID.randomUUID())
                .name("KTS")
                .email("j@j.com")
                .dateOfBirth(LocalDate.of(2000, 2, 1))
                .courses(Set.of(course2.getId().toString()))
                .isUpdated(false)
                .build();

        studentRepository.save(student2).block();

    }
}
