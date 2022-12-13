package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RotationTest {

    @Test
    void getVectorFromRotation() {
        Vector2D vec1 = Rotation.getVectorFromRotation(0);
        Vector2D vec2 = Rotation.getVectorFromRotation(7);
        Vector2D vec3 = Rotation.getVectorFromRotation(4);
        Vector2D vec4 = Rotation.getVectorFromRotation(2);
        assertEquals(vec1, new Vector2D(0, 1));
        assertEquals(vec2, new Vector2D(-1, 1));
        assertEquals(vec3, new Vector2D(0, -1));
        assertEquals(vec4, new Vector2D(1, 0));
    }
}