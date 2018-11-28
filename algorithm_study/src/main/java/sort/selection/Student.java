package sort.selection;

/**
 * @Auther: 56261
 * @Date: 2018/11/27 20:48
 * @Description:
 */
public class Student implements Comparable<Student> {

    private String name;
    private int socre;

    public Student(String name, int socre) {
        this.name = name;
        this.socre = socre;
    }

    public int compareTo(Student other) {
        if(this.socre < other.socre){
            return 1;
        }else if(this.socre > other.socre){
            return -1;
        }else{ // 相等比较 姓名
            return this.name.compareTo(other.name);
        }
    }


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", socre=" + socre +
                '}';
    }
}
