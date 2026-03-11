package com.Gaurav.LMS3.Service.CourseServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTORequest;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTOResponse;

public interface MediaProgressService {
    MediaProgressDTOResponse evaluateMediaProgress(
            String email,
            Long courseId,
            MediaProgressDTORequest request);
}
