package com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class CourseMediaDTOPreview {
    private Long mediaId;
    private CourseMediaType courseMediaType;
    private Integer mediaOrder;
    private String mediaUrl;
}
