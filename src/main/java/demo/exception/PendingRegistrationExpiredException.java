package demo.exception;

public class PendingRegistrationExpiredException extends RuntimeException {
    public PendingRegistrationExpiredException(String m){
        super(m);
    }

}
