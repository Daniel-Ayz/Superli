package HumanResources.ServiceLayer;

public class Response<T> {
    private T data;
    private boolean success;

    public Response(T data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
