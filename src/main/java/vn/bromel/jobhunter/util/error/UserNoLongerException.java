package vn.bromel.jobhunter.util.error;



public class UserNoLongerException extends RuntimeException{
    String message;
    public UserNoLongerException(String message) {super(message);}
}
