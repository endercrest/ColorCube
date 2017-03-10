package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Created by Thomas Cordua-von Specht on 3/7/2017.
 */
public interface ParticleHandler {

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Player player, Particle particle, Location location, int count);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data);


    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     *
     *
     */
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data);


    public enum Particle {
        EXPLOSION_NORMAL,
        EXPLOSION_LARGE,
        EXPLOSION_HUGE,
        FIREWORKS_SPARK,
        WATER_BUBBLE,
        WATER_SPLASH,
        WATER_WAKE,
        SUSPENDED,
        SUSPENDED_DEPTH,
        CRIT,
        CRIT_MAGIC,
        SMOKE_NORMAL,
        SMOKE_LARGE,
        SPELL,
        SPELL_INSTANT,
        SPELL_MOB,
        SPELL_MOB_AMBIENT,
        SPELL_WITCH,
        DRIP_WATER,
        DRIP_LAVA,
        VILLAGER_ANGRY,
        VILLAGER_HAPPY,
        TOWN_AURA,
        NOTE,
        PORTAL,
        ENCHANTMENT_TABLE,
        FLAME,
        LAVA,
        FOOTSTEP,
        CLOUD,
        REDSTONE,
        SNOWBALL,
        SNOW_SHOVEL,
        SLIME,
        HEART,
        BARRIER,
        ITEM_CRACK(ItemStack.class),
        BLOCK_CRACK(MaterialData.class),
        BLOCK_DUST(MaterialData.class),
        WATER_DROP,
        ITEM_TAKE,
        MOB_APPEARANCE,
        DRAGON_BREATH,
        END_ROD,
        DAMAGE_INDICATOR,
        SWEEP_ATTACK;

        private final Class<?> dataType;

        Particle() {
            dataType = Void.class;
        }

        Particle(Class<?> data) {
            dataType = data;
        }

        /**
         * Returns the required data type for the particle
         * @return the required data type
         */
        public Class<?> getDataType() {
            return dataType;
        }
    }

    class NullParticleHandler implements ParticleHandler{

        @Override
        public void spawnParticle(Player player, Particle particle, Location location, int count) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }
    }

}
