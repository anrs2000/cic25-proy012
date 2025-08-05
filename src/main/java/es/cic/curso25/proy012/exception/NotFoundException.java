package es.cic.curso25.proy012.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(){
        super();
    }
    public NotFoundException(String mensaje, Throwable error){
        super(mensaje, error);
    }
    public NotFoundException(String mensaje){
        super(mensaje);
    }
    public NotFoundException(Throwable error){
        super(error);
    }
}
