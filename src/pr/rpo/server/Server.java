package pr.rpo.server;

import pr.rpo.parse.SqlParser;
import pr.rpo.tree.btree.BTree;

import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
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
            String command = s.nextLine();

            if(command.equals("save")) {
                Block block = new Block();
                try {
                    block.from(bTree);
                    block.writeToFile(new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(command.equals("show")){
                System.out.println(bTree.toString());
            }else if(command.equals("quit")) {
                System.out.print("good byte");
                return;
            }else {
                try {
                    SqlParser sqlParser = new SqlParser();
                    System.out.println("parse result:" + sqlParser.parser(command));
                    sqlParser.print();
                    System.out.println("result:" + sqlParser.execute(bTree));
                    System.out.println();
                }catch (ParseException e) {
                    System.out.println("命令不合法");
                }
            }
        }
    }
}
