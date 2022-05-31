package com.sigmundgranaas.forgero.core.toolpart.toolpart;

import com.sigmundgranaas.forgero.Constants;
import com.sigmundgranaas.forgero.core.ForgeroRegistry;
import com.sigmundgranaas.forgero.core.gem.EmptyGem;
import com.sigmundgranaas.forgero.core.material.material.EmptySecondaryMaterial;
import com.sigmundgranaas.forgero.core.resource.ForgeroResourceInitializer;
import com.sigmundgranaas.forgero.core.toolpart.head.HeadState;
import com.sigmundgranaas.forgero.core.toolpart.head.PickaxeHead;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sigmundgranaas.forgero.core.property.ToolPropertyTest.PICKAXEHEAD_SCHEMATIC;

public class PickaxeHeadTest {
    @BeforeEach
    void initialiseResources() {
        ForgeroRegistry.INSTANCE.loadResourcesIfEmpty(new ForgeroResourceInitializer());
    }

    public static PickaxeHead createDefaultPickaxeHead() {
        HeadState state = new HeadState(ForgeroRegistry.MATERIAL.getPrimaryMaterial(Constants.IRON_IDENTIFIER_STRING).get(), new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), PICKAXEHEAD_SCHEMATIC.get());
        return new PickaxeHead(state);
    }


    @Test
    void getToolTypeName() {
        Assertions.assertEquals("pickaxe", createDefaultPickaxeHead().getToolTypeName());
    }

    @Test
    void getToolPartName() {
        Assertions.assertEquals("pickaxehead", createDefaultPickaxeHead().getToolPartName());
    }
}