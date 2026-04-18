package com.maximumg9.g9utils;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
}
