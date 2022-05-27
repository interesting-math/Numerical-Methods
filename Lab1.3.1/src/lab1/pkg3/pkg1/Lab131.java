package lab1.pkg3.pkg1;
public class Lab131 {
    public static void main(String[] args) {
        System.out.println("The first method:");
        final double eps1[] = {0, 1E-5, 1E-6, 1E-7};
        
        System.out.print("k:       ");
        for (int t = 0; t < 4; t++) {
            System.out.print("EPS[" + t + "] = " + eps1[t] + "        " + "\t");
        }
        System.out.println();
        System.out.println();
        
        double E[] = new double[4];
        for (int t = 0; t < 4; t++) {
            E[t] = Math.exp(-1)+eps1[t];
        }
        
        System.out.print("1\t");
        for (int t = 0; t < 4; t++) {
            System.out.print(E[t] + "\t");
        }
        System.out.println();
        for (int k = 2; k <= 20; k++) {
            System.out.print(k+"\t");
            for (int t = 0; t < 4; t++) {
                E[t] = 1-k*E[t];
                System.out.print(E[t] + "\t");
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println("The second method:");
        final double eps2[] = {0, 1E-2, 1E-1, 1.0};
        
        System.out.print("k:       ");
        for (int t = 0; t < 4; t++) {
            System.out.print("EPS[" + t + "] = " + eps2[t] + "        " + "\t");
        }
        System.out.println();
        System.out.println();

        for (int t = 0; t < 4; t++) {
            E[t] = 0;
        }
        for (int k = 40; k >= 1; k--) {
            System.out.print(k+"\t");
            for (int t = 0; t < 4; t++) {
                System.out.print(E[t] + "\t");
                E[t] = (1-E[t])/k;
            }
            System.out.println();
        }
    }
}
