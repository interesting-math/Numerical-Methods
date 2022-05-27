public class Lab123 {
    public static void main(String[] args) {
        System.out.println("Cos(x):");
        for (double x = 0; x < 51; x += 5) {
            double S = 1;
            double a = 1;
            int k = 0;
            while (S + a != S) {    
                    a *= x*x;
                    k++;
                    a /= k;
                    k++;
                    a /= k;
                    a *= -1;
                    S += a;           
            }
            System.out.println("x: " + x + "   S: " + S + "   cos: " + Math.cos(x) + "   relative error: " + Math.abs(Math.cos(x)-S)/Math.cos(x));
        }
        System.out.println();
        for (double x = 0; x < 51; x += 5) {
            double z = x;
            while (true) {
                if (z - 2*Math.PI <= 0) break;
                z -= 2*Math.PI;
            }
            
            double S = 1;
            double a = 1;
            int k = 0;
            while (S + a != S) {    
                    a *= z*z;
                    k++;
                    a /= k;
                    k++;
                    a /= k;
                    a *= -1;
                    S += a;           
            }
            System.out.println("x: " + x + "   S: " + S + "   cos: " + Math.cos(x) + "   relative error: " + Math.abs(Math.cos(x)-S)/Math.cos(x));
        }
        System.out.println();
        System.out.println();
        System.out.println("Sin(x):");
        for (double x = 0; x < 51; x += 5) {
            double S = x;
            double a = x;
            int k = 1;
            while (S + a != S) {    
                    a *= x*x;
                    k++;
                    a /= k;
                    k++;
                    a /= k;
                    a *= -1;
                    S += a;           
            }
            System.out.println("x: " + x + "   S: " + S + "   sin: " + Math.sin(x) + "   relative error: " + Math.abs(Math.sin(x)-S)/Math.sin(x));
        }
        System.out.println();
        for (double x = 0; x < 51; x += 5) {
            double z = x;
            while (true) {
                if (z - 2*Math.PI <= 0) break;
                z -= 2*Math.PI;
            }
            
            double S = z;
            double a = z;
            int k = 1;
            while (S + a != S) {    
                    a *= z*z;
                    k++;
                    a /= k;
                    k++;
                    a /= k;
                    a *= -1;
                    S += a;           
            }
            System.out.println("x: " + x + "   S: " + S + "   sin: " + Math.sin(x) + "   relative error: " + Math.abs(Math.sin(x)-S)/Math.sin(x));
        }
    }
}
