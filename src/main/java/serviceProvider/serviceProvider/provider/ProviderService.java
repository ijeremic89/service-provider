package serviceProvider.serviceProvider.provider;

import java.util.List;

import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;

public interface ProviderService {
    /**
     * Finds a provider by its unique ID.
     *
     * @param id the ID of the provider to find
     * @return the found {@link ProviderDto} if a provider with the specified ID exists
     * @throws ProviderNotFoundException if no provider is found with the given ID
     */
    ProviderDto findProviderById(Long id);

    /**
     * Retrieves all providers.
     *
     * @return a list of {@link ProviderDto} representing all providers. The list may be empty if no providers are found.
     */
    List<ProviderDto> findAllProviders();

    /**
     * Creates a new provider.
     *
     * @param providerDto the {@link ProviderDto} containing the details of the provider to create
     * @return the created {@link ProviderDto} with its ID and other generated values filled in
     * @throws IllegalArgumentException if the provided {@link ProviderDto} contains invalid data
     */
    ProviderDto createProvider(ProviderDto providerDto);

    /**
     * Updates an existing provider.
     *
     * @param id the ID of the provider to update
     * @param provider the {@link ProviderDto} containing the updated details of the provider
     * @return the updated {@link ProviderDto}
     * @throws ProviderNotFoundException if no provider is found with the given ID
     * @throws IllegalArgumentException if the provided {@link ProviderDto} contains invalid data
     */
    ProviderDto updateProvider(Long id, ProviderDto provider);

    /**
     * Deletes a provider by its ID.
     *
     * @param id the ID of the provider to delete
     * @return a confirmation message indicating the successful deletion
     * @throws ProviderNotFoundException if no provider is found with the given ID
     */
    String deleteProvider(Long id);
}
