package demo.exception;

import demo.repository.OtpRepository;

public class OtpExpiredException extends  RuntimeException{

public OtpExpiredException(String m){
    super(m);
}

}
