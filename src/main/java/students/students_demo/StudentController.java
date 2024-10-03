package students.students_demo;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid; 
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// for REST
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
class StudentController {

  private final StudentRepository repository;
  private final StudentModelAssembler assembler;

  StudentController(StudentRepository repository, StudentModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Home Route
  @GetMapping("/")
  public ResponseEntity<String> home() {
    try {
      String homeMessage = "Welcome to the Student API!\n" +
        "Available endpoints:\n" +
        "GET /students - Retrieve all students\n" +
        "POST /students - Add a new student\n" +
        "GET /students/{id} - Retrieve a student by ID\n" +
        "PUT /students/{id} - Update a student by ID\n" +
        "DELETE /students/{id} - Delete a student by ID";
      return ResponseEntity.ok(homeMessage);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while accessing the home route.");
    }
  }

  // Retrieve all students
  @GetMapping("/students")
  public ResponseEntity<?> all() {
    try {
      List<EntityModel<Student>> students = repository.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

      CollectionModel<EntityModel<Student>> collectionModel = CollectionModel.of(
        students,
        linkTo(methodOn(StudentController.class).all()).withSelfRel()
      );

      return ResponseEntity.ok().body(new SuccessResponse("Students retrieved successfully", (collectionModel)));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while retrieving students.");
    }
  }

  // Add a new student
  @PostMapping("/students")
  public ResponseEntity<?> newStudent(@Valid @RequestBody Student newStudent) {
    try {
      EntityModel<Student> entityModel = assembler.toModel(repository.save(newStudent));

      return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(new SuccessResponse("Student created successfully", (entityModel)));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Failed to create a new student. Please check the input data.");
    }
  }

  // Retrieve a student by ID
  @GetMapping("/students/{id}")
  public ResponseEntity<?> one(@PathVariable Long id) {
    try {
      Student student = repository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

      EntityModel<Student> entityModel = assembler.toModel(student);
      return ResponseEntity.ok().body(new SuccessResponse("Student retrieved successfully", (entityModel)));
    } catch (StudentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while retrieving the student.");
    }
  }

  // Update a student by ID
  @PutMapping("/students/{id}")
  public ResponseEntity<?> replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
    try {
      Student updatedStudent = repository.findById(id)
        .map(student -> {
          // Update only the fields that are not null in the incoming request
          if (newStudent.getFirstName() != null) {
            student.setFirstName(newStudent.getFirstName());
          }
          if (newStudent.getLastName() != null) {
            student.setLastName(newStudent.getLastName());
          }
          if (newStudent.getDepartment() != null) {
            student.setDepartment(newStudent.getDepartment());
          }
          if (newStudent.getLevel() != null) {
            student.setLevel(newStudent.getLevel());
          }
          return repository.save(student);
        })
        .orElseThrow(() -> new StudentNotFoundException(id));

      EntityModel<Student> entityModel = assembler.toModel(updatedStudent);

      return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(new SuccessResponse("Student updated successfully", (entityModel)));
    }  catch (StudentNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("Student not found", e.getMessage()));
  } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Failed to update the student. Please check the input data.");
    }
  }

  //Delete a student by ID
  @DeleteMapping("/students/{id}")
  public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
    try {
      if (!repository.existsById(id)) {
        throw new StudentNotFoundException(id);
      }

      repository.deleteById(id);
      // return ResponseEntity.noContent().build();
      return ResponseEntity.ok().body(new SuccessResponse("Student deleted successfully", null));
    } catch (StudentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while deleting the student.");
    }
  }
}
