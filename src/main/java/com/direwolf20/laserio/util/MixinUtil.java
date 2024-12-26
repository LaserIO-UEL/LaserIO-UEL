package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.setup.Registration;
import com.mojang.blaze3d.platform.NativeImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//Taken with permission from Create-Powerlines
public class MixinUtil {
    public static int byteScale(int x, int y) {
        return x * y / 255;
    }

    public static int tintColor(int base, int tint) {
        int r = byteScale(base & 0xFF, tint >> 16);
        int g = byteScale((base >> 8) & 0xFF, (tint >> 8) & 0xFF);
        int b = byteScale((base >> 16) & 0xFF, tint & 0xFF);
        return r | (g << 8) | (b << 16) | (base & 0xFF000000);
    }

    public static int byteLerp(int from, int to, int factor) {
        return (from * (255 - factor) + to * factor) / 255;
    }

    public static int blendColor(int dst, int src) {
        int factor = (src >> 24) & 0xFF;
        int r = byteLerp(dst & 0xFF, src & 0xFF, factor);
        int g = byteLerp((dst >> 8) & 0xFF, (src >> 8) & 0xFF, factor);
        int b = byteLerp((dst >> 16) & 0xFF, (src >> 16) & 0xFF, factor);
        int a = byteLerp((dst >> 24) & 0xFF, factor, factor);
        return r | (g << 8) | (b << 16) | (a << 24);
    }

    public static long getPixels(NativeImage image) {
        String imageString = image.toString();
        int startIndex = imageString.indexOf("@") + 1;
        String pixelsAsString = imageString.substring(startIndex, imageString.length() - 2);
        return Long.parseLong(pixelsAsString);
    }

    public static NativeImage loadNativeImage(String name) {
        name = "/assets/" + LaserIO.MODID + "/textures/" + name + ".png";
        try (InputStream is = LaserIO.class.getResourceAsStream(name)) {
            return NativeImage.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fillLangTable(Map<String, String> table) {
        String prefix = "item." + LaserIO.MODID + ".";
        for (int i = 0; i < Registration.Energy_Overclocker_Cards.size(); i++) {
            String name = (i < Config.NAME_TIERS.get().size()) ? Config.NAME_TIERS.get().get(i) : ("Energy Overclocker Tier " + (i + 1));
            table.put(prefix + Registration.Energy_Overclocker_Cards.get(i).getId().getPath(), name);
        }
    }
}