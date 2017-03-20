package com.endercrest.colorcube.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Thomas Cordua-von Specht on 12/23/2016.
 *
 * This is a utility that is here to help send packets to players as required.
 */
public class OBCUtil {

    public static Class<?> getOBCClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }
}
