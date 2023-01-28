package com.sigmundgranaas.forgero.minecraft.common.soul;

import com.sigmundgranaas.forgero.core.soul.Soul;
import com.sigmundgranaas.forgero.core.soul.SoulSource;
import com.sigmundgranaas.forgero.core.state.State;
import com.sigmundgranaas.forgero.core.state.composite.ConstructedTool;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.registry.Registry;

public class SoulHelper {
    public static State of(LivingEntity entity, ConstructedTool tool) {
        SoulSource soulSource = new SoulSource(Registry.ENTITY_TYPE.getId(entity.getType()).toString());
        Soul soul = new Soul(soulSource);
        return tool.bind(soul);
    }
}
