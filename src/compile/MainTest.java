package compile;

/**
 * @author Sean
 * @version 1.0
 * @date 创建时间：2016/11/16 15:49
 * @parameter
 * @return
 */
public class MainTest {
    public static void main(String[] args) {
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis("./src/compile/input.txt");
        lexicalAnalysis.analyse();
    }

}
