package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaEntityRepository extends JpaRepository<CourseMediaEntity, Long> {
    @Query("""
        SELECT COALESCE(MAX(m.mediaOrdering), 0)
        FROM CourseMediaEntity m
        WHERE m.course.courseId = :courseId
    """)
    Integer findLastMediaOrderByCourseId(@Param("courseId") Long courseId);
    Optional<CourseMediaEntity> findByMediaIdAndCourse_CourseId(Long mediaId, Long courseId);
        @Modifying
        @Query("""
        UPDATE CourseMediaEntity m
        SET m.mediaOrdering = m.mediaOrdering - 1
        WHERE m.course.courseId = :courseId
          AND m.mediaOrdering > :deletedMediaOrder
    """)
        void shiftMediaOrderAfterDelete(
                @Param("courseId") Long courseId,
                @Param("deletedMediaOrder") Integer deletedMediaOrder
        );
    @Query("""
            SELECT m.mediaUrl
            FROM CourseMediaEntity m
            WHERE m.course.courseId = :courseId
            AND m.courseMediaType = :mediaType
            """)
    Optional<String> findMediaUrlByCourseIdAndCourseMediaType(
            @Param("courseId") Long courseId,
            @Param("mediaType")CourseMediaType courseMediaType);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO(
                m.mediaId,
                m.courseMediaType,
                m.mediaOrdering,
                m.mediaUrl
            )
            FROM CourseMediaEntity m
            WHERE m.mediaId = :mediaId
            """)
    Optional<MediaContentDTO> getMedia(@Param("mediaId") Long mediaId);
    boolean existsByMediaIdAndCourse_CourseId(Long mediaId, Long courseId);
}
