package lab1.pkg4.pkg2;

import java.math.*;

public class Lab142 {
    public static void main(String[] args) {
        final double da = 1E-7;
        System.out.println("i\t\tL[i]\t\t\tdx[i]\t\t\ti+dx[i]");
        for (int i = 1; i <= 20; i++) {
            double nom = Math.pow((double)i, 19);
            double denom = 1;
            for (int j = 1; j <= 20; j++) {
                if (i == j) continue;
                denom *= (i-j);
            }
            double Li = nom / denom;
            double dx = Li * da;
            double i_dx = i+dx;
            System.out.println(i + "\t" + Li + "\t" + dx + "\t" + i_dx);
        }
    }    
}
