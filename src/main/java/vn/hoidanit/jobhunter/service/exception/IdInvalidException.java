package vn.hoidanit.jobhunter.service.exception;

public class IdInvalidException extends Exception{
    String message;
    public IdInvalidException(String message) {super(message);}
}
