package br.my.company.keycloak.storage.rest;

public class RESTIdentityStoreException extends RuntimeException {

	/**
	 * @author <a href="mailto:wallacerock@gmail.com">Wallace Roque</a>
	 */
	private static final long serialVersionUID = 1658007737503835157L;
	
	public RESTIdentityStoreException() {}
	
	public RESTIdentityStoreException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public RESTIdentityStoreException(Throwable cause) {
	    super(cause);
	}
}
