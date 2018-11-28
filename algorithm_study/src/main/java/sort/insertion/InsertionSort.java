package sort.insertion;

import sort.selection.SelectionSort;
import sort.util.SortTestHelper;

/**
 * @Auther: 56261
 * @Date: 2018/11/28 20:07
 * @Description:
 */
public class InsertionSort {

    /**
     * 插入排序， 遍历数组，从前到后，寻找当前位置合适的插入位置
     * 插入排序 当排序数量达到100000复杂度高的话，插入排序比选择排序效率更高
     * <p>
     * 7 2 3 4 1 5 8 原始数据
     * <p>
     * 2 7 3 4 1 5 8 坐标为1数据开始，，2 往前找，2应该插入的位置为坐标1
     * 2 3 7 4 1 5 8
     * 2 3 4 7 1 5 8
     * 1 2 3 4 7 5 8
     * 1 2 3 4 5 7 8
     *
     * @param arr
     */
    public static void insertionSort(Comparable[] arr) {
        int length = arr.length;
        for (int i = 1; i < length; i++) {

            //写法 1
//            for (int j = i; j > 0 && (arr[j].compareTo(arr[j - 1]) < 0); j--) {
//                swap(arr, j, j - 1);
//            }

            // 写法2 优化
            Comparable e = arr[i];
            int j = i;
            for(;j>0 && arr[j-1].compareTo(e)>0;j--){
                arr[j] = arr[j-1];
            }
            arr[j] = e;
        }
    }


    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }


    public static void main(String[] args) {
        Integer[] arr = SortTestHelper.generateRandomArray(100000,0,3);
        SortTestHelper.printArray(arr);
        Integer[] arr1 = arr.clone();
        SortTestHelper.testSort(InsertionSort.class.getName(),"insertionSort",arr);
        SortTestHelper.printArray(arr1);
        SortTestHelper.testSort(SelectionSort.class.getName(),"selectionSort",arr1);
    }
}
