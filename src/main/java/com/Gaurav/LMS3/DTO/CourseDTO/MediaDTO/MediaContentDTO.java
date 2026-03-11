package com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import lombok.*;

@NoArgsConstructor
@Getter @Setter @Builder
public class MediaContentDTO {
    private Long mediaId;
    private CourseMediaType courseMediaType;
    private Integer mediaOrdering;
    private String mediaUrl;
    public MediaContentDTO(
            Long mediaId,
            CourseMediaType courseMediaType,
            Integer mediaOrdering,
            String mediaUrl){
        this.mediaId = mediaId;
        this.courseMediaType = courseMediaType;
        this.mediaOrdering = mediaOrdering;
        this.mediaUrl = mediaUrl;
    }
}
