package me.cheezburga.skcheez.elements.keys.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CondHasDestroyableKeys extends PropertyCondition<ItemType> {

    static {
        if (Skript.methodExists(ItemMeta.class, "hasDestroyableKeys")) {
            register(CondHasDestroyableKeys.class, PropertyType.HAVE, "destroyable key[s]", "itemtypes");
        }
    }

    @Override
    public boolean check(ItemType item) {
        return item.getItemMeta().hasDestroyableKeys();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "destroyable keys";
    }
}
