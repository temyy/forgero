package com.sigmundgranaas.forgero.minecraft.common.client.model.unbaked;

import static net.minecraft.client.render.model.ModelRotation.X0_Y0;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.gson.JsonObject;
import com.sigmundgranaas.forgero.core.Forgero;
import com.sigmundgranaas.forgero.core.model.ModelTemplate;
import com.sigmundgranaas.forgero.core.model.PaletteTemplateModel;
import com.sigmundgranaas.forgero.core.model.TextureModel;
import com.sigmundgranaas.forgero.core.texture.utils.Offset;
import com.sigmundgranaas.forgero.minecraft.common.mixins.JsonUnbakedModelOverrideMixin;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class Unbaked2DTexturedModel implements UnbakedDynamicModel {
	public static final String TRANSPARENT_BASE_IDENTIFIER = "transparent_base";
	private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
	private final List<ModelTemplate> textures;
	private final List<JsonObject> displayOverrides;
	private final Map<String, Offset> offsetMap;
	private final Map<String, Integer> resolutionMap;
	private final Map<String, Integer> indexMap;
	private final String id;
	private final Baker loader;
	private final Function<SpriteIdentifier, Sprite> textureGetter;
	private final String parent;

	public Unbaked2DTexturedModel(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, List<ModelTemplate> textures, String id) {
		this.loader = loader;
		this.textureGetter = textureGetter;
		this.textures = textures;
		this.id = id;
		this.offsetMap = new HashMap<>();
		this.resolutionMap = new HashMap<>();
		this.indexMap = new HashMap<>();
		this.displayOverrides = new ArrayList<>();
		this.textures.sort(Comparator.comparing(ModelTemplate::order));
		this.parent = this.textures.stream()
				.map(ModelTemplate::getParent)
				.flatMap(Optional::stream)
				.findAny()
				.orElse("minecraft:item/handheld");
	}

	private String textureName(ModelTemplate model) {
		if (model instanceof PaletteTemplateModel paletteTemplateModel) {
			return getTextureBasePath() + String.format("%s-%s", paletteTemplateModel.palette(), paletteTemplateModel.template().replace(".png", ""));
		}
		return ((TextureModel) model).texture();
	}

	public String BuildJsonModel() {
		JsonObject model = new JsonObject();
		model.addProperty("parent", this.parent);
		model.add("textures", this.getTextures());
		model.addProperty("gui_light", "front");
		if (!this.displayOverrides.isEmpty()) {
			model.add("display", displayOverrides.get(0));
		}
		return model.toString();
	}

	protected String getTextureBasePath() {
		return Forgero.NAMESPACE + ":item/";
	}

	protected JsonObject getTextures() {
		JsonObject jsonTextures = new JsonObject();
		if (!this.textures.isEmpty()) {
			for (int i = 0; i < this.textures.size(); i++) {
				var texture = textureName(this.textures.get(i));
				var layer = "layer" + i;
				this.offsetMap.put(layer, textures.get(i).getOffset().orElse(new Offset(0, 0)));
				this.resolutionMap.put(layer, textures.get(i).getResolution());
				textures.get(i).getDisplayOverrides().ifPresent(displayOverrides::add);
				this.indexMap.put(layer, i + 1);
				jsonTextures.addProperty(layer, texture);
			}
		} else {
			jsonTextures.addProperty("layer" + 1, getTextureBasePath() + TRANSPARENT_BASE_IDENTIFIER);
		}
		return jsonTextures;
	}

	public JsonUnbakedModel buildUnbakedJsonModel() {
		return JsonUnbakedModel.deserialize(BuildJsonModel());
	}

	public String getIdentifier() {
		return id;
	}

	private void applyOffset(ModelElement element) {
		for (ModelElementFace face : element.faces.values()) {
			var offset = offsetMap.getOrDefault(face.textureId, new Offset(0, 0));
			if (offset.x() != 0 || offset.y() != 0) {
				element.from.add(offset.x(), offset.y(), 0);
				element.to.add(offset.x(), offset.y(), 0);
				break;
			}

		}
		for (ModelElementFace face : element.faces.values()) {
			int index = this.indexMap.getOrDefault(face.textureId, 1);
			if (element.faces.containsKey(Direction.SOUTH) || element.faces.containsKey(Direction.NORTH)) {
				element.from.add(0, 0, -0.001f * index);
				element.to.add(0, 0, 0.001f * index);
			}
			if (element.faces.containsKey(Direction.WEST) || element.faces.containsKey(Direction.EAST)) {
				element.from.add(index * 0.001f, 0, 0);
				element.to.add(index * -0.001f, 0, 0);
			}
			if (element.faces.containsKey(Direction.DOWN) || element.faces.containsKey(Direction.UP)) {
				element.from.add(0, index * -0.001f, 0);
				element.to.add(0, index * 0.001f, 0);
			}
		}

	}

	private void applyResolutionScaling(ModelElement element) {
		Integer topResolution = resolutionMap.values().stream().reduce(16, Integer::max);
		if (topResolution.equals(16)) {
			return;
		}

		for (ModelElementFace face : element.faces.values()) {
			var res = resolutionMap.getOrDefault(face.textureId, 16);
			if (!res.equals(topResolution)) {
				float initialZ = element.from.z();
				float scale = (float) res / topResolution;
				element.from.mul(scale, scale, 1);
				element.to.mul(scale, scale, 1);

				float z = element.from.z();
				element.from.add(0, 0, z - initialZ);
				element.to.add(0, 0, z - initialZ);
				break;
			}
		}
	}

	@Override
	public BakedModel bake() {
		var parentModel = loader.getOrLoadModel(new Identifier(this.parent));
		JsonUnbakedModel model = this.buildUnbakedJsonModel();
		ModelIdentifier id = this.getId();
		JsonUnbakedModel generated_model = ITEM_MODEL_GENERATOR.create(textureGetter, model);

		for (ModelElement element : generated_model.getElements()) {
			applyOffset(element);
		}

		for (ModelElement element : generated_model.getElements()) {
			applyResolutionScaling(element);
		}

		if (parentModel instanceof JsonUnbakedModel jsonParent) {
			((JsonUnbakedModelOverrideMixin) generated_model).setParent(jsonParent);
			return generated_model.bake(loader, jsonParent, textureGetter, X0_Y0, id, true);
		}

		return generated_model.bake(loader, model, textureGetter, X0_Y0, id, true);

	}

	@Override
	public ModelIdentifier getId() {
		return new ModelIdentifier(Forgero.NAMESPACE, getIdentifier(), "inventory");
	}
}
