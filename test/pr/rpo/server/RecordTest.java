package pr.rpo.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    void from() {
    }

    @Test
    void toByte() {
        Record record = new Record("a", "b");
        Record fromRecord = Record.from(record.toByte())[0];
        assertArrayEquals(fromRecord.toByte(),record.toByte());
    }
}