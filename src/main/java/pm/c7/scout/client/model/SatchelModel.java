package pm.c7.scout.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

public class SatchelModel<T extends LivingEntity> extends EntityModel<T> {
	private final ModelPart root;
	private final ModelPart satchel;
	private final ModelPart strap;

	public SatchelModel(ModelPart root) {
		this.root = root;
		this.satchel = root.getChild("satchel");
		this.strap = this.satchel.getChild("strap");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();

		PartDefinition satchel = root.addOrReplaceChild("satchel", CubeListBuilder.create().texOffs(10, 0).addBox(-6.0F, -12.0F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.275F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition strap = satchel.addOrReplaceChild("strap", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -13.0F, -2.0F, 1.0F, 14.0F, 4.0F, new CubeDeformation(0.275F)), PartPose.offsetAndRotation(-3.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		return LayerDefinition.create(modelData, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertices, int light, int overlay, int color) {
		satchel.render(poseStack, vertices, light, overlay, color);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}
