package response;

public class GenericResponse extends spark.Response {
    private String message;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse() {}
}
