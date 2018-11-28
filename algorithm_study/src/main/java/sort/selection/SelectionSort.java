package sort.selection;

import sort.util.SortTestHelper;

/**
 * @Auther: 56261
 * @Date: 2018/11/27 20:14
 * @Description:
 */
public class SelectionSort {

    private SelectionSort(){}

    /**
     * 排序时，寻找[i,n)区间的最小值
     *
     * 使用泛型 自定义排序,排序的对象要实现了comparable接口,
     *
     * @return
     */
//  public static void selectionSort(int[] arr) {
    public static void selectionSort(Comparable[] arr) {
        int length = arr.length;
        for(int i=0;i<length;i++){
            int minIndex = i;
            // 该处 体现出选择排序的思路， 从第一个开始，往后找最小的一个数，得到坐标，与第一个数互换，
            /**
             *    3 1 5 6 7 2 8 4
             *   1  3 5 6 7 2 8 4
             *   1 2  5 6 7 3 8 4
             *   1 2 3  6 7 5 8 4
             *   1 2 3 4  7 5 8 6
             *   1 2 3 4 5  7 8 6
             *   1 2 3 4 5 6  8 7
             *   1 2 3 4 5 6 7  8
             */
            for(int j = i+1;j<length;j++){
              //  if(arr[j] < arr[minIndex])
                if(arr[j].compareTo(arr[minIndex]) < 0)
                    minIndex = j;
            }
            swap(arr,i,minIndex);
        }
    }

    /**
     * 将数组中指定位置的值互换
     * @param arr
     * @param i
     * @param j
     */
//    public static void swap(int[] arr,int i,int j){
    public static void swap(Object[] arr,int i,int j){
//        int temp = arr[i];
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] arr = SortTestHelper.generateRandomArray(10,1,20);
        System.out.println(System.currentTimeMillis());
        selectionSort(arr);
        System.out.println(System.currentTimeMillis());
        for(int i:arr){
            System.out.print(i + " ");
        }
        System.out.println("");
        // 测试自定义student 对象
        Student[] students = {new Student("A",23),new Student("B",21),new Student("C",24),new Student("D",21)};
        selectionSort(students);
        for (Student student:students){
            System.out.println(student.toString());
        }

        SortTestHelper.testSort(SelectionSort.class.getName(),"selectionSort",arr);

    }
}
