package org.pehlivanmert.springwebflux.dto;

import org.pehlivanmert.springwebflux.modal.metadata.CourseMetadata;

public record CourseDto(String name, String description, Integer duration, String teacher,
                        CourseMetadata courseMetadata) {
}
