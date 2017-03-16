package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.handler.particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class V1_8_R3ParticleHandler implements ParticleHandler {

    private ParticleEffect transform(Particle particle){
        try {
            return ParticleEffect.valueOf(particle.name());
        }catch(IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count) {
        spawnParticle(player, particle, location, count, 0, 0, 0);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count);
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");

    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        ParticleEffect newParticle = transform(particle);
        if(newParticle != null){
            newParticle.display((float)offsetX, (float)offsetY, (float)offsetZ, 1F, count, location, player);
        }else{
            MessageManager.getInstance().debugConsole("[V1_8_R3] Particle Unsupported");
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        ParticleEffect newParticle = transform(particle);
        if(newParticle != null){
            newParticle.display((float)offsetX, (float)offsetY, (float)offsetZ, (float)extra, count, location, player);
        }else{
            MessageManager.getInstance().debugConsole("[V1_8_R3] Particle Unsupported");
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        MessageManager.getInstance().debugConsole("[V1_8_R3] This method is not support for this version of ParticleHandler");
    }
}
