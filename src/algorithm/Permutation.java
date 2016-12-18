package algorithm;

import java.util.Arrays;

/**
 * 非递归的全排列实现
 * <p/>
 * Created by Billin on 2016/12/5.
 */
public class Permutation {

    private static class Node {
        int number;

        // 1 - left ; 2 - right
        int orientation;

        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

    public static void main(String[] args) {

        Node[] array = new Node[4];
        for (int i = 0; i < array.length; i++) {
            Node node = new Node();
            node.number = i + 1;
            node.orientation = 1;
            array[i] = node;
        }

        int activeNumberIndex;
        System.out.println(Arrays.toString(array));

        while (true) {
            activeNumberIndex = indexOfActionNumber(array);

            if (activeNumberIndex == -1)
                break;

            // swap node
            Node node = array[activeNumberIndex];
            if (node.orientation == 1) {
                array[activeNumberIndex] = array[activeNumberIndex - 1];
                array[activeNumberIndex - 1] = node;
            } else {
                array[activeNumberIndex] = array[activeNumberIndex + 1];
                array[activeNumberIndex + 1] = node;
            }

            // update all node orientation greater than max active number
            updateOrientation(array, node.number);

            // print array
            System.out.println(Arrays.toString(array));
        }
    }

    /**
     * get max active number of an array
     *
     * @param array array of Node
     * @return index of max active number index; else -1
     */
    private static int indexOfActionNumber(Node[] array) {
        int result = -1;
        int maxNumber = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i].orientation == 1 && i != 0) {

                if (array[i].number > array[i - 1].number && array[i].number > maxNumber) {
                    maxNumber = array[i].number;
                    result = i;
                }

            } else if (array[i].orientation == 2 && i != array.length - 1) {

                if (array[i].number > array[i + 1].number && array[i].number > maxNumber) {
                    maxNumber = array[i].number;
                    result = i;
                }
            }
        }

        return result;
    }

    /**
     * update all node orientation greater than max active number
     */
    private static void updateOrientation(Node[] array, int number) {
        for (Node anArray : array) {
            if (anArray.number > number) {
                if (anArray.orientation == 1) {
                    anArray.orientation = 2;
                } else {
                    anArray.orientation = 1;
                }
            }
        }
    }

}
