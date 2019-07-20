package pr.rpo.parse;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokener {

    //2-opera,3-key,4-value
    private final String regLang = "\\s*((set|get|delete)|(\\w+)|(\\w*))?";
    private final Pattern pattern = Pattern.compile(regLang);
    private String commandLine;
    private List<Token> tokens = new ArrayList<>();
    private int pos;

    public Tokener(String commandLine) {
        this.commandLine = commandLine;
        Matcher matcher = pattern.matcher(this.commandLine);
        while(matcher.find()) {
            Token token = null;
            if(matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                token = new Token();
                token.setWord(matcher.group(2));
                token.setType("operator");
            }else if(matcher.group(3) != null && !matcher.group(3).isEmpty()) {
                token = new Token();
                token.setWord(matcher.group(3));
                token.setType("key");
            }else if(matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                token = new Token();
                token.setWord(matcher.group(4));
                token.setWord("value");
            }else {
                //
            }
            if(token == null) continue;
            tokens.add(token);
        }
        this.pos = 0;
    }

    public boolean hasToken() {
        if(tokens.isEmpty() || pos >= tokens.size()) {
            return false;
        }else {
            return true;
        }
    }

    public Token nextToken() {
        if(hasToken()) {
            return tokens.get(pos++);
        }else {
            return null;
        }
    }

    public Token peekToken() {
        if(hasToken()) {
            return tokens.get(pos);
        }else {
            return null;
        }
    }

    public static void main(String[] args) {
        String SET = "set a 1";
        Tokener t = new Tokener(SET);
        System.out.println(t.nextToken().getWord());
    }
}
