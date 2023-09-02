package cn.aistore.ai.service.exception;

public class NoItemExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public NoItemExistsException(final String message) {
        super(message);
    }
}
