import java.math.*;

public class Lab121 {
    public static void main(String[] args) {
        double step = 1;
        for (double x = 0; x < 51; x += step) {
            double S = 1;
            double a = 1;
            int it = 1;
            while (S + a != S) {
                a *= x;
                a /= it;
                S += a;
                it++;
            }
            System.out.println("x: " + x + " S: " + S + " exp: " + Math.exp(x));
        }
        System.out.println();
        
        System.out.println("The first method:");
        for (double x = -20; x <= -20; x += step) {
            double S = 1;
            double a = 1;
            int it = 1;
            while (S + a != S) {
                a *= x/it;
                S += a;
                System.out.println(it + " " + a + " " + S);
                it++;
            }
            System.out.println("x: " + x + " S: " + S + " exp: " + Math.exp(x));
        }
        
        System.out.println();
        System.out.println("The second method:");
        for (double x = 0; x < 51; x += step) {
            double S = 1;
            double a = 1;
            int it = 1;
            while (S + a != S) {
                a *= x;
                a /= it;
                S += a;
                it++;
            }
            System.out.println("x: " + -x + " S: " + 1/S + " exp: " + Math.exp(-x));
        }
    }
}
