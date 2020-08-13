package cn.lr.exception;

public class EmployeeUnactiveException extends BaseException {

	private static final long serialVersionUID = 1L;

	public EmployeeUnactiveException() {
		super();
	}
	
	public EmployeeUnactiveException(String msg) {
		super(msg);
	}
}
