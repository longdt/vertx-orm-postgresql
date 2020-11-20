package com.github.longdt.vertxorm.repository.query;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InTest {
    @Test
    void testConstructor() {
        var in = new In<>("some_field", Collections.singletonList("abc"));
    }
}