package demo.exception;
public class PendingRegistrationNotFoundException extends RuntimeException{
    public PendingRegistrationNotFoundException(String m){
        super(m);
    }

}
