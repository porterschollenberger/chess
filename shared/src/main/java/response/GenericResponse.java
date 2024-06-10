package response;

public class GenericResponse {
    private String message;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse() {}

    public String getMessage() {
        return message;
    }
}
