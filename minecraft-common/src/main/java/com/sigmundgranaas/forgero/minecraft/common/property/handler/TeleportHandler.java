package com.sigmundgranaas.forgero.minecraft.common.property.handler;

import com.sigmundgranaas.forgero.minecraft.common.entity.EnderTeleportationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class TeleportHandler implements Runnable {
	private final int level;
	private final World world;
	private final PlayerEntity user;

	public TeleportHandler(int level, World world, PlayerEntity user) {
		this.level = level;
		this.world = world;
		this.user = user;
	}

	public static void execute(int level, World world, PlayerEntity user) {
		new TeleportHandler(level, world, user).run();
	}

	@Override
	public void run() {
		Item item = user.getStackInHand(user.getActiveHand()).getItem();
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

		user.getItemCooldownManager().set(item, 20);
		if (!world.isClient) {
			EnderTeleportationEntity teleportationEntity = new EnderTeleportationEntity(world, user, 500 * level);
			teleportationEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 5f, 0f);
			world.spawnEntity(teleportationEntity);
		}
	}
}
