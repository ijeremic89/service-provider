package serviceProvider.serviceProvider.provider;

import java.util.List;

import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;
import serviceProvider.serviceProvider.provider.model.ProviderDTO;

public interface ProviderService {
    /**
     * Finds a provider by its unique ID.
     *
     * @param id the ID of the provider to find
     * @return the found {@link ProviderDTO} if a provider with the specified ID exists
     * @throws ProviderNotFoundException if no provider is found with the given ID
     */
    ProviderDTO findProviderById(Long id);

    /**
     * Retrieves all providers.
     *
     * @return a list of {@link ProviderDTO} representing all providers. The list may be empty if no providers are found.
     */
    List<ProviderDTO> findAllProviders();

    /**
     * Creates a new provider.
     *
     * @param providerDTO the {@link ProviderDTO} containing the details of the provider to create
     * @return the created {@link ProviderDTO} with its ID and other generated values filled in
     * @throws IllegalArgumentException if the provided {@link ProviderDTO} contains invalid data
     */
    ProviderDTO createProvider(ProviderDTO providerDTO);

    /**
     * Updates an existing provider.
     *
     * @param id the ID of the provider to update
     * @param provider the {@link ProviderDTO} containing the updated details of the provider
     * @return the updated {@link ProviderDTO}
     * @throws ProviderNotFoundException if no provider is found with the given ID
     * @throws IllegalArgumentException if the provided {@link ProviderDTO} contains invalid data
     */
    ProviderDTO updateProvider(Long id, ProviderDTO provider);

    /**
     * Deletes a provider by its ID.
     *
     * @param id the ID of the provider to delete
     * @return a confirmation message indicating the successful deletion
     * @throws ProviderNotFoundException if no provider is found with the given ID
     */
    String deleteProvider(Long id);

    /**
     * Finds providers by specified search criteria.
     * <p>
     * This method allows for dynamic filtering of providers based on their name and the ID of a service they offer.
     * Both criteria are optional and can be used together or individually to narrow down the search results.
     * <p>
     * The search by name is case-insensitive, meaning it does not consider the case of the letters when matching
     * provider names. The search by service ID is an exact match, targeting providers that offer a specific service
     * identified by the provided ID.
     *
     * @param name Optional. The name of the provider or a part of it to search for. If {@code null} or empty,
     *             the search will not be restricted by name.
     * @param serviceId Optional. The ID of the service to search for providers that offer this service.
     *                  If {@code null}, the search will not be restricted by service ID.
     * @return A list of {@link ProviderDTO} objects that match the search criteria. The list may be empty if
     *         no providers match the criteria.
     */
    List<ProviderDTO> findProvidersByCriteria(String name, Long serviceId);
}
