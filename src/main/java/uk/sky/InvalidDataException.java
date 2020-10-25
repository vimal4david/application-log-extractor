package uk.sky;

class InvalidDataException extends RuntimeException{
    public InvalidDataException(String errorMessage) {
        super(errorMessage);
    }
}
