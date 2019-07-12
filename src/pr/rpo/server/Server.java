package pr.rpo.server;

import pr.rpo.parse.SqlParser;
import pr.rpo.tree.btree.BTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        File file = new File("./a.db");
        BTree bTree;
        if(!file.exists()) {
            System.out.println("创建文件");
            try {
                file.createNewFile();
                bTree = new BTree();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }else {
            System.out.println("读取文件");
            Block block = new Block();
            block.from(file);
            bTree = block.getIndex();
        }

        System.out.println("please input your command");
        while(true) {
            System.out.print("$:");
            Scanner s = new Scanner(System.in);

            String sql = s.nextLine();
            System.out.println("you type :" + sql);
            if(sql.equals("save")) {
                Block block = new Block();
                try {
                    block.from(bTree);
                    block.writeToFile(new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(sql.equals("show")){
                System.out.println(bTree.toString());
            }else {
                SqlParser sqlParser = new SqlParser();
                System.out.println("parse result:" + sqlParser.parser(sql));
                sqlParser.print();
                System.out.println("result:" + sqlParser.execute(bTree));
                System.out.println();
            }
        }
    }
}
