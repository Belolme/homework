package algorithm;

import java.util.Scanner;

/**
 * 在字母表∑={a,b,c}上定义乘法运算：
 * aa=b,     ab=b,     ac=a
 * ba=c,     bb=b,     bc=a
 * ca=a,     cb=c,     cc=c
 * 求∑上的任意乘法表达式  X1n=x1x2…xn，存在多少种可能的计算
 * 顺序，使X1n的计算结果为a。
 * <p/>
 * Created by Billin on 2016/12/7.
 */
public class ASequence {

    private static class Note {
        int a;

        int b;

        int c;

        @Override
        public String toString() {
            return "Note{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String alphaSequence = scanner.next();

        // init data construction
        Note[][] notes = new Note[alphaSequence.length()][alphaSequence.length()];

        for (int i = 0; i < alphaSequence.length(); i++) {
            for (int j = 0; j < alphaSequence.length(); j++) {
                notes[i][j] = new Note();
            }
        }

        for (int i = 0; i < alphaSequence.length(); i++) {
            Note note = notes[i][i];
            switch (alphaSequence.charAt(i)) {
                case 'a':
                    note.a = 1;
                    break;

                case 'b':
                    note.b = 1;
                    break;

                case 'c':
                    note.c = 1;
                    break;
            }
        }

        // algorithm
        for (int record = 0; record < alphaSequence.length(); record++) {
            int step = record + 1;
            for (int i = 0; i < alphaSequence.length(); i++) {

                int j = step + i;
                if (j >= alphaSequence.length())
                    break;

                for (int point = i; point < j; point++) {
                    Note note = notes[i][j];
                    Note left = notes[i][point];
                    Note right = notes[point + 1][j];

                    note.a += left.a * right.c;
                    note.a += left.b * right.c;
                    note.a += left.c * right.a;

                    note.b += left.a * right.b;
                    note.b += left.b * right.b;
                    note.b += left.a * right.a;

                    note.c += left.b * right.a;
                    note.c += left.c * right.b;
                    note.c += left.c * right.c;
                }

            }
        }

        for (int i = 0; i < alphaSequence.length(); i++) {
            for (int j = 0; j < alphaSequence.length(); j++) {
                System.out.print(notes[i][j].a);
            }
            System.out.println();
        }

        System.out.println("a 的数量为:" + notes[0][alphaSequence.length() - 1].a);
    }
}
