package compile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * grammar analysis
 * Created by Billin on 2016/11/29.
 */
public class GrammarAnalysis {

    private static List<Unit> input;

    private static int pointer = 0;

    private static Unit move() {
        return input.get(pointer++);
    }

    private static Unit get() {
        return input.get(pointer);
    }

    private static void matchThrow(Unit target, int src) {
        if (!match(target, src)) {
            throwPrint(target);
        }
    }

    private static void matchThrow(Unit target, String src) {
        if (!match(target, src)) {
            throwPrint(target);
        }
    }

    private static boolean match(Unit target, int src) {
        return src == target.getId();
    }

    private static boolean match(Unit target, int src, int... srcs) {
        if (match(target, src)) {
            return true;
        }

        for (int s : srcs) {
            if (match(target, s)) {
                return true;
            }
        }

        return false;
    }

    private static boolean match(Unit target, String src) {
        return src.equals(target.getValue());
    }

    private static boolean match(Unit target, String src, String... srcs) {
        if (match(target, src))
            return true;

        for (String srcT : srcs) {
            if (srcT.equals(target.getValue())) {
                return true;
            }
        }

        return false;
    }

    private static void throwPrint(Unit unit) {
        int index = input.indexOf(unit);
        index++;
        throw new RuntimeException("error at " + index + " " + unit);
    }

    public static void main(String[] args) {

        MainTest.main(null);

        StringBuffer stringBuffer = new StringBuffer();
        FileUtil.readFile(stringBuffer, "./src/compile/output.txt");

        Pattern pattern = Pattern.compile("\\((\\d+|-\\d+),([^)]+|\\))\\)");
        Matcher matcher = pattern.matcher(stringBuffer);
        input = new LinkedList<>();
        while (matcher.find()) {
            input.add(new Unit(Integer.valueOf(matcher.group(1)), matcher.group(2)));
        }
        input.add(new Unit(Integer.MAX_VALUE, "###"));
        input = new ArrayList<>(input);

        matchThrow(move(), KeyTypes.INT);
        matchThrow(move(), "main");
        matchThrow(move(), "(");
        matchThrow(move(), ")");
        matchThrow(move(), "{");
        code();
        matchThrow(move(), "}");
        matchThrow(move(), "###");

        System.out.println("compile success");
    }

    private static void code() {
        Unit first = get();
        if (match(first, "if")) {
            ifCondition();
            code();
        } else if (match(first, KeyTypes.INT)) {
            intCondition();
            code();
        } else if (match(first, "}", "###")) {

        } else {
            throwPrint(first);
        }
    }

    private static void intCondition() {
        matchThrow(move(), KeyTypes.INT);
        intConditionA();
        matchThrow(move(), ";");
    }

    private static void intConditionA() {
        matchThrow(move(), KeyTypes.ID);
        intConditionB();
    }

    private static void intConditionB() {
        Unit first = get();
        if (match(first, "=")) {
            move();
            expressions();
            intConditionC();
        } else if (match(first, ";")) {
            // nothing to do
        } else {
            throwPrint(first);
        }
    }

    private static void intConditionC() {
        Unit first = get();
        if (match(first, ",")) {
            move();
            intConditionA();
        } else if (match(first, ";")) {
            // nothing to do
        } else {
            throwPrint(first);
        }
    }

    private static void ifCondition() {
        matchThrow(move(), "if");
        matchThrow(move(), "(");
        expressions();
        matchThrow(move(), ")");
        matchThrow(move(), "{");
        code();
        matchThrow(move(), "}");
    }

    private static void expressions() {
        expressionsTemp();
        expressionsA();
    }

    private static void expressionsA() {
        Unit first = get();
        if (match(first, "+")) {
            move();
            expressionsTemp();
            expressionsA();
        } else if (match(first, "-")) {
            move();
            expressionsTemp();
            expressionsA();
        } else if (match(first, ")", ",", ";")) {
            // nothing to do
        } else {
            throwPrint(first);
        }
    }

    private static void expressionsTemp() {
        expressionFinal();
        expressionTempA();
    }

    private static void expressionTempA() {
        Unit first = get();
        if (match(first, "*")) {
            move();
            expressionFinal();
            expressionTempA();
        } else if (match(first, "/")) {
            move();
            expressionFinal();
            expressionTempA();
        } else if (match(first, ")", "+", "-", ",", ";")) {
            // nothing to do
        } else {
            throwPrint(first);
        }
    }

    private static void expressionFinal() {
        Unit first = get();
        if (match(first, "(")) {
            move();
            expressions();
            matchThrow(move(), ")");
        } else if (match(first, KeyTypes.ID, KeyTypes.DIGIT)) {
            move();
        } else {
            throwPrint(first);
        }
    }

}

class Unit {
    private int id;

    private String value;

    public Unit(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}