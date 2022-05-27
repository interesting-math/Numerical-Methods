public class Lab112 {
    public static void main(String[] args) {
        System.out.println("Machinery epsilon");
        
        float E1 = 1;
        while (1 + E1 != 1) {
            E1 /= 2;
        }
        System.out.println("float:" + E1);
        
        double E2 = 1;
        while (1 + E2 != 1) {
            E2 /= 2;
        }
        System.out.println("double:" + E2);
    }
    
}
