package com.sigmundgranaas.forgero.core.data.factory;

import com.sigmundgranaas.forgero.core.data.ForgeroDataResource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract class for managing the creation of Forgero content from data files.
 * This class handles the general structure and logic in data files.
 * Parent relationships and dependencies are common for all Resources, and will be handled here.
 * More advanced attributes needs to be handled by an inheritor
 *
 * @param <T> The type of the POJO object
 * @param <R> The resulting resource type
 */
public abstract class DataResourceFactory<T extends ForgeroDataResource, R> {
    Map<String, T> pojos = new HashMap<>();
    Set<String> availableNameSpaces = new HashSet<>();

    public DataResourceFactory() {
    }

    public DataResourceFactory(List<T> pojos, Set<String> availableNameSpaces) {
        this.pojos = pojos.stream().collect(Collectors.toMap(pojo -> pojo.name, pojo -> pojo));
        this.availableNameSpaces = availableNameSpaces;
    }

    public static <T> T replaceAttributesDefault(T attribute1, T attribute2, T defaultAttribute) {
        if (attribute1 == null && attribute2 == null)
            return defaultAttribute;
        else return Objects.requireNonNullElse(attribute1, attribute2);
    }

    public static <T> T attributeOrDefault(T attribute1, T defaultAttribute) {
        return Objects.requireNonNullElse(attribute1, defaultAttribute);
    }

    public static <T> List<T> mergeAttributes(List<T> attribute1, List<T> attribute2) {
        if (attribute1 == null && attribute2 == null)
            return Collections.emptyList();
        else if (attribute1 != null && attribute2 != null) {
            return Stream.of(attribute1, attribute2).flatMap(List::stream).distinct().toList();
        } else return Objects.requireNonNullElse(attribute1, attribute2);
    }

    /**
     * Method to build a desired Resource from a corresponding pojo.
     * Will return a resource if the pojo is valid.
     * <p>
     * Will return empty if the resource cannot be parsed.
     * This can be because the data file is invalid, or because its missing dependencies.
     *
     * @param pojo input node
     * @return Optional Resource built from the pojo.
     */
    public Optional<R> buildResource(T pojo) {
        if (pojo.abstractResource || !resolveDependencies(pojo)) {
            return Optional.empty();
        }
        if (pojo.parent == null) {
            return createResource(pojo);
        }
        return assemblePojoFromParent(pojo).flatMap(this::createResource);
    }

    private boolean resolveDependencies(T pojo) {
        return pojo.dependencies == null || availableNameSpaces.containsAll(pojo.dependencies);
    }

    protected abstract Optional<R> createResource(T pojo);

    protected abstract T mergePojos(T parent, T child);

    /**
     * Recursive method for handling parent relationships between resources.
     * This method will recursively dig through the parent/child relationships and merge resources on its way back up the recursions tree.
     *
     * @param pojo node
     * @return Merged Pojo from pojo and parent. Will return pojo if parent is null
     */
    private Optional<T> assemblePojoFromParent(T pojo) {
        if (pojo.parent == null) {
            return Optional.of(pojo);
        }
        if (pojos.containsKey(pojo.parent)) {
            var parentOpt = assemblePojoFromParent(pojos.get(pojo.parent));
            return parentOpt
                    .map(simpleMaterialPOJO -> mergePojos(simpleMaterialPOJO, pojo))
                    .or(() -> Optional.of(pojo));
        }
        return Optional.empty();
    }
}
