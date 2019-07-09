package pr.rpo.parse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlParserTest {
    final String INSERT_SQL = "insert (peter,22,\"二十二\")";
    final String SELECT_SQL = "select peter";
    final String DELETE_SQL = "delete peter";

    SqlParser sqlParser;

    @BeforeEach
    void setUp() {
        sqlParser = new SqlParser();
    }

    @Test
    void parser() {
    }

    @Test
    void testInsert() {
        sqlParser.parser(INSERT_SQL);
        assertEquals(sqlParser.operator, "insert");
        assertEquals(sqlParser.key, "peter");
        assertArrayEquals(sqlParser.params, new String[]{"peter", "22", "\"二十二\""});
    }
    @Test
    void testSelect() {
        sqlParser.parser(SELECT_SQL);
        assertEquals(sqlParser.operator, "select");
        assertEquals(sqlParser.key, "peter");
        assertArrayEquals(sqlParser.params, null);
    }
    @Test
    void testDelete() {
        sqlParser.parser(DELETE_SQL);
        assertEquals(sqlParser.operator, "delete");
        assertEquals(sqlParser.key, "peter");
        assertArrayEquals(sqlParser.params, null);

    }
}