package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CourseMediaDeleteRequest {
    private Long mediaId;
    private String courseMediaType;
}
