package pr.rpo.parse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenerTest {
    final String INSERT_SQL = "insert (peter,22,\"二十二\")";

    Tokener tokener;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testInsert() {
        final String INSERT_SQL = "insert (peter,22,二十二)";
        tokener = new Tokener(INSERT_SQL);
        assertEquals(tokener.next(), "insert");
        assertArrayEquals(tokener.rest(), new String[]{"peter","22","二十二"});
        assertEquals(tokener.next(), "peter");
        assertEquals(tokener.next(), "22");
        assertEquals(tokener.next(), "二十二");
    }

    @Test
    void testSelect() {
        final String SELECT_SQL = "select peter";
        tokener = new Tokener(SELECT_SQL);
        assertEquals(tokener.next(), "select");
        assertEquals(tokener.next(), "peter");
    }

    @Test
    void testDelete() {
        final String DELETE_SQL = "delete peter";
        tokener = new Tokener(DELETE_SQL);
        assertEquals(tokener.next(), "delete");
        assertEquals(tokener.next(), "peter");
    }

}