package com.wwyl.study.netty_study.util;

/**
 * @Auther: lvla
 * @Date: 2018/11/29 09:39
 * @Description:
 */
public class SortUtil {

    private SortUtil() {
    }

    /**
     * 选择排序
     * @param arr
     */
    public static void selectionSort(Comparable[] arr){
        int length = arr.length;
        for(int i = 0;i<length;i++){
            int minIndex = i;
            for(int j=1+i;j<length;j++){
                if(arr[j].compareTo(arr[minIndex])<0){
                    minIndex = j;
                }
            }
            swap(arr,i,minIndex);
        }
    }

    private static void swap(Object[] arr, int i, int minIndex) {
        Object temp = arr[i];
        arr[i] = arr[minIndex];
        arr[minIndex] =  temp;
    }


    public static void main(String[] args) {
        Integer[] arr = {5,6,1,3,2,4,9,8};
        selectionSort(arr);
        for(Integer i : arr){
            System.out.println(i);
        }
    }
}
