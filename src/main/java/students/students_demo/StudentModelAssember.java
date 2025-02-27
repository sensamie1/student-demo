package students.students_demo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {

  @Override
  public EntityModel<Student> toModel(Student student) {

    return EntityModel.of(student, //
      linkTo(methodOn(StudentController.class).one(student.getId())).withSelfRel(),
      linkTo(methodOn(StudentController.class).all()).withRel("students"));
  }
}