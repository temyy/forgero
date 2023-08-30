package com.sigmundgranaas.forgero.fabric.patchouli;

import static com.sigmundgranaas.forgero.fabric.resources.ARRPGenerator.RESOURCE_PACK;

import net.minecraft.util.Identifier;

public class BookDropOnAdvancement {
	private static final String Advancement = "{\"type\":\"advancement_reward\",\"pools\":[{\"rolls\":1,\"entries\":[{\"type\":\"item\",\"name\":\"patchouli:guide_book\",\"functions\":[{\"function\":\"set_nbt\",\"tag\":\"{\\\"patchouli:book\\\":\\\"patchouli:forgero_guide\\\"}\"}]}]}]}";

	public static void registerBookDrop() {
		var id = new Identifier("forgero:loot_tables/grant_book_on_advancement.json");

		RESOURCE_PACK.addData(id, Advancement.getBytes());
	}
}
