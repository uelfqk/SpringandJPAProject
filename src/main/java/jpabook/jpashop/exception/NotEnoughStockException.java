package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {

    // Exception 을 만들때 Exception Trace 등을 위해서 아래 메소드를 오버라이드 하는것이 좋다.
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
