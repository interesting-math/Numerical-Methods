public class Lab111 {
    public static void main(String[] args) {
        System.out.println("Machinery zero:");
        
        float E1 = 1;
        while (E1 > 0) {
            if (E1/2 > 0) E1/= 2;
            else break;
        }
        System.out.println("float:" + E1);
        
        double E2 = 1;
        while (E2 > 0) {
            if (E2/2 > 0) E2/= 2;
            else break;
        }
        System.out.println("double:" + E2);
    }
}
