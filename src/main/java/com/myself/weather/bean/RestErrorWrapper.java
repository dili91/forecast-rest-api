package com.myself.weather.bean;

import javax.validation.constraints.NotNull;

/**
 * Bean for custom REST errors. This is used by application custom error handler
 * 
 * @author andreadilisio
 *
 */
public class RestErrorWrapper {
	@NotNull
	private Error error;
	
	public RestErrorWrapper(String code, String message) {
		this.error = new Error(code, message);
	}

	public Error getError() {
		return error;
	}

	class Error {
		@NotNull
		private String code;
		@NotNull
		private String message;

		public Error(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
