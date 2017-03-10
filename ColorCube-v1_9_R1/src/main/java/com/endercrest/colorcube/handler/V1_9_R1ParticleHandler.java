package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 3/10/2017.
 *
 * This is just a wrapper class for the system that already exists in the 1.9 and above spigot version.
 */
public class V1_9_R1ParticleHandler implements ParticleHandler {

    private final String warning = "&cAttempting to use a particle that is not supported on this server version";


    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count, offsetX, offsetY, offsetZ);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count, offsetX, offsetY, offsetZ, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count, offsetX, offsetY, offsetZ, extra);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, location, count, offsetX, offsetY, offsetZ, extra, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }

    @Override
    public <T> void spawnParticle(Player player, Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        try {
            org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle.name());
            player.spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
        }catch (IllegalArgumentException e){
            MessageManager.getInstance().log(warning);
        }
    }
}
