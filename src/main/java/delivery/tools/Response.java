package delivery.tools;

public class Response {
    private String message;
    private boolean error;
    private Object obj = null;

    public Response(String message, boolean error, Object obj) {
        this.message = message;
        this.error = error;
        this.obj = obj;
    }

    public Response(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public Response(String message) {
        this.message = message;
        this.error = true;
    }

    public Response() {
        this.message = "success";
        this.error = false;
    }

    public Response(Object obj) {
        this.message = "success";
        this.error = false;
        this.obj = obj;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasError() {
        return error;
    }

    public Object getData() {
        return obj;
    }
}
