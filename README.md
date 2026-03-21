# LMS3 — Learning Management System API

A backend REST API for a Learning Management System built with Spring Boot.
Supports course creation, media upload, enrollment, JWT authentication, and role-based authorization.

> This project does not include a UI. All operations are performed via REST APIs.

---

## Tech Stack

| Layer              | Technology                    |
|--------------------|-------------------------------|
| Language           | Java 17                       |
| Framework          | Spring Boot                   |
| Security           | Spring Security + JWT         |
| ORM                | Spring Data JPA / Hibernate   |
| Database           | MySQL                         |
| Build Tool         | Maven                         |
| Media Storage      | Cloudinary                    |
| API Documentation  | Swagger (SpringDoc OpenAPI)   |
| API Testing        | Postman                       |

---

## Features

- JWT-based authentication and authorization
- Role-based access control — ADMIN, INSTRUCTOR, LEARNER
- Course creation, publishing, drafting, and discontinuation
- Course media upload — Thumbnail, Intro Video, Course Videos
- Ordered media playlist with auto-reorder after deletion
- Course enrollment system with cancellation support
- Public course listing, keyword search, and category filtering
- Account status management — ACTIVE, SUSPENDED, DEACTIVATED
- Global exception handling with structured JSON responses
- Full transactional service logic

---

## Roles & Permissions

### ADMIN
- Manage users — suspend, view learners and instructors
- View all courses, reviews, and enrollments

### INSTRUCTOR
- Create and manage courses
- Upload and manage course media with playlist ordering
- View enrollments and course/instructor reviews

### LEARNER
- Browse and search public courses
- Enroll in and cancel courses
- Watch videos and track media progress
- Add and delete course reviews
- Switch profile to Instructor

---

## Project Structure

```
src/
├── controller/             REST controllers per role
├── service/                Business logic interfaces and implementations
├── repository/             Spring Data JPA repositories
├── entity/                 JPA entity classes
├── dto/
│   ├── CourseDTO/
│   │   ├── CourseReviewDTO     — review request and response for a course
│   │   ├── EnrollmentDTO       — enrollment request and response
│   │   └── MediaDTO            — media upload, preview, and progress DTOs
│   └── UserDTO/
│       ├── InstructorDTO       — instructor profile, stats, and course overview DTOs
│       └── StudentDTO          — student profile, course content, and progress DTOs
├── exception/              Custom exceptions and global exception handler
├── config/                 Security, Cloudinary, and Swagger configuration
└── resources/
    ├── application.properties
    └── application.yml
```

---

## Environment Variables

Set these in your `application.yml` before running:

```yml
CLOUDINARY_CLOUD_NAME: your_cloud_name
CLOUDINARY_API_KEY: your_api_key
CLOUDINARY_SECRET_KEY: your_secret_key
DB_URL: jdbc:mysql://localhost:3306/lms3
DB_USERNAME: your_db_username
DB_PASSWORD: your_db_password
```

---

## Media Handling

- All media files stored on **Cloudinary**
- Supported media types: `THUMBNAIL`, `INTRO`, `VIDEO`
- Each course supports an ordered media playlist
- On media deletion, playlist order auto-adjusts using JPQL order-shift queries

---

## Swagger API Documentation

Swagger UI is integrated using **SpringDoc OpenAPI**.

| Resource            | URL                                              |
|---------------------|--------------------------------------------------|
| Swagger UI          | `http://localhost:8080/swagger-ui/index.html`    |
| OpenAPI JSON Spec   | `http://localhost:8080/v3/api-docs`              |

### How to use Swagger with JWT

1. Run the application
2. Open `http://localhost:8080/swagger-ui/index.html`
3. Call `POST /api/lms3/auth/login` to get your JWT token
4. Click the **Authorize** button at the top right of Swagger UI
5. Enter your token as `Bearer your_token_here`
6. All secured endpoints are now accessible directly from Swagger UI

### SpringDoc dependency

Add to `pom.xml` if not already present:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### Permit Swagger in Security Config

Add these paths to your `SecurityFilterChain`:

```java
.requestMatchers(
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**"
).permitAll()
```

---

## API Endpoints

Base URL: `http://localhost:8080/api/lms3`

---

### Public — No authentication required

| Method     | Endpoint                                    | Description                              |
|------------|---------------------------------------------|------------------------------------------|
| `POST`     | `/public/register-user`                     | Register a new user                      |
| `POST`     | `/public/verify-user`                       | Request OTP for forgot password          |
| `POST`     | `/auth/login`                               | Sign in — returns JWT token              |
| `POST`     | `/public/forgot-password-change`            | Change forgotten password using OTP      |
| `GET`      | `/public/all-published-courses`             | Get all publicly available courses       |
| `GET`      | `/public/get-public-courses?keyword=`       | Search courses by keyword                |
| `GET`      | `/public/get-public-course/{id}`            | Get a specific public course by ID       |
| `GET`      | `/public/get-courses-category?category=`    | Get courses filtered by category         |

---

### User — Authentication required

| Method     | Endpoint                                              | Description                              |
|------------|-------------------------------------------------------|------------------------------------------|
| `POST`     | `/user/password-change-request/verify-account`        | Verify account before password change    |
| `POST`     | `/user/password-change?otp=&new password=`            | Change password using OTP                |
| `GET`      | `/user/account-status`                                | Get current account status               |
| `GET`      | `/user/get-roles`                                     | Get assigned roles                       |

