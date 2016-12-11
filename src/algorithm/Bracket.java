package algorithm;

/**
 * 合法括号序列处理算法
 * 文法：
 * S -> (A | null
 * A -> )B | (A)B
 * B -> S | null
 * <p/>
 * Created by Billin on 2016/12/6.
 */
public class Bracket {

    private static String bracketString =
            "((())(()(())(())))(())()()()((()())()(()((()()))()))()#";

    private static int index = 0;

    private static char get() {
        return bracketString.charAt(index);
    }

    private static char move() {
        return bracketString.charAt(index++);
    }

    private static void error(int index) {
        throw new RuntimeException("error at " + index + ": " + bracketString.charAt(index));
    }

    public static void main(String[] args) {
        // start handle
        S();
        if (get() == '#')
            System.out.println("success");
        else {
            error(index);
        }
    }

    private static void S() {
        char c = get();
        if (c == '(') {
            move();
            A();
        } else if (c == '#') {
            // nothing to do
        } else {
            error(index);
        }
    }

    private static void A() {
        char c = get();
        if (c == ')') {
            move();
            B();
        } else if (c == '(' || c == '[') {
            move();
            A();
            if (get() != ')')
                error(index);
            else
                move();
            B();
        } else {
            error(index);
        }
    }

    private static void B() {
        char c = get();
        if (c == '(' || c == '[') {
            S();
        } else if (c == '#' || c == ')') {
            // nothing to do
        } else {
            error(index);
        }
    }
}
