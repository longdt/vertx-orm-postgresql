package com.github.longdt.vertxorm.repository.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RawQueryTest {
    @Test
    void buildSQL() {
        var query = new RawQuery<Object>("abc = ? and age > ?");
        StringBuilder builder = new StringBuilder();
        query.buildSQL(builder, 100);
        System.out.println(builder);
    }
}