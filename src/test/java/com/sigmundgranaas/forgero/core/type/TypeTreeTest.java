package com.sigmundgranaas.forgero.core.type;

import com.sigmundgranaas.forgero.core.data.v2.data.TypeData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeTreeTest {

    public static List<TypeData> createDataList() {
        var schematic = new TypeData("SCHEMATIC", Optional.empty());
        var pickaxeSchematic = new TypeData("PICKAXE_SCHEMATIC", Optional.of(schematic.name()));
        var shovelSchematic = new TypeData("SHOVEL_SCHEMATIC", Optional.of(schematic.name()));
        var swordSchematic = new TypeData("SWORD_SCHEMATIC", Optional.of(schematic.name()));
        var saberSchematic = new TypeData("SABER_SCHEMATIC", Optional.of(swordSchematic.name()));
        var material = new TypeData("MATERIAL", Optional.empty());
        var wood = new TypeData("WOOD", Optional.of(material.name()));
        var oak = new TypeData("OAK", Optional.of("WOOD"));
        return List.of(wood, pickaxeSchematic, shovelSchematic, swordSchematic, saberSchematic, material, schematic, oak);
    }

    @Test
    void createValidTypeTree() {
        var tree = new TypeTree();
        createDataList().forEach(tree::addNode);
        tree.resolve();
        var testNodeSchematic = tree.find("SABER_SCHEMATIC");
        var testNodeOak = tree.find("OAK");
        assertTrue(testNodeSchematic.isPresent());
        assertTrue(testNodeSchematic.get().getParent().isPresent());

        assertTrue(testNodeOak.isPresent());
        assertTrue(testNodeOak.get().getParent().isPresent());
    }
}