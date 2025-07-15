package com.direwolf20.laserio.util;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MiscTools {
    public static boolean inBounds(int x, int y, int w, int h, double ox, double oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    public static Vector3f findOffset(Direction direction, int slot, Vector3f[] offsets) {
        Vector3f offsetVector = new Vector3f(offsets[slot]);
        switch(direction) {
            case UP -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                offsetVector.add(0, 1, 0);
            }
            case DOWN -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                offsetVector.add(0, 0, 1);
            }
            case NORTH -> {}
            case EAST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                offsetVector.add(1, 0, 0);
            }
            case SOUTH -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-180);
                offsetVector = quaternionf.transform(offsetVector);
                offsetVector.add(1, 0, 1);
            }
            case WEST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                offsetVector.add(0, 0, 1);
            }
        }
        return offsetVector;
    }

    public static ListTag stringListToNBT(List<String> list) {
        ListTag nbtList = new ListTag();
        for (String string : list) {
            CompoundTag tag = new CompoundTag();
            tag.putString("list", string);
            nbtList.add(tag);
        }
        return nbtList;
    }

    public static List<String> NBTToStringList(ListTag nbtList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            CompoundTag tag = nbtList.getCompound(i);
            list.add(tag.getString("list"));
        }
        return list;
    }

    public static MutableComponent tooltipMaker(String translationKey, String prefix, int color) {
        MutableComponent component = Component.translatable(translationKey);
        if (!prefix.isBlank()) {
            component = Component.literal(prefix).append(component);
        }
        Style style = Style.EMPTY.withColor(color);
        component.setStyle(style);
        return component;
    }

    public static MutableComponent tooltipMaker(String translationKey, String prefix, ChatFormatting color) {
        return tooltipMaker(translationKey, prefix, color.getColor());
    }

    public static MutableComponent tooltipMaker(String translationKey, int color) {
        return tooltipMaker(translationKey, "", color);
    }

    public static MutableComponent tooltipMaker(String translationKey, ChatFormatting color) {
        return tooltipMaker(translationKey, "", color.getColor());
    }

    public static MutableComponent tooltipMakerLiteral(String literal, String prefix, int color) {
        MutableComponent component;
        if (prefix.isBlank()) {
            component = Component.literal(literal);
        } else {
            component = Component.literal(prefix + literal);
        }
        Style style = Style.EMPTY.withColor(color);
        component.setStyle(style);
        return component;
    }

    public static MutableComponent tooltipMakerLiteral(String literal, String prefix, ChatFormatting color) {
        return tooltipMakerLiteral(literal, prefix, color.getColor());
    }

    public static MutableComponent tooltipMakerLiteral(String literal, int color) {
        return tooltipMakerLiteral(literal, "", color);
    }

    public static MutableComponent tooltipMakerLiteral(String literal, ChatFormatting color) {
        return tooltipMakerLiteral(literal, "", color.getColor());
    }
}