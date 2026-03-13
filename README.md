# LMS3 - Learning Management System Backend (Spring Boot)

#Short Description
- This is a backend REST API for a Learning Management System built using Spring Boot.
- The application supports course creation, media upload, enrollment, authentication,
role-based authorization, and course management.
- This project does not include UI. All operations are performed using REST APIs.

## Features

- User Authentication (Basic Auth / JWT)
- Role-based authorization (ADMIN / INSTRUCTOR / LEARNER)
- Course creation and management
- Course media upload (Thumbnail / Intro / Videos)
- Ordered media playlist support
- Course enrollment system
- Public course listing
- Course filtering and searching
- Account status handling (ACTIVE / SUSPENDED / DEACTIVATED)
- Exception handling with JSON response
- Transactional service logic

## Tech Stack
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Cloudinary (for media storage)
- Lombok
- PostMan

## Project Structure
- Controller Layer
- Service Layer
- Repository Layer
- Entity Layer
- DTO Layer
- Exception Handling
- Configuration Layer
- applicatin.properties and application.yml

## Environment Variables
- CLOUDINARY_CLOUD_NAME
- CLOUDINARY_API_KEY
- CLOUDINARY_SECRET_KEY
- DB_URL
- DB_USERNAME
- DB_PASSWORD

## Roles
ADMIN
- Manage users
- Manage courses
- Manage accounts
INSTRUCTOR
- Create courses
- Upload media
- Manage course playlist
- Manage course and course's medias
LEARNER
- View public courses
- Enroll in course
- Watch videos
- Manage enrolled courses

# Media Handling
- Media stored in Cloudinary
- Supports Thumbnail / Intro / Course Videos
- Playlist ordering supported
- Media order auto-adjust after delete
- JPQL used for order shifting

## API Endpoints
** 📁 Public


POST	Register User	/api/lms3/public/register-user
POST	Verify User	/api/lms3/public/verify-user
POST	User Sign In	/api/lms/auth/login
POST	Change Password (Forgot)	/api/lms3/public/forgot-password-change
GET	Get All Published Courses	/api/lms3/public/all-published-courses
GET	Get Course by Keyword	/api/lms3/public/get-public-courses?keyword=
GET	Get Public Course by ID	/api/lms3/public/get-public-course/{id}
GET	Get Courses by Category	/api/lms3/public/get-courses-category?category=
GET	Get All Reviews of a Course	/lms/api/public/get-allReview/{id}
GET	Get Public Instructor Profile	/api/lms/public/get-instructorProfile?instructor name=
GET	Get Cloudinary Resource	https://res.cloudinary.com/...

** 📂 User → Instructor


Method	Name	Endpoint
GET	Get Profile	/api/lms3/instructor/get-myProfile
PUT	Update Profile	/api/lms3/instructor/update-profile
GET	Get Stats	/api/lms3/instructor/get-stats
GET	Get Published Courses	/api/lms3/instructor/get-published-courses
GET	Get Draft Courses	/api/lms3/instructor/get-draft-courses
GET	Get Discontinued Courses	/api/lms3/instructor/get-discontinued-courses
GET	Get All Course Reviews	/api/lms3/instructor/get-allCourse-Reviews
GET	Get Reviews of Specific Course	/api/lms3/instructor/get-course-reviews/{id}
GET	Get My Reviews	/api/lms3/instructor/get-myReviews
GET	Activated Courses	/lms/api/instructor/get-allActiveCourses
GET	Deactivated Courses	/lms/api/instructor/get-allDeactivatedCourses
POST	Check Student Enrollment	/api/lms/instructor/check-enrollment/{id}?student-email=
PUT	Activate Course	/api/lms/course/activate-course/{id}
GET	Get All Enrollments	/api/lms/instructor/get-allEnrollments/{id}

** 📂 User → Learner


Method	Name	Endpoint
GET	Get Profile	/api/lms3/learner/get-myProfile
PUT	Update Profile	/api/lms3/learner/update-profile
PUT	Switch to Instructor	/api/lms3/learner/switch-profile
GET	Get Course Content by ID	/api/lms3/learner/get-course/{id}
GET	Get Media by Media ID	/api/lms3/learner/get-courseMedia/{courseId}/{mediaId}
PUT	Update Media Progress	/api/lms3/learner/update-media-progress/{id}
POST	Add Review to Course	/api/lms3/learner/addReview-Course/{id}
PUT	Check Course Completion	/api/lms3/course/mar-check/{courseId}/{mediaId}?check=

** 📂 User → Instructor → Course


Method	Name	Endpoint
POST	Register Course	/api/lms3/course/register-course
POST	Upload Media	/api/lms3/course/upload-media/{id}
PUT	Update Course	/api/lms3/course/update-course/{id}
PUT	Change Availability	/api/lms3/course/change-availability/{id}?choice=
GET	Get Media Preview	/api/lms3/course/media-preview/{id}?mediaType=
GET	Get Course by ID	/api/lms3/course/get-course/{id}
GET	Get All Courses	/api/lms3/course/get-courses
GET	Get Course Medias	/api/lms3/course/get-course-medias/{id}
DELETE	Delete Course Media	/api/lms3/course/delete-media/{id}

** 📂 User → Learner → Enrollment


Method	Name	Endpoint
POST	Enroll in Course	/api/lms3/learner/enroll/{courseId}
GET	Check Enrollment	/api/lms3/enrollment/isEnrolled/{courseId}
GET	Get My Enrollments	/api/lms3/enrollment/myEnrollments
GET	Get All My Enrollments	/api/lms/learner/get-allMyEnrollments

** 📂 User → User (Account)


Method	Name	Endpoint
POST	Verify for Password Change	/api/lms3/user/password-change-request/verify-account
POST	Change Password	/api/lms3/user/password-change?otp=&new password=

** 📁 Review


Method	Name	Endpoint
POST	Add Review to Course	/api/lms3/learner/addReview-Course/{id}
DELETE	Delete Review	/lms/api/review/delete-review/{id}

** 📁 Admin


Method	Name	Endpoint
GET	Get All Learners	/api/lms3/admin/all-learners
GET	Get All Instructors	/api/lms3/admin/all-instructors
PUT	Suspend User	/api/lms3/admin/suspend-user?userEmail=
GET	Get All Courses of Instructor	/api/lms3/admin/get-InstructorCourse?userEmail=
GET	Get Individual Course	/api/lms3/admin/get-course/{id}
GET	Get All Course Reviews	/api/lms3/admin/get-allCourseReview/{id}
GET	Get All Instructor Reviews	/api/lms3/admin/get-allInstructorReviews?instructorId=
GET	Get Student's Course Reviews	/api/lms3/admin/get-student-courseReviews?email=
GET	Get Student's Instructor Reviews	/api/lms3/admin/get-student-instructorReviews?email=


# How to Run
1. Clone repository
2. Configure application.yml
3. Add Cloudinary credentials
4. Add database config
5. Run Spring Boot application
