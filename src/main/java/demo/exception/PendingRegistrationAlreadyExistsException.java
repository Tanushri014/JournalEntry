package demo.exception;

public class PendingRegistrationAlreadyExistsException extends  RuntimeException{
    public  PendingRegistrationAlreadyExistsException(String message){

        super(message);
    }

}
