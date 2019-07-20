package pr.rpo.parse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pr.rpo.parse.Tokener;

import static org.junit.jupiter.api.Assertions.*;

class TokenerTest {
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTokenInsert() {
        final String SETSQL = "set a 1";
        Tokener tokener = new Tokener(SETSQL);
        assertTrue(tokener.hasToken());
        assertEquals(tokener.nextToken().getWord(),"set");
        assertEquals(tokener.nextToken().getWord(),"a");
        assertEquals(tokener.nextToken().getWord(), "1");
        assertFalse(tokener.hasToken());
    }



}