package com.sigmundgranaas.forgero.minecraft.common.predicate;

import java.util.function.Predicate;

import com.mojang.serialization.Codec;

public class CodecUtils {
	public static <T, P extends Predicate<T>> Codec<Predicate<T>> generalPredicate(Codec<P> specificCodec, Class<P> clazz) {
		return specificCodec.xmap(it -> it, clazz::cast);
	}
}
