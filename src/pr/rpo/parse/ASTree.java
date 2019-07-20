package pr.rpo.parse;

import java.util.List;

public class ASTree {
    private List<ASTree> childs;


    public ASTree(List<ASTree> c) {
        childs = c;
    }

    public ASTree() {
    }

    final public int outDegree() {
        return childs.size();
    }

    final public ASTree child(int i) {
        return childs.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String seq = "";
        for(ASTree a : childs) {
            sb.append(seq);
            seq = " ";
            sb.append(a.toString());
        }
        sb.append(")");

        return sb.toString();
    }
}
