package com.Gaurav.LMS3.DTO.CourseDTO;

import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//This dto is being used for fetching the course entity when student start studying course
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CourseContentDTO {
    private Long courseId;
    private String instructorName;
    private String courseTitle;
    private String courseDescription;
    private CourseType courseType;
    private String courseDuration;
    @Builder.Default
    private List<MediaContentDTO> medias = new ArrayList<>();
}
