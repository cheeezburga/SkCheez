package me.cheezburga.skcheez.elements.modifiers.expressions;

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
import com.google.common.collect.Multimap;
import me.cheezburga.skcheez.api.modifiers.ModifierUtils;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Name("Attribute Modifiers - Item")
@Description("Gets the attribute modifiers of a given item.")
@Examples({"set {_modifiers::*} to attribute modifiers on player's tool",
        "set {_attackSpeed::*} to modifiers of player's tool where [modifier attribute of input is attack speed]",
        "add {_modifier} to modifiers of {_item}",
        "add {_modifiers::*} to modifiers of {_item}",
        "remove {_modifier} from modifiers of {_item}",
        "reset modifiers of {_item}"})
@Since("1.0.0")

public class ExprAttrModifiersItem extends PropertyExpression<ItemType, AttributeModifierWrapper> {

    static {
        register(ExprAttrModifiersItem.class, AttributeModifierWrapper.class, "[attribute] modifiers", "itemtypes");
    }

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        setExpr((Expression<ItemType>) exprs[0]);
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected AttributeModifierWrapper @NotNull [] get(Event event, ItemType [] source) {
        List<AttributeModifierWrapper> wrappers = new ArrayList<>();
        for (ItemType item : source) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasAttributeModifiers()) { // should I check here if meta != null too?
                Multimap<Attribute, AttributeModifier> attributes = meta.getAttributeModifiers();
                if (attributes != null) {
                    for (Attribute attribute : attributes.keySet()) {
                        Collection<AttributeModifier> modifiers = attributes.get(attribute);
                        for (AttributeModifier modifier : modifiers) {
                            AttributeModifierWrapper wrapper = new AttributeModifierWrapper(attribute, modifier);
                            wrappers.add(wrapper);
                        }
                    }
                }
            }
        }
        return wrappers.toArray(new AttributeModifierWrapper[0]);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.ADD || mode == ChangeMode.REMOVE) {
            return CollectionUtils.array(AttributeModifierWrapper[].class);
        } else if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) {
            return CollectionUtils.array();
        }
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantValue"})
    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) {
            for (ItemType item : getExpr().getArray(event)) {
                ItemMeta reset = item.getItemMeta();
                reset.setAttributeModifiers(null);
                item.setItemMeta(reset);
            }
        } else if (mode == ChangeMode.SET || mode == ChangeMode.ADD || mode == ChangeMode.REMOVE) {
            if (delta != null && delta instanceof AttributeModifierWrapper[] wrappers) {
                for (ItemType item : getExpr().getArray(event)) {
                    ItemMeta meta = item.getItemMeta();
                    if (mode == ChangeMode.SET) {
                        Multimap<Attribute, AttributeModifier> map = ModifierUtils.convertWrappers(wrappers);
                        meta.setAttributeModifiers(map);
                    } else if (mode == ChangeMode.ADD || mode == ChangeMode.REMOVE) {
                        for (AttributeModifierWrapper wrapper : wrappers) {
                            Attribute attribute = wrapper.getAttribute();
                            AttributeModifier modifier = wrapper.getModifier();
                            if (mode == ChangeMode.ADD) {
                                AttributeModifier conflict = ModifierUtils.getConflictingModifier(meta, attribute, modifier);
                                if (conflict != null) {
                                    meta.removeAttributeModifier(attribute, conflict);
                                }
                                meta.addAttributeModifier(attribute, modifier);
                            } else if (mode == ChangeMode.REMOVE) {
                                meta.removeAttributeModifier(attribute, modifier);
                            }
                        }
                    }
                    item.setItemMeta(meta);
                }
            }
        }

    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends AttributeModifierWrapper> getReturnType() {
        return AttributeModifierWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "attribute modifiers of " + getExpr().toString(event, b);
    }
}
