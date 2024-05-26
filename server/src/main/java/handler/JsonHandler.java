package handler;

@FunctionalInterface
public interface JsonHandler {
    String handle(spark.Request request, spark.Response response);
}