package pr.rpo.parse;

import pr.rpo.tree.btree.BTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SqlParser {
    String operator = null;
    String key = null;
    String[] params = null;

    public boolean parser(String sql) {
        Tokener tokener = new Tokener(sql);

        String op = tokener.next();

        switch(op) {
            case "insert":parseInsert(tokener);operator = op;break;
            case "select":parseSelect(tokener);operator = op;break;
            case "delete":parseDelete(tokener);operator = op;break;
            default:return false;
        }

        return true;
    }

    private void parseInsert(Tokener tokener) {
        params = tokener.rest();
        key = tokener.next();
    }

    private void parseSelect(Tokener tokener) {
        key = tokener.next();
    }

    private void parseDelete(Tokener tokener) {
        key = tokener.next();
    }


    public void print() {
        System.out.println("operator:" + operator);
        System.out.println("key:" + key);
        System.out.println("params:");
        if(params == null) return;
        Arrays.asList(params).forEach(e -> System.out.print(e + " "));

    }

    public Map<String, String> execute(BTree bTree) {
        Map<String, String> m = new HashMap(1);
        switch(operator) {
            case "insert":bTree.put(key, params);m.put("result", "success");break;
            case "select":m.put("result", Arrays.toString((String[])bTree.get(key)));break;
            case "delete":bTree.delete(key);m.put("result", "success");break;
            default:;
        }
        return m;
    }

    public static void main(String[] args) {
        final String INSERT_SQL = "insert (peter,22,\"二十二\")";
        final String SELECT_SQL = "select peter";
        final String DELETE_SQL = "delete peter";

        SqlParser sqlParser = new SqlParser();
        sqlParser.parser(INSERT_SQL);
        sqlParser.print();

    }
}
