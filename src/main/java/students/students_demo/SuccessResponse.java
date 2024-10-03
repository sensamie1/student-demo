package students.students_demo;

public class SuccessResponse {
  private String message;
  private Object data;

  public SuccessResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  // Getters and setters
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
