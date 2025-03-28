package vn.bromel.jobhunter.util.error;

public class IdInvalidException extends RuntimeException{
    String message;
    public IdInvalidException(String message) {super(message);}
}
