package com.sigmundgranaas.forgero.item.tool;

import com.sigmundgranaas.forgero.item.StateItem;
import com.sigmundgranaas.forgero.item.items.DynamicAttributeItem;
import com.sigmundgranaas.forgero.item.tooltip.StateWriter;
import com.sigmundgranaas.forgero.property.PropertyContainer;
import com.sigmundgranaas.forgero.state.State;
import com.sigmundgranaas.forgero.type.Type;
import com.sigmundgranaas.forgero.util.match.Context;
import com.sigmundgranaas.forgero.util.match.Matchable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DynamicShovelItem extends ShovelItem implements DynamicAttributeItem, State, StateItem {
    private final State DEFAULT;

    public DynamicShovelItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, State defaultState) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.DEFAULT = defaultState;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        StateWriter.of(state(stack)).write(tooltip, context);

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Text getName(ItemStack stack) {
        return getName();
    }

    @Override
    public String name() {
        return DEFAULT.name();
    }

    @Override
    public String nameSpace() {
        return DEFAULT.nameSpace();
    }

    @Override
    public Type type() {
        return DEFAULT.type();
    }

    @Override
    public boolean test(Matchable match, Context context) {
        return DEFAULT.test(match, context);
    }

    @Override
    public PropertyContainer dynamicProperties(ItemStack stack) {
        return dynamicState(stack);
    }

    @Override
    public PropertyContainer defaultProperties() {
        return DEFAULT;
    }

    @Override
    public State defaultState() {
        return DEFAULT;
    }
}
