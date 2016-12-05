package compile;

import java.io.*;

/**
 * 文件操作
 *
 * @author Sean
 * @version 1.0
 * @date 创建时间：2016/11/15 23:11
 */
public class FileUtil {

    /**
     * 文件读取到缓冲区
     *
     * @param buffer 缓冲区
     * @param file   文件源
     * @return true:success
     * false:filed;
     */
    public static boolean readFile(StringBuffer buffer, String file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String temp = null;
            while ((temp = br.readLine()) != null) {

                //追加到缓冲区
                buffer.append(temp);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 追加方式写文件
     *
     * @param args 需要写入的字符串
     * @return true : success
     * false : filed
     */
    public static boolean writeFile(String args) {
        File file = new File("./src/compile/output.txt");
        try {
            if (!file.exists()) {

                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(args);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 清楚文件内容
     *
     * @return true:success
     * false:fail
     */
    public static boolean clearFile() {
        try {
            File file = new File("./src/compile/output.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

}
