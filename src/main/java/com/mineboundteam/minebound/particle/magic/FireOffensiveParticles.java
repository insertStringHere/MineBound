package com.mineboundteam.minebound.particle.magic;

import com.mineboundteam.minebound.magic.OffensiveSpells.FireOffensiveSpell;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireOffensiveParticles extends TextureSheetParticle {
    protected FireOffensiveParticles(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed, int lifetime) {
        super(pLevel, pX, pY, pZ
//                , pXSpeed, pYSpeed, pZSpeed
        );

        this.setSpriteFromAge(spriteSet);
        this.friction = 1F;
        this.quadSize *= 0.25F;
        this.lifetime = lifetime;
        this.gravity = 1f;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        this.xd = pXSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        this.yd = pYSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        this.zd = pZSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        double d1 = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / d1 * (double) 0.4F;
        this.yd = this.yd / d1 * (double) 0.4F;
        this.zd = this.zd / d1 * (double) 0.4F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || this.onGround) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            // TODO: check for collision damage using the particle bounding box
        }
//        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        //        private final int lifetime;
        FireOffensiveSpell.FireOffensiveSpellConfig config;

        public Provider(SpriteSet spriteSet, FireOffensiveSpell.FireOffensiveSpellConfig config) {
            this.sprites = spriteSet;
            // Convert block distance to lifetime in ticks
//            this.lifetime = ;
            this.config = config;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FireOffensiveParticles(level, x, y, z, this.sprites, dx, dy, dz, (int) (config.FIRE_DISTANCE.get() * 2.5));
        }
    }
}