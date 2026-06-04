package com.maximumg9.g9utils;

import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class Util {
    @SuppressWarnings("unchecked")
    public static <O> Class<O> getClassStrict(O object) {
        return (Class<O>) object.getClass();
    }
    public static Vec3d interpVec(float delta, Vec3d start, Vec3d end) {
        return new Vec3d(
            MathHelper.lerp(delta,start.x,end.x),
            MathHelper.lerp(delta,start.y,end.y),
            MathHelper.lerp(delta,start.z,end.z)
        );
    }

    public static String getSafeString(Text txt) {
        StringBuilder stringBuilder = new StringBuilder();
        visitSafe(
            txt,
            string -> {
                stringBuilder.append(string);
                return Optional.empty();
            }
        );
        return stringBuilder.toString();

    }


    public static <T> Optional<T> visitSafe(Text txt, StringVisitable.Visitor<T> visitor) {
        Optional<T> optional = visitSafe(txt.getContent(),visitor);
        if (optional.isPresent()) {
            return optional;
        } else {
            for (Text text : txt.getSiblings()) {
                Optional<T> optional2 = visitSafe(text,visitor);
                if (optional2.isPresent()) {
                    return optional2;
                }
            }

            return Optional.empty();
        }
    }

    public static <T> Optional<T> visitSafe(TextContent content, StringVisitable.Visitor<T> visitor) {
        return switch (content) {
            case TranslatableTextContent trans -> {
                if(G9utils.VANILLA_LANGUAGE_KEYS.contains(trans.getKey())) {
                    yield trans.visit(visitor);
                } else {
                    yield visitor.accept(trans.getKey());
                }
            }
            case KeybindTextContent bind -> {
                if(G9utils.VANILLA_LANGUAGE_KEYS.contains(bind.getKey())) {
                    yield bind.visit(visitor);
                } else {
                    yield visitor.accept(bind.getKey());
                }
            }
            default -> content.visit(visitor);
        };
    }
}
