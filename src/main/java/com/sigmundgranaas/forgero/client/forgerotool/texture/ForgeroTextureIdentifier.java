package com.sigmundgranaas.forgero.client.forgerotool.texture;

import com.sigmundgranaas.forgero.item.forgerotool.model.ToolPartModelType;
import com.sigmundgranaas.forgero.item.forgerotool.toolpart.ForgeroToolPartTypes;

import java.util.Optional;

public interface ForgeroTextureIdentifier {
    boolean fileExists();

    String getFileNameWithExtension();

    String getRelativeTexturePath();

    String getFileNameWithoutExtension();

    String getBaseTextureFileNameWithExtension();

    Optional<ToolPartModelType> getModelType();

    Optional<ForgeroToolPartTypes> getToolPartType();

    Optional<String> getMaterial();
}
