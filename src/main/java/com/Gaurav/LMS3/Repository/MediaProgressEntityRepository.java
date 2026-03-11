package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.MediaProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaProgressEntityRepository extends
        JpaRepository<MediaProgressEntity, Long> {
    Optional<MediaProgressEntity> findByCourseMedia_MediaIdAndEnrollment_EnrollmentId(Long mediaId, Long enrollmentId);
    List<MediaProgressEntity> findByEnrollment_EnrollmentId(Long enrollmentId);
}
