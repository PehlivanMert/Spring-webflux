package org.pehlivanmert.springwebflux.dto;

import java.util.List;

public record StudentListDto(List<StudentDto> students) {
}