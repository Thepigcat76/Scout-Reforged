package pm.c7.scout.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import pm.c7.scout.Scout;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.client.model.SatchelModel;
import pm.c7.scout.content.items.BaseBagItem;

public class SatchelRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation SATCHEL_TEXTURE = Scout.rl("textures/entity/satchel.png");
	private static final ResourceLocation UPGRADED_SATCHEL_TEXTURE = Scout.rl("textures/entity/upgraded_satchel.png");

	private final SatchelModel<T> satchel;

	public SatchelRenderLayer(RenderLayerParent<T, M> context) {
		super(context);
		// TODO: Use model layers for this
		LayerDefinition modelData = SatchelModel.getTexturedModelData();
		this.satchel = new SatchelModel<>(null);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack satchel = ScoutUtil.findBagItem((Player) entity, BaseBagItem.BagType.SATCHEL, false);

		if (!satchel.isEmpty()) {
			BaseBagItem satchelItem = (BaseBagItem) satchel.getItem();
			var texture = SATCHEL_TEXTURE;
			if (satchelItem.getSlotCount() == ScoutUtil.MAX_SATCHEL_SLOTS)
				texture = UPGRADED_SATCHEL_TEXTURE;

			poseStack.pushPose();
			((PlayerModel<?>) this.getParentModel()).body.translateAndRotate(poseStack);
			this.getParentModel().copyPropertiesTo(this.satchel);
			VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(
					bufferSource, RenderType.armorCutoutNoCull(texture), false, satchel.hasFoil()
			);
			this.satchel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}
}
