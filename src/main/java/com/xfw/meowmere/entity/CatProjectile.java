package com.xfw.meowmere.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CatProjectile extends Projectile {
    protected float damage;

    public CatProjectile(EntityType<? extends CatProjectile> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public CatProjectile(EntityType<? extends CatProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public CatProjectile(Level levelIn, LivingEntity shooter) {
        this(XEntityRegistry.CATPROJECTILE.get(), levelIn, shooter);
        this.setNoGravity(false);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation.scale(0.5F));
    }

    protected double getDefaultGravity() {
        return 0.05;
    }

    public void tick() {
        this.setNoGravity(false);
        super.tick();
    }

}
