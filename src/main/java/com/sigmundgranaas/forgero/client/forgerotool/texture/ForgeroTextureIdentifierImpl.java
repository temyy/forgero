package com.sigmundgranaas.forgero.client.forgerotool.texture;

import com.sigmundgranaas.forgero.client.forgerotool.model.ToolPartModelType;
import com.sigmundgranaas.forgero.core.tool.toolpart.ForgeroToolPartTypes;
import com.sigmundgranaas.forgero.item.Constants;
import com.sigmundgranaas.forgero.item.implementation.ToolPartItemImpl;
import com.sigmundgranaas.forgero.utils.FileSystemUtilities;
import com.sigmundgranaas.forgero.utils.ResourceUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
public class ForgeroTextureIdentifierImpl implements ForgeroTextureIdentifier {
    private final Identifier id;

    public ForgeroTextureIdentifierImpl(@NotNull Identifier id) {
        this.id = id;
    }

    @Override
    public boolean fileExists() {
        return FileSystemUtilities.resourceExists(FileSystemUtilities.getResourcePath() + getRelativeTexturePath());
    }

    @Override
    public String getFileNameWithExtension() {
        String texturePath = id.getPath();
        String[] elements = texturePath.split("/");
        if (!elements[elements.length - 1].endsWith(".png")) {
            elements[elements.length - 1] += ".png";
        }
        return elements[elements.length - 1];
    }

    @Override
    public String getRelativeTexturePath() {
        return ResourceUtils.ITEM_TEXTURES_FOLDER_PATH + getFileNameWithExtension();
    }

    @Override
    public String getFileNameWithoutExtension() {
        String texturePath = id.getPath();
        String[] elements = texturePath.split("/");
        return elements[elements.length - 1].replace(".png", "");
    }

    @Override
    public String getBaseTextureFileNameWithExtension() {
        String texturePath = id.getPath();
        String[] elements = texturePath.split("/");
        String[] materialSplit = elements[elements.length - 1].split("_");
        String[] extensionSplit = materialSplit[1].split("\\.");
        return extensionSplit[0] + Constants.BASE_IDENTIFIER + "." + extensionSplit[1];
    }

    @Override
    public Optional<ToolPartModelType> getModelType() {
        return ToolPartItemImpl.getToolPartModelTypeFromFileName(getFileNameWithoutExtension());
    }


    @Override
    public Optional<ForgeroToolPartTypes> getToolPartType() {
        return ToolPartItemImpl.getToolPartTypeFromFileName(getFileNameWithoutExtension());
    }

    @Override
    public Optional<String> getMaterial() {
        return ToolPartItemImpl.getMaterialFromFileName(getFileNameWithoutExtension());
    }
}
