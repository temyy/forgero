package com.sigmundgranaas.forgero.minecraft.common.predicate.entity;

import static com.sigmundgranaas.forgero.minecraft.common.predicate.CodecUtils.generalPredicate;

import java.util.function.Function;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.sigmundgranaas.forgero.minecraft.common.predicate.AdapterCodec;
import com.sigmundgranaas.forgero.minecraft.common.predicate.KeyPair;
import com.sigmundgranaas.forgero.minecraft.common.predicate.PredicateAdapter;
import com.sigmundgranaas.forgero.minecraft.common.predicate.XYZPredicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class EntityAdapter {
	static Function<Entity, BlockPos> pos = Entity::getBlockPos;
	static Function<Entity, EntityType<?>> type = Entity::getType;
	static Function<Entity, World> world = Entity::getWorld;
	static Function<Entity, Pair<BlockPos, WorldView>> posWorldPair = entity -> new Pair<>(entity.getBlockPos(), entity.getWorld());

	public static String ENTITY_POS_KEY = "pos";
	public static String ENTITY_TYPE_KEY = "entity_type";


	public static Codec<KeyPair<Predicate<Entity>>> entityPosCodec() {
		return AdapterCodec.of(ENTITY_POS_KEY, generalPredicate(XYZPredicate.CODEC, XYZPredicate.class), PredicateAdapter.create(pos));
	}

	public static Codec<KeyPair<Predicate<Entity>>> entityTypePredicate() {
		return AdapterCodec.of(ENTITY_TYPE_KEY, generalPredicate(EntityTypePredicate.CODEC, EntityTypePredicate.class), PredicateAdapter.create(type));
	}
}
