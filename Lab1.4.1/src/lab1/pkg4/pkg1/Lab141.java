package lab1.pkg4.pkg1;

import java.util.*;

public class Lab141 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("i\tL[i]\tdx[i]\ti+dx[i]");
        final double da = 1E-7;
        for (int i = 1; i <= 20; i++) {
            System.out.print(i);
            double nom = Math.pow(i, 19);
            double denom = 1;
            for (int j = 1; j <= 20; j++) {
                if (j != i) {
                    denom *= i-j;
                }
            }
            double Li = nom/denom;
            double dx = Li * da;
            double idx = i+dx;
        }
    } 
}
