package com.sigmundgranaas.forgero.core.tool.toolpart.factory;

import com.sigmundgranaas.forgero.core.gem.Gem;
import com.sigmundgranaas.forgero.core.material.material.PrimaryMaterial;
import com.sigmundgranaas.forgero.core.material.material.SecondaryMaterial;
import com.sigmundgranaas.forgero.core.material.material.realistic.EmptySecondaryMaterial;
import com.sigmundgranaas.forgero.core.tool.toolpart.ForgeroToolPart;

public abstract class ToolPartBuilder {
    protected final PrimaryMaterial primary;
    protected SecondaryMaterial secondary;
    protected Gem gem;


    public ToolPartBuilder(PrimaryMaterial primary) {
        this.primary = primary;
        this.secondary = new EmptySecondaryMaterial();
        this.gem = null;
    }

    public ToolPartBuilder(ForgeroToolPart part) {
        this.primary = part.getPrimaryMaterial();
        this.secondary = part.getSecondaryMaterial();
        this.gem = null;
    }

    public PrimaryMaterial getPrimary() {
        return primary;
    }

    public SecondaryMaterial getSecondary() {
        return secondary;
    }

    public ToolPartBuilder setSecondary(SecondaryMaterial secondary) {
        this.secondary = secondary;
        return this;
    }

    public Gem getGem() {
        return gem;
    }

    public ToolPartBuilder setGem(Gem gem) {
        this.gem = gem;
        return this;
    }

    public abstract ForgeroToolPart createToolPart();
}
