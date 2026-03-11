package com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class MediaEntityDTO {
    private CourseMediaType courseMediaType;
    private String mediaUrl;
    private LocalDateTime mediaUploadedTime;
}
