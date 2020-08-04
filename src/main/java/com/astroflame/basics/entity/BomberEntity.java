package com.astroflame.basics.entity;

import com.astroflame.basics.util.RegistryHandler;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class BomberEntity extends FlyingEntity {

    public BomberEntity(EntityType<? extends FlyingEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new BomberEntity.MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new scanGoal(this));
        this.goalSelector.addGoal(4, new staySafeGoal(this));
        this.goalSelector.addGoal(2, new BomberEntity.spreadPoisonGoal(this));
        this.goalSelector.addGoal(1, new roamAroundGoal(this));
    }

    // Identifies targets and assigns them for use by other goals
    class scanGoal extends Goal {

        private final EntityPredicate distance = new EntityPredicate().setDistance(100D);
        private final BomberEntity body;

        public scanGoal(BomberEntity body) {
            this.setMutexFlags(EnumSet.of(Flag.LOOK));
            this.body = body;
        }

        @Override
        public boolean shouldExecute() {
            PlayerEntity target = world.getClosestPlayer(new EntityPredicate().setDistance(768.0D), this.body);
            body.setAttackTarget(target);
            return target != null;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
    }

    // While having a target, hover within a safe distance
    class staySafeGoal extends Goal {

        private final BomberEntity body;

        public staySafeGoal(BomberEntity body) {
            this.body = body;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return body.getAttackTarget() != null;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            LivingEntity target = body.getAttackTarget();
            if (target.getDistanceSq(this.body) < 768.0D && this.body.canEntityBeSeen(target)) {
                Vec3d safeBlock = getSafePos(body.getAttackTarget());
                body.moveController.setMoveTo(safeBlock.x, safeBlock.y, safeBlock.z, 1.0D);
            }
        }

        private Vec3d getSafePos(LivingEntity target) {
            double x;
            double y;
            double z;
            if (target.getPosX() > body.getPosX()) {
                x = body.getPosX() - 10D;
            } else {
                x = body.getPosX() + 10D;
            }

            if (target.getPosY() > body.getPosY()) {
                y = body.getPosY() - 2D;
            } else if (target.getPosY() < body.getPosY() && target.getPosY() + 10 < body.getPosY()) {
                y = body.getPosY() + 2D;
            } else {
                y = body.getPosY() + 1D;
            }

            if (target.getPosZ() > body.getPosZ()) {
                z = body.getPosZ() - 10D;
            } else {
                z = body.getPosZ() + 10D;
            }
            return new Vec3d(x, y, z);
        }
    }

    // Main attacking goal, spreads poison on the chosen target
    class spreadPoisonGoal extends Goal {

        private BomberEntity body;
        private int poisonCD = 20;

        public spreadPoisonGoal(BomberEntity body) {
            this.body = body;
        }

        @Override
        public boolean shouldExecute() {
            if (poisonCD > 0) {
                poisonCD--;
            }
            return (poisonCD <= 0 && BomberEntity.this.getAttackTarget() != null);
        }

        @Override
        public void startExecuting() {
            LivingEntity target = BomberEntity.this.getAttackTarget();
            if (target.getDistanceSq(this.body) < 768.0D && this.body.canEntityBeSeen(target)) {
                PoisonBombEntity poisonBomb = new PoisonBombEntity(RegistryHandler.POISON_BOMB_ENTITY.get(), 1.0D, 1.0D, 1.0D, body.world);
                poisonBomb.setPosition(body.getPosX(), body.getPosY(), body.getPosZ());
                poisonBomb.shoot(target.getPosX() - body.getPosX(), target.getPosYEye() - body.getPosY(), target.getPosZ() - body.getPosZ(), 0.95F, 0.5F);
                world.addEntity(poisonBomb);

                poisonCD = 40;
            }
        }
    }

    // While having no target, roam around freely
    class roamAroundGoal extends Goal {

        private final BomberEntity body;

        public roamAroundGoal(BomberEntity body) {
            this.body = body;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return (BomberEntity.this.getAttackTarget() == null && !body.getMoveHelper().isUpdating());
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random random = this.body.getRNG();
            double d0 = this.body.getPosX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 10.0F);
            double d1 = this.body.getPosY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 10.0F);
            double d2 = this.body.getPosZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 10.0F);
            this.body.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    static class MoveHelperController extends MovementController {
        private final BomberEntity body;
        private int courseChangeCooldown;

        public MoveHelperController(BomberEntity body) {
            super(body);
            this.body = body;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.body.getRNG().nextInt(6) + 2;
                    Vec3d vec3d = new Vec3d(this.posX - this.body.getPosX(), this.posY - this.body.getPosY(), this.posZ - this.body.getPosZ());
                    vec3d = vec3d.normalize();
                    this.body.setMotion(this.body.getMotion().add(vec3d.scale(0.1D)));
                    this.action = MovementController.Action.WAIT;
                }
            }
        }
    }
}
