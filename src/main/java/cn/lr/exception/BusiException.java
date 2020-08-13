package cn.lr.exception;

public class BusiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public BusiException() {
		super();
	}
	
	public BusiException(String msg) {
		super(msg);
	}
}
