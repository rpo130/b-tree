package pr.rpo.parse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlParserTest {
    final String INSERT_SQL = "set peter 22";
    final String SELECT_SQL = "get peter";
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
    void testInsert() throws ParseException {
        sqlParser.parser(INSERT_SQL);
        assertEquals(((ASTLeaf)sqlParser.asTree().child(0)).token().getWord(), "set");
        assertEquals(((ASTLeaf)sqlParser.asTree().child(1)).token().getWord(), "peter");
        assertEquals(((ASTLeaf)sqlParser.asTree().child(2)).token().getWord(),  "22");
    }
    @Test
    void testSelect() throws ParseException {
        sqlParser.parser(SELECT_SQL);
        assertEquals(((ASTLeaf)sqlParser.asTree().child(0)).token().getWord(), "get");
        assertEquals(((ASTLeaf)sqlParser.asTree().child(1)).token().getWord(), "peter");
    }
    @Test
    void testDelete() throws ParseException {
        sqlParser.parser(DELETE_SQL);
        assertEquals(((ASTLeaf)sqlParser.asTree().child(0)).token().getWord(), "delete");
        assertEquals(((ASTLeaf)sqlParser.asTree().child(1)).token().getWord(), "peter");

    }
}