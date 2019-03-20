package model.exceptions;

public class InvalidProgressException extends IllegalArgumentException {
    public InvalidProgressException(){
    }

    public InvalidProgressException(String string) {
        super(string);
    }
}


