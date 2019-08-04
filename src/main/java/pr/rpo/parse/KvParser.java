package pr.rpo.parse;

import pr.rpo.server.TranRecord;
import pr.rpo.tree.btree.BTree;

import java.text.ParseException;
import java.util.*;

public class KvParser {
    ASTree asTree = null;

    public boolean parser(String sql) throws ParseException {
        Tokener tokener = new Tokener(sql);
        ASTLeaf operator = operator(tokener);
        ASTLeaf key = key(tokener);
        if(operator.token().getWord().equals("set")) {
            ASTLeaf value = value(tokener);
            asTree = new ASTree(Arrays.asList(operator,key,value));
        }else {
            asTree = new ASTree(Arrays.asList(operator,key));
        }

        return true;
    }

    private ASTLeaf operator(Tokener tokener) throws ParseException {
        List<String> OP = new ArrayList<String>();
        OP.add("set");OP.add("get");OP.add("delete");
        Token t = tokener.peekToken();
        if(!OP.contains(t.getWord()))
            throw new ParseException("",t.getIndex());
        return new ASTLeaf(tokener.nextToken());
    }

    private ASTLeaf key(Tokener tokener) throws ParseException {
        if(!tokener.hasToken()) throw new ParseException("",1);
        return new ASTLeaf(tokener.nextToken());
    }

    private ASTLeaf value(Tokener tokener) throws ParseException {
        if(!tokener.hasToken()) throw new ParseException("",1);
        return new ASTLeaf(tokener.nextToken());
    }

    public void print() {
        System.out.println(asTree.toString());
    }

    public ASTree asTree() {
        return asTree;
    }

    public Map<String, String> execute(BTree bTree) {
        Map<String, String> m = new HashMap(1);

        String key = ((ASTLeaf)asTree.child(1)).token().getWord();

        switch(((ASTLeaf)asTree.child(0)).token().getWord())  {
            case "set":bTree.put(key, ((ASTLeaf)asTree.child(2)).token().getWord());m.put("result", "success");break;
            case "get":m.put("result", (String)bTree.get(key));break;
            case "delete":bTree.delete(key);m.put("result", "success");break;
            default:;
        }
        return m;
    }

    public Map<String, String> txExecute(BTree bTree) {
        TranRecord.start();
        Map<String, String> m = new HashMap(1);

        String key = ((ASTLeaf)asTree.child(1)).token().getWord();

        switch(((ASTLeaf)asTree.child(0)).token().getWord())  {
            case "set":
                TranRecord.put(bTree, key, ((ASTLeaf)asTree.child(2)).token().getWord());
                m.put("result", "success");break;
            case "get":
                m.put("result", (String)bTree.get(key));break;
            case "delete":
                TranRecord.put(bTree, key, bTree.get(key));
                m.put("result", "success");break;
            default:;
        }

        return m;
    }

    public static void main(String[] args) throws ParseException {
        final String INSERT_SQL = "set peter 22";
        final String SELECT_SQL = "get peter";
        final String DELETE_SQL = "delete peter";

        KvParser sqlParser = new KvParser();
        sqlParser.parser(INSERT_SQL);
        sqlParser.print();

    }
}
