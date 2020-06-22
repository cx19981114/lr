package cn.lr.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BaseException() {
		super();
	}
	
	public BaseException(String msg) {
		super(msg);
	}
}
