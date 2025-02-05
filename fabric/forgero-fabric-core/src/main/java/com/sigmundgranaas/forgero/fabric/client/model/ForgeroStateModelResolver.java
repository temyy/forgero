package com.sigmundgranaas.forgero.fabric.client.model;

import java.util.Optional;
import java.util.Set;

import com.sigmundgranaas.forgero.core.configuration.ForgeroConfigurationLoader;
import com.sigmundgranaas.forgero.core.model.ModelRegistry;
import com.sigmundgranaas.forgero.core.state.State;
import com.sigmundgranaas.forgero.minecraft.common.client.model.unbaked.UnbakedStateModel;
import com.sigmundgranaas.forgero.minecraft.common.service.StateService;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;

public class ForgeroStateModelResolver implements ModelResolver {
	private final ModelRegistry registry;
	private final StateService service;
	private final Set<Identifier> models;

	public ForgeroStateModelResolver(ModelRegistry registry, StateService service, Set<Identifier> models) {
		this.registry = registry;
		this.service = service;
		this.models = models;
	}

	@Override
	public @Nullable UnbakedModel resolveModel(Context context) {
		if (models.contains(context.id())) {
			Identifier id = new Identifier(context.id().getNamespace(), context.id().getPath().replace("item/", ""));
			Optional<State> state = service.find(id);
			if (state.isPresent()) {
				return new UnbakedStateModel(registry, service, ForgeroConfigurationLoader.configuration.modelStrategy, state.get());

			}
		}
		return null;
	}
}
