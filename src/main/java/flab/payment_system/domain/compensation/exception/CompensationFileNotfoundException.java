package flab.payment_system.domain.compensation.exception;

import flab.payment_system.common.exception.NotfoundException;

public class CompensationFileNotfoundException extends NotfoundException {

	public CompensationFileNotfoundException() {
		super();
		this.message = "file_not_exist";
	}
}
