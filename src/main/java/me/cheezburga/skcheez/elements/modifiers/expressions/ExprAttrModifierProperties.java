package me.cheezburga.skcheez.elements.modifiers.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("NullableProblems")
@Name("Attribute Modifier - Properties")
@Description({"Represents the properties of an attribute modifier, which can be changed.",
        "See the equipment slot and attribute modifier operation types."})
@Examples({"set modifier attribute of {_modifier} to attack speed",
        "set modifier name of {_modifier} to \"A Name\"",
        "add 1 to modifier amount of {_modifier}",
        "set modifier operation of {_modifier} to add number",
        "set modifier slot of {_modifier} to hand slot",
        "set modifier uuid of {_modifier} to random uuid"})
@Since("1.0.0")
public class ExprAttrModifierProperties extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprAttrModifierProperties.class, Object.class, ExpressionType.COMBINED,
                "[attribute[ ]] modifier attribute of %attributemodifiers%",
                "[attribute[ ]]modifier name of %attributemodifiers%",
                "[attribute[ ]]modifier (amount|increment) of %attributemodifiers%",
                "[attribute[ ]]modifier operat(or|ion) of %attributemodifiers%",
                "[attribute[ ]]modifier slot of %attributemodifiers%",
                "[attribute[ ]]modifier uuid of %attributemodifiers%"
                );
    }

    private static final int ATTRIBUTE = 0;
    private static final int NAME = 1;
    private static final int AMOUNT = 2;
    private static final int OPERATION = 3;
    private static final int SLOT = 4;
    private static final int UUID = 5;
    private int pattern;
    private Expression<AttributeModifierWrapper> wrapper;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pattern = matchedPattern;
        wrapper = (Expression<AttributeModifierWrapper>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable Object[] get(Event event) {
        AttributeModifierWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return null;

        switch (pattern) {
            case ATTRIBUTE -> { return new Attribute[]{wrapper.getAttribute()}; }
            case NAME -> { return new String[]{wrapper.getModifier().getName()}; }
            case AMOUNT -> { return new Number[]{wrapper.getModifier().getAmount()}; }
            case OPERATION -> { return new AttributeModifier.Operation[]{wrapper.getModifier().getOperation()}; }
            case SLOT -> { return new EquipmentSlot[]{wrapper.getModifier().getSlot()}; }
            case UUID -> { return new String[]{wrapper.getModifier().getUniqueId().toString()}; }
        }
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
        if (pattern == ATTRIBUTE && mode == ChangeMode.SET) {
            return CollectionUtils.array(Attribute.class);
        } else if (pattern == NAME && mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        } else if (pattern == AMOUNT && (mode == ChangeMode.SET || mode == ChangeMode.ADD || mode == ChangeMode.REMOVE)) {
            return CollectionUtils.array(Number.class);
        } else if (pattern == OPERATION && (mode == ChangeMode.SET || mode == ChangeMode.RESET)) {
            return CollectionUtils.array(AttributeModifier.Operation.class);
        } else if (pattern == SLOT && (mode == ChangeMode.SET || mode == ChangeMode.RESET)) {
            return CollectionUtils.array(EquipmentSlot.class);
        } else if (pattern == UUID && mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class, UUID.class);
        }
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        Object object = delta != null ? delta[0] : null;
        AttributeModifierWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return;

        if (pattern == ATTRIBUTE && mode == ChangeMode.SET) {
            if (object instanceof Attribute attribute) {
                wrapper.setAttribute(attribute);
            }
        } else if (pattern == NAME && mode == ChangeMode.SET) {
            if (object instanceof String name) {
                wrapper.modifyName(name);
            }
        } else if (pattern == AMOUNT) {
            if (object instanceof Number amount) {
                if (mode == ChangeMode.SET) {
                    wrapper.modifyAmount(amount.doubleValue());
                } else if (mode == ChangeMode.ADD || mode == ChangeMode.REMOVE) {
                    double initial = wrapper.getModifier().getAmount();
                    wrapper.modifyAmount(initial + amount.doubleValue());
                }
            }
        } else if (pattern == OPERATION && mode == ChangeMode.SET) {
            if (object instanceof AttributeModifier.Operation operation) {
                wrapper.modifyOperation(operation);
            }
        } else if (pattern == SLOT && mode == ChangeMode.SET) {
            if (object instanceof EquipmentSlot slot) {
                wrapper.modifySlot(slot);
            }
        } else if (pattern == UUID && mode == ChangeMode.SET) {
            UUID uuid = java.util.UUID.randomUUID();
            if (object instanceof String s) {
                uuid = java.util.UUID.fromString(s);
            } else if (object instanceof UUID u) {
                uuid = u;
            }
            wrapper.modifyUUID(uuid);
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return switch (pattern) {
            case ATTRIBUTE -> Attribute.class;
            case NAME, UUID -> String.class;
            case AMOUNT -> Number.class;
            case OPERATION -> AttributeModifier.Operation.class;
            case SLOT -> EquipmentSlot.class;
            default -> throw new IllegalStateException("Unexpected pattern: " + pattern);
        };
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        String prop = switch (pattern) {
            case ATTRIBUTE -> "attribute";
            case NAME -> "name";
            case AMOUNT -> "amount";
            case OPERATION -> "operation";
            case SLOT -> "slot";
            case UUID -> "uuid";
            default -> "null";
        };
        return prop + " of attribute modifier " + this.wrapper.toString(event, b);
    }
}
