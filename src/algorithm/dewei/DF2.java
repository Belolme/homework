package algorithm.dewei;

import java.util.Scanner;

public class DF2 {
    private String input;

    public DF2(String input) {
        this.input = input;
    }

    public int calculate() {
        int length = input.length();
        int m[][][] = new int[length][length][3];
        for (int i = 0; i < length; i++) {
            switch (input.charAt(i)) {
                case 'a':
                    m[i][i][0] = 1;
                    break;
                case 'b':
                    m[i][i][1] = 1;
                    break;
                case 'c':
                    m[i][i][2] = 1;
                    break;
            }
        }
        for (int step = 1; step <= length - 1; step++) {
            for (int start = 0; start < length - step; start++) {
                int end = start + step;
                for (int k = start; k < end; k++) {
                    m[start][end][0] += (m[start][k][0] * m[k + 1][end][2]);
                    m[start][end][0] += (m[start][k][1] * m[k + 1][end][2]);
                    m[start][end][0] += (m[start][k][2] * m[k + 1][end][0]);

                    m[start][end][1] += (m[start][k][0] * m[k + 1][end][0]);
                    m[start][end][1] += (m[start][k][0] * m[k + 1][end][1]);
                    m[start][end][1] += (m[start][k][1] * m[k + 1][end][1]);

                    m[start][end][2] += (m[start][k][1] * m[k + 1][end][0]);
                    m[start][end][2] += (m[start][k][2] * m[k + 1][end][1]);
                    m[start][end][2] += (m[start][k][2] * m[k + 1][end][2]);
                }
            }
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print("" + m[i][j][0] + ' ');
            }
            System.out.println();
        }
        return m[0][length - 1][0];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();
        DF2 df = new DF2(input);
        System.out.println(df.calculate());
    }
}
