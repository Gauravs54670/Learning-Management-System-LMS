package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class InstructorCourseDTO {
    private String courseTitle;
    private String courseDescription;
    private String courseCode;
}
