package pm.c7.scout.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;

public class PouchRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final ItemInHandRenderer heldItemRenderer;

	public PouchRenderLayer(RenderLayerParent<T, M> context, ItemInHandRenderer heldItemRenderer) {
		super(context);
		this.heldItemRenderer = heldItemRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		var leftPouch = ScoutUtil.findBagItem((Player) entity, BaseBagItem.BagType.POUCH, false);
		var rightPouch = ScoutUtil.findBagItem((Player) entity, BaseBagItem.BagType.POUCH, true);

		if (!leftPouch.isEmpty()) {
			poseStack.pushPose();
			((PlayerModel<?>) this.getParentModel()).leftLeg.translateAndRotate(poseStack);
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
			poseStack.scale(0.325F, 0.325F, 0.325F);
			poseStack.translate(0F, -0.325F, -0.475F);
			this.heldItemRenderer.renderItem(entity, leftPouch, ItemDisplayContext.FIXED, false, poseStack, multiBufferSource, light);
			poseStack.popPose();
		}
		if (!rightPouch.isEmpty()) {
			poseStack.pushPose();
			((PlayerModel<?>) this.getParentModel()).rightLeg.translateAndRotate(poseStack);
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
			poseStack.scale(0.325F, 0.325F, 0.325F);
			poseStack.translate(0F, -0.325F, 0.475F);
			this.heldItemRenderer.renderItem(entity, rightPouch, ItemDisplayContext.FIXED, false, poseStack, multiBufferSource, light);
			poseStack.popPose();
		}
	}
}
