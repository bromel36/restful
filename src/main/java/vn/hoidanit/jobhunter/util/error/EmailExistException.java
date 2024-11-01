package vn.hoidanit.jobhunter.util.error;


public class EmailExistException extends RuntimeException {
    String message;
    public EmailExistException(String message) {super(message);}
}
