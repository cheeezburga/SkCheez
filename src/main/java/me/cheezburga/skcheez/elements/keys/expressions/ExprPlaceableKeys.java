package me.cheezburga.skcheez.elements.keys.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.destroystokyo.paper.Namespaced;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Name("Destroyable Keys - Item")
@Description("Get or modify the destroyable keys of an item.")
@Examples({"",""})
@Since("1.0.0")
public class ExprPlaceableKeys extends PropertyExpression<ItemType, NamespacedKey> {

    static {
        register(ExprPlaceableKeys.class, NamespacedKey.class, "place(able| on) keys", "itemtypes");
    }

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        setExpr((Expression<ItemType>) exprs[0]);
        return true;
    }

    @SuppressWarnings("NullableProblems")
    protected NamespacedKey @NotNull [] get(Event event, ItemType [] source) {
        List<NamespacedKey> keys = new ArrayList<>();
        for (ItemType item : source) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDestroyableKeys()) {
                Set<Namespaced> onItem = meta.getDestroyableKeys();
                for (Namespaced ns : onItem) {
                    keys.add(NamespacedKey.fromString(ns.getNamespace() + ":" + ns.getKey()));
                }
            }
        }
        return keys.toArray(new NamespacedKey[0]);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.ADD || mode == ChangeMode.REMOVE) {
            return CollectionUtils.array(NamespacedKey[].class, ItemType[].class);
        } else if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) {
            return CollectionUtils.array();
        }
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantValue"})
    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        ItemType[] source = getExpr().getArray(event);
        Set<Namespaced> keys = new HashSet<>();

        if (delta != null) {
            for (Object o : delta) {
                if (o instanceof ItemType item) {
                    keys.add(item.getMaterial().getKey());
                } else if (o instanceof NamespacedKey key) {
                    keys.add(key);
                }
            }
        }

        if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) {
            for (ItemType item : source) {
                ItemMeta meta = item.getItemMeta();
                Collection<Namespaced> empty = new ArrayList<>();
                meta.setDestroyableKeys(empty);
                item.setItemMeta(meta);
            }
        } else if (mode == ChangeMode.SET) {
            for (ItemType item : source) {
                ItemMeta meta = item.getItemMeta();
                meta.setDestroyableKeys(keys);
                item.setItemMeta(meta);
            }
        } else if (mode == ChangeMode.ADD) {
            for (ItemType item : source) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDestroyableKeys()) {
                    keys.addAll(meta.getDestroyableKeys());
                }
                meta.setDestroyableKeys(keys);
                item.setItemMeta(meta);
            }
        } else if (mode == ChangeMode.REMOVE) {
            for (ItemType item : source) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDestroyableKeys()) {
                    Set<Namespaced> alreadyOn = meta.getDestroyableKeys();
                    alreadyOn.removeAll(keys);
                    meta.setDestroyableKeys(alreadyOn);
                }
                item.setItemMeta(meta);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends NamespacedKey> getReturnType() {
        return NamespacedKey.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean b) {
        return "placeable keys of " + getExpr().toString(e, b);
    }
}
