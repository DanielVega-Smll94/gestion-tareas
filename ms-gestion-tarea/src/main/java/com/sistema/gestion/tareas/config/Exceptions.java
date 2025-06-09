package com.sistema.gestion.tareas.config;

public class Exceptions {
	
	public static class BadRequestException extends RuntimeException {
	    /**
		 * 
		 */
		private static final long serialVersionUID = -3873395088731168154L;

		public BadRequestException(String message) {
	        super(message);
	    }
	}

	public static class ResourceNotFoundException extends RuntimeException {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ResourceNotFoundException(String message) {
	        super(message);
	    }
	}

}
