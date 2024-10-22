package vn.hoidanit.jobhunter.util.error;

public class IdInvalidException extends Exception{
    String message;
    public IdInvalidException(String message) {super(message);}
}
