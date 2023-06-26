package com.sigmundgranaas.forgero.fabric.client.renderer.blockentity;

import com.sigmundgranaas.forgero.fabric.client.blockentity.AssemblyStationBlockEntity;
import com.sigmundgranaas.forgero.minecraft.common.block.assemblystation.AssemblyStationBlock;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AssemblyStationBlockEntityRenderer implements BlockEntityRenderer<AssemblyStationBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

	@Override
	public void render(AssemblyStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		//		if (!EasyAnvils.CONFIG.get(ClientConfig.class).renderAnvilContents) return;
		Direction direction = ((LootableContainerBlockEntity) this).getCachedState().get(AssemblyStationBlock.FACING);
		int posData = (int) ((LootableContainerBlockEntity) this).getPos().asLong();
		this.renderFlatItem(
				0, ((LootableContainerBlockEntity) this).getStack(0), direction, matrices, light, overlay, posData);
		this.renderFlatItem(
				1, ((LootableContainerBlockEntity) this).getStack(1), direction, matrices, light, overlay, posData);
	}

	private void renderFlatItem(int index, ItemStack stack, Direction direction, MatrixStack poseStack, int packedLight, int packedOverlay, VertexConsumerProvider vertexConsumers, int posData) {
		if (stack.isEmpty()) return;

		poseStack.push();
		poseStack.translate(0.0, 1.0375, 0.0);
		poseStack.multiply(Direction.Axis.XN.rotationDegrees(90.0F));
		boolean mirrored = (direction.getAxis().getStep() == 1 ? 1 : 0) != index % 2;

		switch (direction.getAxis()) {
			case X -> {
				if (mirrored) {
					poseStack.translate(0.25, -0.5, 0.0);
				} else {
					poseStack.multiply(Direction.Axis.ZP.rotationDegrees(180.0F));
					poseStack.translate(-0.75, 0.5, 0.0);
				}
			}
			case Z -> {
				if (mirrored) {
					poseStack.multiply(Axis.ZN.rotationDegrees(90.0F));
					poseStack.translate(0.25, 0.5, 0.0);
				} else {
					poseStack.multiply(Axis.ZP.rotationDegrees(90.0F));
					poseStack.translate(-0.75, -0.5, 0.0);
				}
			}
		}
		poseStack.scale(0.375F, 0.375F, 0.375F);

		this.itemRenderer.renderItem(stack, ModelTransformation.Mode.FIXED, packedLight, packedOverlay, poseStack,
				vertexConsumers, posData + index
		);

		poseStack.pop();
	}
}
