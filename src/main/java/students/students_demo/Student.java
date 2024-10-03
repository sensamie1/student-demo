package students.students_demo;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
class Student {

  private @Id
  @GeneratedValue Long id;
  @NotBlank(message = "First name cannot be empty")
  private String firstName;

  @NotBlank(message = "Last name cannot be empty")
  private String lastName;

  @NotBlank(message = "Department cannot be empty")
  private String department;

  @NotNull(message = "Level cannot be null")
  private Integer level;

  Student() {}

  Student(String firstName, String lastName, String department, Integer level) {

    this.firstName = firstName;
    this.lastName = lastName;
    this.department = department;
    this.level = level;
  }

  public Long getId() {
    return this.id;
  }

  public String getFirstName() {
    return this.firstName;
  }
  public String getLastName() {
    return this.lastName;
  }

  public String getDepartment() {
    return this.department;
  }

  public Integer getLevel() {
    return this.level;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Student))
      return false;
      Student student = (Student) o;
    return Objects.equals(this.id, student.id) && Objects.equals(this.firstName, student.firstName)
      && Objects.equals(this.lastName, student.lastName) && Objects.equals(this.department, student.department) && Objects.equals(this.level, student.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.firstName, this.lastName, this.department, this.level);
  }

  @Override
  public String toString() {
    return "Student{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
      + '\'' + ", department='" + this.department + '\'' + ", level='" + this.level + '\'' + '}';
  }
}
