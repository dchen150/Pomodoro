package model.exceptions;

public class NegativeInputException extends IllegalArgumentException {
    public NegativeInputException(){
    }

    public NegativeInputException(String string) {
        super(string);
    }
}
