package pr.rpo.server;

import pr.rpo.parse.SqlParser;
import pr.rpo.tree.btree.BTree;

import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        BTree bTree = new BTree();

        System.out.println("please input your command");
        while(true) {
            System.out.print("$:");
            Scanner s = new Scanner(System.in);

            String sql = s.nextLine();
            System.out.println("you type :" + sql);
            SqlParser sqlParser = new SqlParser();
            System.out.println("parse result:" + sqlParser.parser(sql));
            sqlParser.print();
            System.out.println("result:" + sqlParser.execute(bTree));
            System.out.println();
        }
    }
}
