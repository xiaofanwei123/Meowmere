package com.xfw.meowmere.entity;

import com.xfw.meowmere.Meowmere;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public class CatProjectile extends Projectile {
    protected static final EntityDataAccessor<Integer> BOUNCESLEFT = SynchedEntityData.defineId(CatProjectile.class, EntityDataSerializers.INT);

    public int getbouncesLeft() {
        return this.entityData.get(BOUNCESLEFT);
    }

    public void setbouncesLeft(int i) {
        this.entityData.set(BOUNCESLEFT, i);
    }

    private float damage;

    public CatProjectile(EntityType<? extends CatProjectile> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public CatProjectile(EntityType<? extends CatProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public CatProjectile(Level levelIn, LivingEntity shooter) {
        this(Meowmere.CATPROJECTILE.get(), levelIn, shooter);
        this.setNoGravity(false);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void shoot(Vec3 rotation, float speed) {
        this.setDeltaMovement(rotation.scale(speed));
    }


    protected double getDefaultGravity() {
        return 0.025F;
    }

    public void tick() {
        super.tick();
        setPos(position().add(getDeltaMovement()));
        if (!this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - getDefaultGravity(), vec34.z);
        }

        if (this.level().isClientSide) {
            if (getbouncesLeft() >= 1) {
                this.level().addParticle(Meowmere.PLAYER_RAINBOW.get(), this.xo, this.yo, this.zo, this.getId(), 0, 0);
            }
            if (this.tickCount % 5 == 0) {
                int particleCount = 1;
                double spread = 1;
                double speed = 1;
                rainbowDustParticle(this.level(), this.xo, this.yo, this.zo, speed, spread, particleCount);
            }
        }

        this.handleHitDetection();
    }

    public void handleHitDetection() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !NeoForge.EVENT_BUS.post(new ProjectileImpactEvent(this, hitresult)).isCanceled()) {
            this.onHit(hitresult);
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        this.playSound(SoundEvents.CAT_AMBIENT, 3F, 1F);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        entityHitResult.getEntity().invulnerableTime = 0;
        if (this.getOwner() != null) {
            entityHitResult.getEntity().hurt(this.getOwner().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), damage);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        Vec3 hitPos = hitResult.getLocation();
        //计算当前碰撞次数（1表示首次碰撞）
        int bounceCount = 5 - getbouncesLeft();
        //粒子效果
        if (this.level().isClientSide) {
            int particleCount = 20 + 15 * bounceCount;
            double spread = 2 + bounceCount;
            double speed = 1;
            rainbowDustParticle(this.level(), hitPos.x, hitPos.y, hitPos.z, speed, spread, particleCount);
        }
        //范围伤害
        else {
            if (getbouncesLeft() >= 0) {
            float range = 2.5F + 0.5F * bounceCount;
            AABB boundingBox = new AABB(
                    hitPos.x - range,
                    hitPos.y - range,
                    hitPos.z - range,
                    hitPos.x + range,
                    hitPos.y + range,
                    hitPos.z + range
            );
            List<? extends Entity> entities = this.level().getEntities(this, boundingBox);
            for (Entity target : entities) {
                if (!(target instanceof Player) &&
                        target instanceof LivingEntity &&
                        this.getOwner() != null) {
                    target.hurt(this.getOwner().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), damage);
                }
            }

            //反弹逻辑
                Direction face = hitResult.getDirection();
                Vec3 normal = new Vec3(face.getStepX(), face.getStepY(), face.getStepZ());
                this.setDeltaMovement(calculateBounceVector(this.getDeltaMovement(), normal));
                double offset = 0.1;
                this.setPos(
                        hitPos.x + normal.x * offset,
                        hitPos.y + normal.y * offset,
                        hitPos.z + normal.z * offset
                );
            }
            setbouncesLeft(getbouncesLeft() - 1);
        }
        //第5次碰撞后销毁实体
        if (this.level().isClientSide && getbouncesLeft() <=0) {
            this.discard();
        }//延后1tick为保证客户端能渲染粒子，可选择发包。销毁实体正常是-1
        if(!this.level().isClientSide && getbouncesLeft() ==-2){
            this.discard();
        }
    }

    private void rainbowDustParticle(Level level, double x, double y, double z, double speed, double spread, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            float hue = i / (float) particleCount;
            float saturation = 0.2f;
            float brightness = 0.95f;
            int rgb = Color.HSBtoRGB(hue, saturation, brightness);
            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;
            float whiteFactor = 0.4f;
            r = r * (1 - whiteFactor) + whiteFactor;
            g = g * (1 - whiteFactor) + whiteFactor;
            b = b * (1 - whiteFactor) + whiteFactor;
            double offsetX = (Meowmere.RANDOM.nextDouble() - 0.5) * spread;
            double offsetY = (Meowmere.RANDOM.nextDouble() - 0.5) * spread;
            double offsetZ = (Meowmere.RANDOM.nextDouble() - 0.5) * spread;
            ParticleOptions particle = new DustParticleOptions(
                    new Vector3f(r, g, b),
                    1.5f
            );
            level.addParticle(
                    particle,
                    x + offsetX,
                    y + offsetY,
                    z + offsetZ,
                    speed,
                    speed,
                    speed
            );
        }
    }

    private Vec3 calculateBounceVector(Vec3 velocity, Vec3 normal) {
        double dotProduct = velocity.dot(normal);
        return velocity.subtract(
                normal.scale(2 * dotProduct)
        );
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BOUNCESLEFT, 4);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setbouncesLeft(compound.getInt("BouncesLeft"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("BouncesLeft", getbouncesLeft());
    }

}
