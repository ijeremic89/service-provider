package serviceProvider.serviceProvider.exceptions;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(Long id) {
        super("Could not find service with Id: " + id);
    }
}