---

### Learner — Role: `LEARNER`

| Method     | Endpoint                                              | Description                              |
|------------|-------------------------------------------------------|------------------------------------------|
| `GET`      | `/learner/get-myProfile`                              | Get learner profile                      |
| `PUT`      | `/learner/update-profile`                             | Update learner profile                   |
| `PUT`      | `/learner/update-status?status=`                      | Update account status                    |
| `PUT`      | `/learner/switch-profile`                             | Switch learner profile to instructor     |
| `GET`      | `/learner/get-course/{id}`                            | Get enrolled course content              |
| `GET`      | `/learner/get-courseMedia/{courseId}/{mediaId}`        | Get specific media content               |
| `PUT`      | `/learner/update-media-progress/{id}`                 | Update media watch progress              |
| `POST`     | `/learner/addReview-Course/{id}`                      | Add a review to a course                 |
| `DELETE`   | `/learner/deleteReview-Course/{id}`                   | Delete own course review                 |

---

### Enrollment — Role: `LEARNER`

| Method     | Endpoint                                    | Description                              |
|------------|---------------------------------------------|------------------------------------------|
| `POST`     | `/enrollment/enroll/{courseId}`             | Enroll in a course                       |
| `GET`      | `/enrollment/isEnrolled/{courseId}`         | Check enrollment status                  |
| `GET`      | `/enrollment/myEnrollments`                 | Get all enrolled courses                 |
| `PUT`      | `/enrollment/cancel-enrollment/{courseId}`  | Cancel an enrollment                     |

---

### Instructor — Role: `INSTRUCTOR`

| Method     | Endpoint                                    | Description                              |
|------------|---------------------------------------------|------------------------------------------|
| `GET`      | `/instructor/get-myProfile`                 | Get instructor profile                   |
| `PUT`      | `/instructor/update-profile`                | Update instructor profile                |
| `GET`      | `/instructor/get-stats`                     | Get course statistics dashboard          |
| `GET`      | `/instructor/get-published-courses`         | Get all published courses                |
| `GET`      | `/instructor/get-draft-courses`             | Get all draft courses                    |
| `GET`      | `/instructor/get-discontinued-courses`      | Get all discontinued courses             |
| `PUT`      | `/instructor/publish-course/{id}`           | Publish a course                         |
| `PUT`      | `/instructor/draft-course/{id}`             | Move a course to draft                   |
| `PUT`      | `/instructor/discontinue-course/{id}`       | Discontinue a course                     |
| `GET`      | `/instructor/get-allCourse-Reviews`         | Get all reviews across courses           |
| `GET`      | `/instructor/get-course-reviews/{id}`       | Get reviews for a specific course        |
| `GET`      | `/instructor/get-myReviews`                 | Get all instructor-level reviews         |

---

### Course — Role: `INSTRUCTOR`

| Method     | Endpoint                                              | Description                              |
|------------|-------------------------------------------------------|------------------------------------------|
| `POST`     | `/course/register-course`                             | Create and register a new course         |
| `POST`     | `/course/upload-media/{id}?mediaType=`                | Upload media to a course                 |
| `PUT`      | `/course/update-course/{id}`                          | Update course details                    |
| `PUT`      | `/course/change-availability/{id}?choice=`            | Toggle course availability               |
| `GET`      | `/course/media-preview/{id}?mediaType=`               | Preview uploaded media                   |
| `GET`      | `/course/get-course/{id}`                             | Get full course details                  |
| `GET`      | `/course/get-course-medias/{id}`                      | Get all media of a course                |
| `PUT`      | `/course/mark-check/{courseId}/{mediaId}?check=`      | Mark media as completed                  |
| `DELETE`   | `/course/delete-media/{id}`                           | Delete a media from course               |

---

### Admin — Role: `ADMIN`

| Method     | Endpoint                                              | Description                              |
|------------|-------------------------------------------------------|------------------------------------------|
| `GET`      | `/admin/all-learners`                                 | Get all registered learners              |
| `GET`      | `/admin/all-instructors`                              | Get all registered instructors           |
| `PUT`      | `/admin/suspend-user?userEmail=`                      | Suspend a user account                   |
| `GET`      | `/admin/get-InstructorCourse?userEmail=`              | Get courses of a specific instructor     |
| `GET`      | `/admin/get-course/{id}`                              | Get individual course details            |
| `GET`      | `/admin/get-allCourseReview/{id}`                     | Get all reviews for a course             |
| `GET`      | `/admin/get-allInstructorReviews?instructorId=`       | Get all reviews of an instructor         |
| `GET`      | `/admin/get-student-courseReviews?email=`             | Get course reviews by a student          |
| `GET`      | `/admin/get-student-instructorReviews?email=`         | Get instructor reviews by a student      |

---

## How to Run

```bash
# 1. Clone the repository
git clone https://github.com/your-username/lms3.git

# 2. Navigate into the project directory
cd lms3

# 3. Configure application.yml with your database and Cloudinary credentials

# 4. Run the application
./mvnw spring-boot:run
```

Application starts at `http://localhost:8080`

Swagger UI available at `http://localhost:8080/swagger-ui/index.html`

---

## Author

**Gaurav Soni**
Backend Dev — Java, Spring Boot
