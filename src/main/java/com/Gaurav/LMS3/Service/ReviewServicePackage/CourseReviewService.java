package com.Gaurav.LMS3.Service.ReviewServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewRequest;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewResponse;


public interface CourseReviewService {
CourseReviewResponse addReview(
            Long courseId,
            String email,
            CourseReviewRequest courseReviewRequest);
    String deleteReview(Long courseId, String email);
}
