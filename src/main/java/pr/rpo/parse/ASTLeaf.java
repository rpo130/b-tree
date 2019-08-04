package pr.rpo.parse;

public class ASTLeaf extends ASTree {
    private Token token;

    public ASTLeaf(Token t) {
        token = t;
    }

    public Token token() {
        return token;
    }

    @Override
    public String toString() {
        return token.getWord();
    }
}
