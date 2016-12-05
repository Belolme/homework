package compile;

import java.lang.reflect.Field;

/**
 * 对读取的字符进行分类操作
 *
 * @author Sean
 * @version 1.0
 * @date 创建时间：2016/11/16 9:41
 * @parameter
 * @return
 */
public class TypeUtil {

    //关键字符表
    private final String keyWords[] = {"abstract", "boolean", "break", "byte",
            "case", "catch", "char", "class", "continue", "default", "do",
            "double", "else", "extends", "final", "finally", "float", "for",
            "if", "implements", "import", "instatceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected",
            "synchronized", "this", "throw", "throws", "transient", "try",
            "void", "volatile", "while", "strictfp", "enum", "const", "assert"};

    //运算符表
    private final char operator[] = {'+', '-', '*', '/', '=', '>', '<', '&', '|', '!'};

    //界符
    private final char separators[] = {',', ';', '{', '}', '(', ')', '[', ']', '_', ':', '.', '"', '\\','\''};

    /**
     * 判断是否为字符
     *
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    /**
     * 判断是否为数字
     *
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    /**
     * 判断是否为关键字
     *
     * @param s 需判断的字符串
     * @return boolean
     */
    public boolean isKeyWord(String s) {
        for (int i = 0; i < keyWords.length; i++) {
            if (keyWords[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为运算符
     *
     * @param ch 需判断字符
     * @return boolean
     */
    public boolean isOperator(char ch) {
        for (int i = 0; i < operator.length; i++) {
            if (operator[i] == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符是否为分隔符
     *
     * @param ch 待检测的字符
     * @return boolean
     */
    public boolean isSeparators(char ch) {
        for (int i = 0; i < separators.length; i++) {
            if (ch == separators[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取种别编码
     *
     * @param args 属性别名
     * @return 种别编码
     */
    public int getType(String args) {
        int type = -1;
        Field[] fields = KeyTypes.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(args)) {
                try {
                    type = (Integer) field.get(new KeyTypes());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return type;
    }


}
