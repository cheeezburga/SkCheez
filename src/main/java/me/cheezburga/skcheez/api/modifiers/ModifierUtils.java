package me.cheezburga.skcheez.api.modifiers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class ModifierUtils {
    // add method to get nbt/nbt string from wrapper
    public static Multimap<Attribute, AttributeModifier> convertWrappers(AttributeModifierWrapper[] wrappers) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        for (AttributeModifierWrapper wrapper : wrappers) {
            map.put(wrapper.getAttribute(), wrapper.getModifier());
        }
        return map;
    }

    public static boolean canAddModifier(ItemMeta meta, AttributeModifier modifier) {
        // after some testing, this doesn't work as intended
        // a modifier with an identical uuid CAN be applied to an item if it's for a different attribute
        // but it CANNOT, without being replaced
        if (!meta.hasAttributeModifiers()) {
            return true;
        } else {
            Multimap<Attribute, AttributeModifier> map = meta.getAttributeModifiers();
            if (map != null) {
                for (AttributeModifier m : map.values()) {
                    if (m.getUniqueId() == modifier.getUniqueId()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static @Nullable AttributeModifierWrapper getConflictingModifier(ItemMeta meta, AttributeModifier modifier) {
        // implement and replace the canaddmodifer method above
    }
}
