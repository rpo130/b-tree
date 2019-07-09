package pr.rpo.parse;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokener {

    private String sql;
    private String[] ts;
    private int pos;

    public Tokener(String sql) {
        this.sql = sql;
        List<String> list = Arrays.asList(sql.split("\\s|\\(|\\)|,"));
        list = list.stream().filter(e -> !e.isEmpty()).collect(Collectors.toList());
        this.ts = new String[list.size()];
        this.ts = list.toArray(this.ts);
        this.pos = 0;
    }

    public String next() {
        if(pos < ts.length) {
            if(ts[pos].isEmpty()) {
                pos++;
                return next();
            }else {
                return ts[pos++];
            }
        }else {
            return null;
        }
    }

    public String[] rest() {
        String[] rs = new String[ts.length - pos];
        System.arraycopy(ts,pos,rs,0,ts.length-pos);
        return rs;
    }

    public void print() {
        String n = next();
        while(n != null) {
            System.out.println(n);
            n = next();
        }

    }

    public static void main(String[] args) {

        final String INSERT_SQL = "insert (peter,22,\"二十二\")";
        final String SELECT_SQL = "select peter";
        final String DELETE_SQL = "delete peter";

        Tokener tokener = new Tokener(INSERT_SQL);
        tokener.print();
        tokener = new Tokener(SELECT_SQL);
        tokener.print();
        tokener = new Tokener(DELETE_SQL);
        tokener.print();
    }
}
