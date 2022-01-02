package com.sigmundgranaas.forgero.registry;

import com.google.gson.JsonElement;
import com.sigmundgranaas.forgero.recipe.RecipeCollection;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class RecipeRegistryImpl implements RecipeRegistry {
    private static RecipeRegistry INSTANCE;
    private final RecipeCollection collection;

    public RecipeRegistryImpl(RecipeCollection collection) {
        this.collection = collection;
    }

    public static RecipeRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeRegistryImpl(RecipeCollection.INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public void registerRecipes(Map<Identifier, JsonElement> map) {
        collection.getRecipes().forEach(recipeWrapper -> map.put(recipeWrapper.getRecipeID(), recipeWrapper.getRecipe()));
    }

    @Override
    public void registerRecipeSerializers() {
        collection.getRecipeTypes().forEach(serializer -> Registry.register(Registry.RECIPE_SERIALIZER, serializer.getIdentifier(), serializer.getSerializer()));
    }
}
