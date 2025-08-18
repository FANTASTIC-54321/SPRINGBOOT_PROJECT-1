public class Program1 {
    public int Reading(int x)
    {
        System.out.println("The Number is : "+x);
        return 0;
    }
    public static void main(String [] args) {
        Program1 obj = new Program1();
        int x = 10;
        obj.Reading(x);
    }
}