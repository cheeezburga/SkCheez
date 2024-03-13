package me.cheezburga.skcheez.elements.modifiers.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CondHasAttributeModifiers extends PropertyCondition<ItemType> {

    static {
        if (Skript.methodExists(ItemMeta.class, "hasAttributeModifiers")) {
            register(CondHasAttributeModifiers.class, PropertyType.HAVE, "[a|an] [attribute] modifier[s]", "itemtypes");
        }
    }

    @Override
    public boolean check(ItemType item) {
        return item.getItemMeta().hasAttributeModifiers();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "attribute modifiers";
    }
}
