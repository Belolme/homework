package compile;

/**
 * 词法分析
 *
 * @author Sean
 * @version 1.0
 * @date 创建时间：2016/11/16 10:31
 * @parameter
 * @return
 */
public class LexicalAnalysis extends TypeUtil {
    private StringBuffer buffer = new StringBuffer();//缓冲区
    private int i = 0;
    private char ch;
    private String strToken; //字符数组，存放构成单词符号的字符串

    public LexicalAnalysis(String fileSrc) {
        FileUtil.readFile(buffer, fileSrc);
    }

    public void analyse() {
        strToken = "";
        FileUtil.clearFile();
        while (i < buffer.length()) {
            getChar();  //读取一个字符
            getBC();
            if (isLetter(ch)) {
                while (isLetter(ch) || isDigit(ch)) {
                    concat();
                    getChar();
                }
                retract();//回调
                if (isKeyWord(strToken)) {
                    writeFile(strToken, strToken);//stroken为关键字
                } else {
                    writeFile("id", strToken);//stroken为标识符
                }
                strToken = "";
            } else if (isDigit(ch)) {
                while (isDigit(ch)) { //ch为数字
                    concat();
                    getChar();
                }
                if (!isLetter(ch)) { //不能数字+字母
                    retract();
                    writeFile("digit", strToken); //是整形
                } else {
                    writeFile("error", strToken);
                }
                strToken = "";
            } else if (isOperator(ch)) {//运算符
                if (ch == '/') {
                    getChar();
                    if (ch == '*') {//多行注释
                        while (true) {
                            getChar();
                            if (ch == '*') {//多行注释结束
                                getChar();
                                if (ch == '/') {
                                    getChar();
                                    break;
                                }
                            }
                        }

                    }
                    if (ch == '/') {//单行注释
                        while (ch != 9) {
                            getChar();
                        }
                    }
                    retract();
                }
                switch (ch) {
                    case '+':
                        writeFile("plus", ch + "");
                        break;
                    case '-':
                        writeFile("min", ch + "");
                        break;
                    case '*':
                        writeFile("mul", ch + "");
                        break;
                    case '/':
                        writeFile("div", ch + "");
                        break;
                    case '>':
                        writeFile("gt", ch + "");
                        break;
                    case '<':
                        writeFile("lt", ch + "");
                        break;
                    case '=':
                        writeFile("eq", ch + "");
                        break;
                    case '&':
                        writeFile("and", ch + "");
                        break;
                    case '|':
                        writeFile("or", ch + "");
                        break;
                    case '~':
                        writeFile("not", ch + "");
                        break;
                    default:
                        break;
                }
            } else if (isSeparators(ch)) {
                writeFile("separators", ch + "");
            } else writeFile("error", ch + "");
        }
    }

    /**
     * 将下一个输入字符读到ch中，搜索指示器前移一个字符
     */
    public void getChar() {
        ch = buffer.charAt(i);
        i++;
    }

    /**
     * 判断ch中的字符是否为空白，若是则插入非空白字符
     */
    public void getBC() {
        while (Character.isWhitespace(ch)) {
            getChar();
        }
    }

    /**
     * 将ch连接到strToken之后
     */
    public void concat() {
        strToken += ch;
    }

    /**
     * 回调一个字符位置，将ch设置为空白
     */
    public void retract() {
        i--;
        ch = ' ';
    }

    /**
     * 按照二元式规则写入文件
     *
     * @param file 字符类型
     * @param s    当前字符
     */
    public void writeFile(String file, String s) {
        int temp = getType(file.toUpperCase());
//        System.out.println("(" + file + "," + s + ")");
        file = "(" + temp + "," + s + ")" + "\n";
        FileUtil.writeFile(file);
    }
}
