package pr.rpo.parse;

import java.util.ArrayList;
import java.util.List;

public class Token {
    private String word;
    private String type;
    private int index;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
