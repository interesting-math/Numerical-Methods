package javaapplication4;

import java.util.*;

public class JavaApplication4 {
    public static final double EPS = 1E-5;
    static Scanner sc = new Scanner(System.in);
    
    public static Pair Input_number_of_variables_and_equations() {
        int m, n;
        System.out.print("Enter number of equation: ");
        m = sc.nextInt();
        System.out.print("Enter number of variables: ");
        n = sc.nextInt();
        return new Pair(m ,n);
    }
    
    public static void Swap_Lines(int k1, int k2, int n, Double[][] A, Boolean[] mark) {
        for (int j = 0; j < n; j++) {
            Double tmp;
            tmp = A[k1][j];
            A[k1][j] = A[k2][j];
            A[k2][j] = tmp;
        }
        Boolean tmp;
        tmp = mark[k1];
        mark[k1] = mark[k2];
        mark[k2] = tmp;
    }
    
    public static class Pair {
        int first;
        int second;

        private Pair(int m, int n) {
            first = m;
            second = n;
        }
    };
    
    public static class Matrix {
        int m, n;
        double E;
        
        Double[][] A;
        Double[][] Current_Matrix; 
        Double[] answer;
        
        public Matrix(int m, int n) {
            this.m = m; this.n = n;
            A = new Double[m][n+1];
            Current_Matrix = new Double[m][n+1];
            answer = new Double[n];
        }
        
        void Prepairing() {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= n; j++) {
                    Current_Matrix[i][j] = A[i][j];
                }
            }
        }
        
        void Input(int right_border) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter matrix:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < right_border; j++) {
                    A[i][j] = sc.nextDouble();
                }
            }
        }
        
        void Gaussian_Forward_Elimination(int right_border) {
            for (int k = 0; k < m; k++) {
/*
                double maxv = 0; int position_of_line_with_maxv = k;
                for (int i = k; i < m; i++) {
                    if (Math.abs(A[i][k]) > maxv) {
                        maxv = Math.abs(A[i][k]);
                        position_of_line_with_maxv = i;
                    }
                }
                for (int j = 0; j < n+1; j++) {
                    double tmp = A[k][j];
                    A[k][j] = A[position_of_line_with_maxv][j];
                    A[position_of_line_with_maxv][j] = tmp;
                }

                if (Math.abs(maxv) < EPS) {
                    continue;
                }
*/
                for (int i = 0; i < m; i++) {
                    if (i == k) continue;

                    double multiplier = A[i][k]/A[k][k];
                    
                    for (int j = k; j < right_border; j++) {
                        A[i][j] -= multiplier * A[k][j];
                    }
                }
            }
        }
      
        void Method_of_Simple_Iteration() {
            System.out.println("Method of simple iteration:");
            
            Prepairing();
            
            for (int i = 0; i < m; i++) {
                double cur_element = Current_Matrix[i][i];
                for (int j = 0; j <= n; j++) {
                    Current_Matrix[i][j] /= cur_element;
                }
                Current_Matrix[i][i] = 0.0;
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Current_Matrix[i][j] *= -1.0;
                }
            }
            for (int i = 0; i < n; i++) {
                answer[i] = Current_Matrix[i][n];
            }
            
            for (int iterator = 0; true; iterator++) {
                Double[] tmp_answer = new Double[n];
                for (int j = 0; j < n; j++) {
                    tmp_answer[j] = 0.0;
                }
                
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        tmp_answer[i] += Current_Matrix[i][j] * answer[j];
                    }
                    tmp_answer[i] += Current_Matrix[i][n];
                }
                if (Check_to_Stop(tmp_answer, answer, E)) {
                    System.out.println("Count of iterations: " + (iterator+1));
                    break;
                }
                for (int i = 0; i < n; i++) {
                    answer[i] = tmp_answer[i];
                }
            }
            for (int i = 0; i < n-1; i++) {
                System.out.print(answer[i] + " ");
            }
            System.out.println(answer[n-1]);
        }
        
        int Simplification_and_analisys() {
            // Пропорциональность строк
            for (int k = 0; k < m; k++) {
                if (Math.abs(A[k][k]) > EPS) {
                    double multiplier = A[k][k];
                    if (Math.abs(multiplier) < EPS) continue;
                    for (int j = k; j < n+1; j++) {
                        A[k][j] /= multiplier;
                    }
                }
            }

            Boolean[] mark = new Boolean[m];
            for (int i = 0; i < m; i++) {
                mark[i] = Boolean.FALSE;
            }

            for (int k1 = 0; k1 < m; k1++) {
                if (mark[k1] == Boolean.TRUE) continue;
                for (int k2 = k1+1; k2 < m; k2++) {
                    boolean is_equal = true;
                    for (int j = 0; j < n+1; j++) {
                        if (Math.abs(A[k1][j] - A[k2][j]) > EPS) {
                            is_equal = false;
                            break;
                        }
                    }
                    if (is_equal) {
                        mark[k2] = true;
                    }
                }
            }
            for (int i = 0; i < m; i++) {
                int cnt_of_zeroes = 0;
                for (int j = 0; j < n+1; j++) {
                    if (Math.abs(A[i][j]) < EPS) {
                        cnt_of_zeroes++;
                        A[i][j] = 0.0;
                    }
                }
                if (cnt_of_zeroes == n+1) {
                    mark[i] = Boolean.TRUE;
                }
                if (cnt_of_zeroes == n && Math.abs(A[i][n]) > EPS) {       
                    System.out.println("The system of equations is inconsistent.");
                    return -1;
                }
            }

            for (int i = 0; i < m; i++) {
                for (int j = i+1; j < m; j++) {
                    if (mark[i] == Boolean.TRUE && mark[j] == Boolean.FALSE) {
                        Swap_Lines(i, j, n, A, mark);
                    }
                }
            }

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n+1; j++) {
                    System.out.print(A[i][j] + " ");
                }
                System.out.println();
            }
            
            int cnt_of_marks = 0;
            for (int i = 0; i < m; i++) {
                if (mark[i] == Boolean.TRUE) cnt_of_marks++;
            }
            int bottom_border = m-1-cnt_of_marks;
            return bottom_border;
        }
        
        boolean Checking_Of_Symmetric_Matrix_For_Square_Root() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (A[i][j] != A[j][i]) {
                        return false;
                    }
                }
            }
            return true;
        }   
        
        void Method_Of_Square_Root_Forward_Elimination() {
            if (m != n) {
                System.out.println("Square root method is not applicable.");
                return;
            }
            
            if (!Checking_Of_Symmetric_Matrix_For_Square_Root()) {
                System.out.println("Square root method is not applicable.");
                System.out.println("Matrix is not symmetric.");
                return;
            }
            
        /*    if (!Checking_Of_Positive_Sign_Matrix_For_Square_Root() {
                
                return;
            }
         */   
            Double G[][] = new Double[n][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    G[i][j] = 0.0;
                }
            }
            
            G[0][0] = Math.sqrt(A[0][0]);
            for (int j = 1; j < n; j++) {
                G[0][j] = A[0][j] / G[0][0];
            }
            
            for (int i = 1; i < n; i++) {
                double partical_sum = 0.0;
                for (int k = 0; k <= i-1; k++) {
                    partical_sum += Math.pow(G[k][i], 2);
                }
                G[i][i] = Math.sqrt(A[i][i] - partical_sum);
                
                System.out.println("# " + G[i][i]);
                
                for (int j = i+1; j < n; j++) {
                    partical_sum = 0.0;
                    for (int k = 0; k <= i-1; k++) {
                        partical_sum += G[k][i]*G[k][j];
                    }
                    G[i][j] = (A[i][j] - partical_sum) / G[i][i];
                }
            }
          
            Double[][] t = new Double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    t[i][j] = G[j][i];
                }
            }
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(t[i][j] + " ");
                }
                System.out.println();
            }
            
            System.out.println();
            
            System.out.println();
            
            Double[][] res = new Double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    res[i][j] = 0.0;
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        res[i][j] += t[i][k] * G[k][j];
                    }
                }
            }
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(res[i][j] + " ");
                }
                System.out.println();
            }  
            
            Method_Of_Square_Root_Reversal_Elimination(G);
        }
        
        void Method_Of_Square_Root_Output(double[] x) {
            for (int i = 0; i < n-1; i++) {
                System.out.println(x[i] + " ");
            }
            System.out.println(x[n-1]);
        }
        
        void Method_Of_Square_Root_Reversal_Elimination(Double[][] G) {
            double[] y = new double[n];
            y[0] = A[0][n]/G[0][0];
            for (int i = 1; i < n; i++) {
                double partical_sum = 0.0;
                for (int k = 0; k <= i-1; k++) {
                    partical_sum += G[k][i]*y[k];
                }
                y[i] = (A[i][n] - partical_sum)/G[i][i];
            }
            
            double[] x = new double[n];
            x[n-1] = y[n-1]/G[n-1][n-1];
            for (int i = n-2; i >= 0; i--) {
                double partical_sum = 0.0;
                for (int k = i+1; k < n; k++) {
                    partical_sum += G[i][k]*x[k];
                }
                x[i] = (y[i] - partical_sum)/G[i][i];
            }
            
            Method_Of_Square_Root_Output(x);
        }
        
        boolean Check_to_Stop(Double x_prev[], Double x_cur[], double E) {
            for (int i = 0; i < n; i++) {
                double d = Math.abs(x_prev[i] - x_cur[i]);
                if (d >= E) return false;
            }
            return true;
        }
        
        void Gauss_Seidel_Method() {    
            System.out.println("Gauss-Seidel Method:");
            
            Prepairing();
            
            for (int i = 0; i < m; i++) {
                double cur_element = Current_Matrix[i][i];
                for (int j = 0; j <= n; j++) {
                    Current_Matrix[i][j] /= cur_element;
                }
                Current_Matrix[i][i] = 0.0;
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Current_Matrix[i][j] *= -1.0;
                }
            }
            for (int i = 0; i < n; i++) {
                answer[i] = Current_Matrix[i][n];
            }
            
            for (int iterator = 0; true; iterator++) {
                Double[] tmp_answer = new Double[n];
                for (int j = 0; j < n; j++) {
                    tmp_answer[j] = 0.0;
                }
                
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < i; j++) {
                        tmp_answer[i] += Current_Matrix[i][j] * tmp_answer[j];
                    }
                    for (int j = i; j < n; j++) {
                        tmp_answer[i] += Current_Matrix[i][j] * answer[j];
                    }
                    tmp_answer[i] += Current_Matrix[i][n];
                }
                if (Check_to_Stop(answer, tmp_answer, E)) {
                    System.out.println("Count of iterations: " + (iterator+1));
                    break;
                }
                for (int i = 0; i < n; i++) {
                    answer[i] = tmp_answer[i];
                }
            }
            for (int i = 0; i < n-1; i++) {
                System.out.print(answer[i] + " ");
            }
            System.out.println(answer[n-1]);
        }
        
        void Output(int bottom_border) {
            if (bottom_border == n-1) {
                for (int k = n-1; k >= 0; k--) {
                    answer[k] = A[k][n] / A[k][k];
                }

                System.out.println("Anser:");
                for (int k = 0; k < n-1; k++) {
                    System.out.print(answer[k] + " ");
                }
                System.out.println(answer[n-1]);
            }
            else {
                int cnt_of_free_variables = n-(bottom_border+1);

                Boolean[] marked_variables = new Boolean[n];
                for (int i = 0; i < n; i++) {
                    marked_variables[i] = Boolean.FALSE;
                }

                for (int j = 0; j < n; j++) {
                    int cnt_of_zeroes = 0;
                    for (int i = 0; i <= bottom_border; i++) {
                        if (Math.abs(A[i][j]) < EPS) {
                            cnt_of_zeroes++;
                        }
                    }
                    if (cnt_of_zeroes == bottom_border+1) {
                        if (cnt_of_free_variables > 0) {
                            marked_variables[j] = Boolean.TRUE;
                            cnt_of_free_variables--;
                        }
                    }
                }
                for (int i = n-1; i >= 0; i--) {
                    if (cnt_of_free_variables == 0) break;
                    marked_variables[i] = Boolean.TRUE;
                    cnt_of_free_variables--;
                }
                System.out.println("Initialization of free variables:");
                for (int i = 0; i < n; i++) {
                    if (marked_variables[i] == Boolean.TRUE) {
                        answer[i] = 1.0;
                        System.out.println("Let: " + i + "-th variable assigned: 1.0" );
                    }
                }
                System.out.println("Answer:");
                for (int i = 0; i < n; i++) {
                    if (marked_variables[i] == Boolean.TRUE) {
                        System.out.println(i+"-th variable is free");
                    }
                }

                for (int i = bottom_border; i >= 0; i--) {
                    double cur_sum = 0;

                    int cur_variable = 0;
                    for (int j = 0; j < n; j++) {
                        if (marked_variables[j] == Boolean.FALSE && Math.abs(A[i][j]) > EPS) {
                            cur_variable = j;
                            break;
                        }
                    }

                    System.out.print("X[" + cur_variable + "] = ");
                    for (int j = 0; j < n; j++) {
                        if (marked_variables[j] == Boolean.TRUE) {
                            cur_sum += answer[j] * A[i][j];
                            System.out.print("+(" + -A[i][j] + "/" + A[i][cur_variable] + ")" + "*X[" + j + "] ");
                        }
                    }
                    System.out.println();

                    cur_sum *= -1;
                    cur_sum += A[i][n];

                    for (int j = 0; j < n; j++) {
                        if (marked_variables[j] == Boolean.FALSE && Math.abs(A[i][j]) > EPS) {
                            answer[j] = cur_sum / A[i][j];
                            marked_variables[j] = Boolean.TRUE;
                            break;
                        }
                    }
                }
            
                System.out.println("One of the solutions:");
                for (int k = 0; k < n-1; k++) {
                    System.out.print(answer[k] + " ");
                }
                System.out.println(answer[n-1]);
            }
        }
        
        void Minimum_Discrepancy_Method_Out_Answer(Double[] ans) {
            for (int i = 0; i < n-1; i++) {
                System.out.print(ans[i] + " ");
            }
            System.out.println(ans[n-1]);
        }
        
        void Minimum_Discrepancy_Method() {
            System.out.println("Minimum Discrepancy Method:");
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (A[i][j] < A[j][i]-EPS || A[i][j] > A[j][i]+EPS) {
                        System.out.println("Matrix is not simmetric.");
                        return;
                    }
                }
            }
            
            System.out.print("Enter EPS: ");
            E = sc.nextDouble();
            
            for (int i = 0; i < n; i++) {
                answer[i] = 0.0;
            }
            for (int iteration = 0; true; iteration++) {
                double[] Xi = new double[n];
                double[] Ax = new double[n];
                for (int i = 0; i < n; i++) {
                    Ax[i] = 0.0;
                }
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        Ax[i] += A[i][j] * answer[j];
                    }
                }
                for (int i = 0; i < n; i++) {
                    Xi[i] = Ax[i] - A[i][n];
                }
                double[] Axi = new double[n];
                for (int i = 0; i < m; i++) {
                    Axi[i] = 0.0;
                    for (int j = 0; j < n; j++) {
                        Axi[i] += A[i][j]*Xi[j];
                    }
                }
                double Tau = Scalar_Product(Axi, Xi)/Scalar_Product(Axi, Axi);
                
                boolean the_break_condition_is_satisfied = true;
                for (int i = 0; i < n; i++) {
                    answer[i] = answer[i] - Tau*Xi[i];
                }
                
                for (int i = 0; i < n; i++) {
                    if (Math.abs(Tau*Xi[i]) >= E) {
                        the_break_condition_is_satisfied = false;
                    }
               }
                if (the_break_condition_is_satisfied) {
                    System.out.println("Number of iterations: " + iteration);
                    break;
                }
            }
            Minimum_Discrepancy_Method_Out_Answer(answer);
        }
        
        double Scalar_Product(double[] Arg1, double[] Arg2) {
            double scalar_product = 0.0;
            for (int i = 0; i < n; i++) {
                scalar_product += Arg1[i] * Arg2[i];
            }
            return scalar_product;
        }
        
        void Conjugate_Gradient_Method_Out_Answer(Double[] ans) {
            for (int i = 0; i < n-1; i++) {
                System.out.print(ans[i] + " ");
            }
            System.out.println(ans[n-1]);
        }
        
        void Conjugate_Gradient_Method() {
            System.out.println("Conjugate Gradient Method:");
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (A[i][j] < A[j][i]-EPS || A[i][j] > A[j][i]+EPS) {
                        System.out.println("Matrix is not simmetric.");
                        return;
                    }
                }
            }
            
            System.out.print("Enter EPS: ");
            E = sc.nextDouble();
           
            for (int i = 0; i < n; i++) {
                answer[i] = 0.0;
            }
            double[] Xi = new double[n];
            double[] Ax = new double[n];
            for (int i = 0; i < m; i++) {
                Ax[i] = 0.0;
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Ax[i] += A[i][j] * answer[j];
                }
            }
            for (int i = 0; i < n; i++) {
                Xi[i] = A[i][n] - Ax[i];
            }
            
            double[] p = new double[n];
            for (int i = 0; i < n; i++) {
                p[i] = Xi[i];
            }
            
            for (int iteration = 1; true; iteration++) {
                double[] q = new double[n];
                for (int i = 0; i < m; i++) {
                    q[i] = 0.0;
                    for (int j = 0; j < n; j++) {
                        q[i] += A[i][j] * p[j];
                    }
                }

                double alpha = Scalar_Product(Xi, p) / Scalar_Product(Xi, q);

                for (int i = 0; i < n; i++) {
                    answer[i] = answer[i] + alpha * p[i];
                }
                
                boolean is_break_statement_satisfied = true;
                for (int i = 0; i  < n; i++) {
                    if (Math.abs(alpha * p[i]) >= E) {
                        is_break_statement_satisfied = false;
                        break;
                    }
                }
                if (is_break_statement_satisfied) {
                    System.out.println("Number of iterations: " + iteration);
                    break;
                }
                
                double[] Alpha_Q = new double[n];
                for (int i = 0; i < m; i++) {
                    Alpha_Q[i] = 0.0;
                    for (int j = 0; j < n; j++) {
                        Alpha_Q[i] += A[i][j] * p[j];
                    }
                    Alpha_Q[i] *= alpha;
                }
                for (int i = 0; i < n; i++) {
                    Xi[i] = Xi[i] - Alpha_Q[i];
                }
                
                double beta = Scalar_Product(Xi, q) / Scalar_Product(p, q);

                for (int i = 0; i < n; i++) {
                    p[i] = Xi[i] - beta * p[i];
                }
            }
            
            Conjugate_Gradient_Method_Out_Answer(answer);
        }
        
        int Checking_Positively_Certain() {
            int number_of_positive_minors = 0;
            int number_of_negative_minors = 0;
            
            double[] minors = new double[n];
            
            for (int t = 0; t < n; t++) {
                Matrix tmp = new Matrix(t+1, t+1);
                
                for (int i = 0; i < t+1; i++) {
                    for (int j = 0; j < t+1; j++) {
                        tmp.A[i][j] = A[i][j];
                    }
                }
                
                tmp.Gaussian_Forward_Elimination(t+1);
                double d = 1.0;
                for (int k = 0; k < t+1; k++) d *= tmp.A[k][k];
                
                minors[t] = d;
                
                if (d > 0) number_of_positive_minors++;
                if (d < 0) number_of_negative_minors++;
            }
            
            if (number_of_positive_minors == n) return 1;
            boolean is_negative_certain = true;
            for (int t = 0; t < n; t++) {
                if (t%2 == 0 && minors[t] > 0) {
                    is_negative_certain = false;
                    break;
                }
                if (t%2 == 1 && minors[t] < 0) {
                    is_negative_certain = false;
                    break;
                } 
            }
            if (is_negative_certain) return -1;
            return 0;            
        }
        
        double Maximum_Eigenvalue_Of_Matix() {
            double l = 0.0;
            
            double[] x = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = 0.0;
            }
            x[0] = 1.0;
            
            for (int iteration = 1; true; iteration++) {
                double norm = 0.0;
                for (int i = 0; i < n; i++) {
                    norm += x[i]*x[i];
                }
                norm = Math.sqrt(norm);

                double[] e = new double[n];
                for (int i = 0; i < n; i++) {
                    e[i] = x[i]/norm;
                }

                for (int i = 0; i < m; i++) {
                    x[i] = 0.0;
                    for (int j = 0; j < n; j++) {
                        x[i] += A[i][j] * e[j];
                    }
                }
             
                if (Math.abs(l - Scalar_Product(x, e)) < E) {
                    System.out.println("Number of iteration: " + iteration);
                    return l;
                }
                
                l = Scalar_Product(x, e);
            }
        }
        
        void Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors_Out_Eigenvalues() {
            System.out.print("Eigenvalues: ");
            for (int i = 0; i < n-1; i++) {
                System.out.print(A[i][i] + " ");
            }
            System.out.println(A[n-1][n-1] + " ");
        }
        
        void Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors_Out_Eigenvectors(int[][] indexes_of_support_elements, double[][] coefficients, int count_of_iteration) {
            double[][] eigenvectors = new double[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) eigenvectors[i][j] = 1.0;
                    else eigenvectors[i][j] = 0.0;
                }
            }
            
            for (int iteration = 0; iteration < count_of_iteration; iteration++) {
                double[][] current_simple_rotation_matrix = new double[m][n];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i == j) current_simple_rotation_matrix[i][j] = 1.0;
                        else current_simple_rotation_matrix[i][j] = 0.0;
                    }
                }
                
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        System.out.print(current_simple_rotation_matrix[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
                
                double sum = Math.pow(coefficients[iteration][0], 2.0) + Math.pow(coefficients[iteration][1], 2.0);
                if (sum < 1.0 - EPS) {
                    System.out.println("Error: iteration = " + iteration + "; " + sum);
                    System.out.println("C: " + coefficients[iteration][0] + "; S: " + coefficients[iteration][1]);
                }
                else if (sum > 1.0 + EPS) {
                    System.out.println("Error: iteration = " + iteration + "; " + sum);
                }
                
                current_simple_rotation_matrix[indexes_of_support_elements[iteration][0]][indexes_of_support_elements[iteration][0]] = coefficients[iteration][0]; 
                current_simple_rotation_matrix[indexes_of_support_elements[iteration][1]][indexes_of_support_elements[iteration][1]] = coefficients[iteration][0];
        
                current_simple_rotation_matrix[indexes_of_support_elements[iteration][0]][indexes_of_support_elements[iteration][1]] = -coefficients[iteration][1];
                current_simple_rotation_matrix[indexes_of_support_elements[iteration][1]][indexes_of_support_elements[iteration][0]] = coefficients[iteration][1];
            
                double[][] result = new double[m][n];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        result[i][j] = 0.0;
                    }
                }
                
                for (int i = 0; i < m; i++) {                    
                    for (int j = 0; j < n; j++) {
                        for (int k = 0; k < n; k++) {
                            result[i][j] += eigenvectors[i][k] * current_simple_rotation_matrix[k][j];
                        }
                    }
                }
                
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        eigenvectors[i][j] = result[i][j];
                    }
                }
            }   
            
            System.out.println("Eigenvectors:");
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < m-1; i++) {
                    System.out.print(eigenvectors[i][j] + " ");
                }
                System.out.println(eigenvectors[m-1][j]);
            }
        }
        
        void Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors() {
            // 1
            Pair pos = new Pair(1, 0);
            
            // динамически добавлять в массивы coefficients, indexes_of_support_elements соответствующие элементы 
           
            double[][] coefficients = new double[2*m][2];
            int[][] indexes_of_support_elements = new int[2*m][2];
            
            for(int iteration = 0; true; iteration++) {
                System.out.println("HELLO!");
                
                double maxv = 0.0;
                pos.first = 0; pos.second = 0;
                for (int i = 1; i < m; i++) {
                    for (int j = 0; j < i; j++) {
                        if (Math.abs(A[i][j]) > maxv) {
                            maxv = Math.abs(A[i][j]);
                            pos.first = i; pos.second = j;
                        }
                    }
                }
                
                indexes_of_support_elements[iteration][0] = pos.first;
                indexes_of_support_elements[iteration][1] = pos.second;
                System.out.println("i: " + pos.first + ", j: " + pos.second);
                
                // 2
                double p = 2*A[pos.first][pos.second];
                double q = A[pos.first][pos.first] - A[pos.second][pos.second];
                double d = Math.sqrt(p*p + q*q);

                double C, S;

                if (Math.abs(q) < EPS) {
                    C = S = Math.sqrt(2.0)/2;
                }
                else {
                    double r = Math.abs(q)/(2.0 * d);
                    C = Math.sqrt(0.5 + r);
                    S = Math.sqrt(0.5 - r) * Math.signum(p*q);
                
                    // Намного меньше
                    if (-EPS < Math.abs(p) && Math.abs(p) < EPS) {
                        S = (Math.abs(p) * Math.signum(p*q))/(2*C*d);
                    }
                }
                
                coefficients[iteration][0] = C; coefficients[iteration][1] = S;
                System.out.println("C: " + C + "; S: " + S);
                // 4
                double[][] B = new double[m][n];
                B[pos.first][pos.first] = Math.pow(C, 2.0) * A[pos.first][pos.first] + Math.pow(S, 2.0) * A[pos.second][pos.second] + 2*C*S*A[pos.first][pos.second];
                B[pos.second][pos.second] = Math.pow(S, 2.0) * A[pos.first][pos.first] + Math.pow(C, 2.0) * A[pos.second][pos.second] - 2*C*S*A[pos.first][pos.second];

                B[pos.first][pos.second] = B[pos.second][pos.first] = 0.0;

                // 5
                for (int t = 0; t < n; t++) {
                    if (t == pos.first || t == pos.second) continue;

                    B[pos.first][t] = B[t][pos.first] = C * A[t][pos.first] + S * A[t][pos.second];
                    B[pos.second][t] = B[t][pos.second] = -S*A[t][pos.first] + C*A[t][pos.second];
                }

                // 6
                for (int i = 0; i < m; i++) {
                    if (i == pos.first || i == pos.second) continue;
                    for (int j = 0; j < n; j++) {
                        if (j == pos.first || j == pos.second) continue;
                        B[i][j] = A[i][j];
                    }
                }

                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        A[i][j] = B[i][j];
                    }
                }

                boolean is_break_statement_satisfied = true;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i == j) continue;
                        if (A[i][j] > EPS) {
                            is_break_statement_satisfied = false;
                            break;
                        }
                    }
                    if (!is_break_statement_satisfied) break;
                }

                if (is_break_statement_satisfied) {
                    Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors_Out_Eigenvalues();
                    Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors_Out_Eigenvectors(indexes_of_support_elements, coefficients, iteration+1);
                    break;
                }
            }
            System.out.println("Result matrix B:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n-1; j++) {
                    System.out.print(A[i][j] + " ");
                }
                System.out.println(A[i][n-1]);
            }
            System.out.println();
        }
    };
    
    static double Function_For_Bisection_Method(double x) {
    //    return 4.0 - Math.exp(x) - 2*Math.pow(x, 2.0);
        return Math.pow(x, 3.0) - 6*x+2;
    }
    
    static boolean Bisection_Method_Checking(double a, double b) {
        if (Function_For_Bisection_Method(a) * Function_For_Bisection_Method(b) < 0.0) {
            return true;
        }
        return false;
    }
    
    static void Bisection_Method_Out_Result(double x) {
        System.out.println("x = " + x);
    }
    
    static void Bisection_Method() {
        double E;
        double l, r;
        System.out.println("Enter EPS, l, r:");
        Scanner sc = new Scanner(System.in);
        E = sc.nextDouble();
        l = sc.nextDouble();
        r = sc.nextDouble();
                
        if (!Bisection_Method_Checking(l, r)) {
            System.out.println("Root doesn't exist on this segment");
            return;
        }
        while (r-l >= E) {
            double c = (l+r)/2;
            if (Bisection_Method_Checking(l, c)) {
                r = c;
            }
            else {
                l = c;
            }
        }
        Bisection_Method_Out_Result(l);
    }
    
    static double Function_For_Method_Of_Tangents(double x) {
        return Math.pow(x, 3) - 6*Math.pow(x, 2) + 3*x + 11;
    }
    
    static double First_Derivative_Of_Function_For_Method_Of_Tangents(double x) {
        return 3*Math.pow(x, 2) - 12*x + 3;
    }
    
    static double Second_Derivative_Of_Function_For_Method_Of_Tangents(double x) {
        return 6*x - 12;
    }
    
    static void Method_Of_Tangents() {
        Scanner sc = new Scanner(System.in);
        
        double E;
        double l, r;
        
        System.out.print("Enter EPS: ");
        E = sc.nextDouble();
        System.out.print("Enter left bound: ");
        l = sc.nextDouble();
        System.out.print("Enter right bound: ");
        r = sc.nextDouble();
        System.out.print("Enter initial approach: ");
        double X = sc.nextDouble();
        
        if (Function_For_Method_Of_Tangents(X)*Second_Derivative_Of_Function_For_Method_Of_Tangents(X) <= E) {
            System.out.println("Initial approach isn't correct for this method.");
            return;
        }
        
        for (int iteration = 0; true; iteration++) {
            double x_next = X - Function_For_Method_Of_Tangents(X)/First_Derivative_Of_Function_For_Method_Of_Tangents(X);
            if (Math.abs(x_next - X) < E) {
                break;
            }
            X = x_next;
        }
        System.out.println("Answer:");
        System.out.println("x: " + X);
        System.out.println("f(x): " + Function_For_Method_Of_Tangents(X));
        if (X < l-E || X > r+E) {
            System.out.println("This zero of function is out of the considered piece.");
        }
    }
    
    static double Function_For_Coordinate_Descent_Method(double[] x) {
        return Math.pow(x[1]-Math.pow(x[0], 2.0), 2.0) + Math.pow(1.0 - x[0], 2.0);
    }
    
    static boolean Break_Condition_1_For_Coordinate_Descent_Method(int n, double[] x_cur, double[] x_prev, double E) {
        for (int i = 0; i < n; i++) {
            if (Math.abs(x_cur[i] - x_prev[i]) >= E) return false;
        }
        return true;
    }
    
    static boolean Break_Condition_2_For_Coordinate_Descent_Method(int n, double[] x_cur, double[] x_prev, double E) {
        if (Function_For_Coordinate_Descent_Method(x_cur)-Function_For_Coordinate_Descent_Method(x_prev) < E) {
            return true;
        }
        return false;
    }
    
    static void Coordinate_Descent_Method() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Optimization using coordinate descent method");
        System.out.println();
        
        int n = 2;
        System.out.println("f(x) = Math.pow(x[1] - x[0]*x[0], 2.0) + Math.pow(1.0 - x[0], 2.0) -> min");
     /*
        System.out.print("Enter n (dimention of space): ");
        n = sc.nextInt();
     */ 
        double E;
        System.out.print("Enter EPS: ");
        E = sc.nextDouble();
        
        System.out.println("Enter initial approach: ");
        System.out.print("X0: ");
        
        double[] X0 = new double[n];
        for (int i = 0; i < n; i++) {
            X0[i] = sc.nextDouble();
        }
        double[] x_prev = new double[n];
        for (int i = 0; i < n; i++) {
            x_prev[i] = X0[i];
        }
        double[] x_cur = new double[n];
        for (int i = 0; i < n; i++) {
            x_cur[i] = x_prev[i];
        }
        
        System.out.println("");
        
        for (int iteration = 1; true; iteration++) {
            System.out.println("iteration " + iteration + ": ");
            for (int i = 0; i < n; i++) {
                if (i == 0) System.out.println("Optimization of the first coordinate:");
                if (i == 1) System.out.println("Optimization of the second coordinate:"); 
                Golden_Section_Search(x_cur, i, E, n);  
            }
            System.out.println("f(current x): " + Function_For_Coordinate_Descent_Method(x_cur));
            System.out.println();
            if (Break_Condition_1_For_Coordinate_Descent_Method(n , x_cur, x_prev, E)) {
                System.out.println("Number of iterations: " + iteration);
                break;
            }
            for (int i = 0; i < n; i++) {
                x_prev[i] = x_cur[i];
            }
            
            System.out.println();
        }
        
        System.out.print("X*: ");
        System.out.print("{");
        for (int i = 0; i < n-1; i++) {
            System.out.print(x_cur[i] + ", ");
        }
        System.out.println(x_cur[n-1] + "}");
        
        System.out.println("min(f(x)): " + Function_For_Coordinate_Descent_Method(x_cur));
    }
    
    static double Function_For_Golden_Section_Search(double[] x, double n) {
        // return 2.0 * Math.pow(x-n/20.0, 2.0) + 16.0/x;
        return Math.pow(x[1] - x[0]*x[0], 2.0) + Math.pow(1.0 - x[0], 2.0);
    }
    
    static void Golden_Section_Search(double[] x, int coordinate_for_optimization, double E, int n) {
        final double phi = (Math.sqrt(5.0)-1.0)/2.0 + 1.0;
  
  /*
        Scanner sc = new Scanner(System.in);
              
        double n;
        System.out.print("Enter n (variant of your work): ");
        n = sc.nextDouble();
  */
        
  /*      
        System.out.print("Enter the boundaries of the segment (left, right): ");
        double a = sc.nextDouble(); double b = sc.nextDouble();
  */      
        double a = -10.0;
        double b = 10.0;
  
        double x0 = 0.0;
        
        for(int it = 1; true; it++) {
            double len = b-a;
            double prev_a = a, prev_b = b;

            x0 = a;
            int iteration = 0;

    //        System.out.println("phi: " + phi);
            
            for (iteration = 1; true; iteration++) {
/*
                System.out.println("iteration: " + iteration);

                System.out.println("left: " + a + "; right: " + b);
*/
                double x1 = b - (b-a)/phi;
                double x2 = a + (b-a)/phi;

//                System.out.println("x1: " + x1 + "; x2: " + x2);

                x[coordinate_for_optimization] = x1;
                double y1 = Function_For_Golden_Section_Search(x, coordinate_for_optimization);
                x[coordinate_for_optimization] = x2;
                double y2 = Function_For_Golden_Section_Search(x, coordinate_for_optimization);

//                System.out.println("y1: " + y1 + "; y2: " + y2);

                if (y1 >= y2) {
//                    System.out.println("y1 > y2");

                    a = x1; x1 = x2; x2 = a + (b-a)/phi;

/*                    
                    System.out.println("a = x1; x1 = x2; x2 = a + (b-a)/phi;");

                    System.out.println("a: " + a);
                    System.out.println("b: " + b);

                    System.out.println("x1: " + x1);
                    System.out.println("x2: " + x2);
*/
                }
                else {
//                    System.out.println("y1 <= y2");

                    b = x2; x2 = x1; x1 = b - (b-a)/phi;

/*                    
                    System.out.println("b = x2; x2 = x1; x1 = b - (b-a)/phi;");

                    System.out.println("a: " + a);
                    System.out.println("b: " + b);

                    System.out.println("x1: " + x1);
                    System.out.println("x2: " + x2);
*/
                }

                if (Math.abs(b-a) < E) {
                    x0 = (a+b)/2;
                    break;
                }

//                System.out.println();
            }
    
            boolean is_bound = false;
            if (Math.abs(x0 - prev_a) < E) {
                prev_b = prev_a + 1.0;
                prev_a -= 2*len;
                is_bound = true;
            }
            if (Math.abs(x0 - prev_b) < E) {
                prev_a = prev_b - 1.0;
                prev_b += 2*len;
                is_bound = true;
            }
            if (!is_bound) {
                break;
            }            
            
            /*
            System.out.println();
            System.out.println();
            System.out.println("x: " + x0);
            System.out.println("extr(loc. min): " + Function_For_Golden_Section_Search(x, coordinate_for_optimization));
            System.out.println("Number of iteration: " + iteration);
            */
        }
        
        x[coordinate_for_optimization] = x0;
        

        System.out.println("current x: ");
        System.out.print("{");
        for (int i = 0; i < n-1; i++) {
            System.out.print(x[i] + ", ");
        }
        System.out.println(x[n-1] + "}");
        System.out.println();
    }
        
    public static double Function_For_Steepest_Descent_Method(int n, double[] x) {
        return Math.pow(x[1] - Math.pow(x[0], 2.0), 2.0) + Math.pow(1.0 - x[0], 2.0);
    }
    
    public static double[] Gradient_For_Steepest_Descent_Method(int n, double[] x) {
        double[] gradient = new double[n];
        gradient[0] = -4.0*x[0]*(x[1] - Math.pow(x[0], 2.0)) - 2.0*(1.0 - x[0]);
        gradient[1] = 2.0*(x[1] - Math.pow(x[0], 2.0));
        return gradient;
    }
    
    
    static double Function_For_Golden_Section_Search_For_Steepest_Descent_Method(double[] x, int coordinate_of_lambda, double[] gradient) {
        // coordinate_of_lambda = n (default)
        int n = coordinate_of_lambda;
        
        double[] x_new = new double[coordinate_of_lambda];
        for (int i = 0; i < n; i++) {
            x_new[i] = x[i] - x[coordinate_of_lambda] * gradient[i];
        }
        return Function_For_Steepest_Descent_Method(n, x_new);
    }
    
    static void Golden_Section_Search_For_Steepest_Descent_Method(double[] x, int coordinate_for_optimization, double E, double[] gradient) {
        final double phi = (Math.sqrt(5.0)-1.0)/2.0 + 1.0;
        int n = coordinate_for_optimization;
        
        double a = 0.0;
        double b = 10.0;
  
        double lambda = 0.0;
        
        for(int it = 1; true; it++) {
            double len = b-a;
            double prev_a = a, prev_b = b;

            lambda = a;
            int iteration = 0;

            for (iteration = 1; true; iteration++) {
                
                double x1 = b - (b-a)/phi;
                double x2 = a + (b-a)/phi;

                x[coordinate_for_optimization] = x1;
                double y1 = Function_For_Golden_Section_Search_For_Steepest_Descent_Method(x, coordinate_for_optimization, gradient);
                x[coordinate_for_optimization] = x2;
                double y2 = Function_For_Golden_Section_Search_For_Steepest_Descent_Method(x, coordinate_for_optimization, gradient);

                if (y1 >= y2) {
                    a = x1; x1 = x2; x2 = a + (b-a)/phi;
                }
                else {
                    b = x2; x2 = x1; x1 = b - (b-a)/phi;
                }

                if (Math.abs(b-a) < E) {
                    lambda = (a+b)/2;
                    break;
                }
            }
    
            boolean is_bound = false;
            if (Math.abs(lambda - prev_b) < E) {
                prev_a = prev_b - 1.0;
                prev_b += 2*len;
                is_bound = true;
            }
            if (!is_bound) {
                break;
            }
            /*
            System.out.println();
            System.out.println();
            System.out.println("x: " + lambda);
            System.out.println("extr(loc. min): " + Function_For_Golden_Section_Search_For_Steepest_Descent_Method(x, coordinate_for_optimization, gradient));
            System.out.println("Number of iteration: " + iteration);
            */
        }
        x[coordinate_for_optimization] = lambda;
    }
    
    public static void Steepest_Descent_Method() {
        System.out.println("Optimization using steepest descent method");
        System.out.println("f(x) = Math.pow(x[1] - x[0]*x[0], 2.0) + Math.pow(1.0 - x[0], 2.0) -> min");

        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the dimention of a space: ");
        int n = sc.nextInt();
        
        System.out.print("Enter X0: ");
        double[] x = new double[n+1];
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextDouble();
        }
        
        System.out.print("Enter EPS: ");
        double E = sc.nextDouble();
        
        for (int iteration = 1; true; iteration++) {
            System.out.println();
            System.out.println("iteration: " + iteration);
            
            double[] gradient = Gradient_For_Steepest_Descent_Method(n, x);
           
            System.out.println("Gradient:");
            System.out.print("{");
            for (int i = 0; i < n-1; i++) {
                System.out.print(gradient[i] + ", ");
            }
            System.out.println(gradient[n-1] + "}");
            
            Golden_Section_Search_For_Steepest_Descent_Method(x, n, E, gradient);
            
            double lambda = x[n];
            
            double[] x_next = new double[n+1];
            for (int i = 0; i < n; i++) {
                x_next[i] = x[i] - lambda*gradient[i];
            }
        
            boolean is_break_statement_satisfied = true;
            for (int i = 0; i < n; i++) {
                if (Math.abs(x_next[i] - x[i]) >= E) {
                    is_break_statement_satisfied = false;
                    break;
                }
            }
            
            if (is_break_statement_satisfied) {
                System.out.println();   
                System.out.println("Number of iterations: " + iteration);
                break;
            }
            
            for (int i = 0; i < n; i++) { 
                x[i] = x_next[i];
            }
            
            System.out.println("current x: ");
            System.out.print("{");
            for (int i = 0; i < n-1; i++) {
                System.out.print(x[i] + ", ");
            }
            System.out.println(x[n-1] + "}");    
            
            System.out.println("f(current x): " + Function_For_Steepest_Descent_Method(n, x));
        }
        
        System.out.print("X*: ");
        System.out.print("{");
        for (int i = 0; i < n-1; i++) {
            System.out.print(x[i] + ", ");
        }
        System.out.println(x[n-1] + "}");
        
        System.out.println("min(f(x)): " + Function_For_Steepest_Descent_Method(n, x));
    }
    
    static double Exact_Solution_For_Runge_Kutta_Method(double x) {
        return 0.25*(-6.0*x - 7*Math.exp(-2*x)+7);
    }
    
    static double Derivative_For_Runge_Cutta_Method(double x, double y) {
        return -2.0*y-3.0*x+2.0;
    }
    
    static double coeffRK(double x, double y, double h, int p) {
/*
        Неклассический вариант для 4-го порядка
        double k1 = h * Derivative_For_Runge_Cutta_Method(x, y);
        double k2 = h * Derivative_For_Runge_Cutta_Method(x + h/3, y + k1/3);
        double k3 = h * Derivative_For_Runge_Cutta_Method(x + 2*h/3, y + k1/3 + k2);
        double k4 = h * Derivative_For_Runge_Cutta_Method(x + h, y + k1 - k2 + k3);
 */
        if (p == 1) {
            // Euler method
            return h*Derivative_For_Runge_Cutta_Method(x, y);
        }
        if (p == 2) {
            double K1 = Derivative_For_Runge_Cutta_Method(x, y);
            double K2 = Derivative_For_Runge_Cutta_Method(x + h/2, y + h/2 * K1);
            return h*K2;
        }
        if (p == 3) {
            double K1 = h*Derivative_For_Runge_Cutta_Method(x, y);
            double K2 = h*Derivative_For_Runge_Cutta_Method(x + h/2, y + K1/2);
            double K3 = h*Derivative_For_Runge_Cutta_Method(x + h, y + 2*K2 - K1);
            return (K1 + 4*K2 + K3)/6.0;
        }
        if (p == 4) {
            double K1 = Derivative_For_Runge_Cutta_Method(x, y);
            double K2 = Derivative_For_Runge_Cutta_Method(x + h/2, y + h/2 * K1);
            double K3 = Derivative_For_Runge_Cutta_Method(x + h/2, y + h/2 * K2);
            double K4 = Derivative_For_Runge_Cutta_Method(x + h, y + h * K3);
            return (K1 + 2*K2 + 2*K3 + K4)*h/6;
        }   
    }
    
    static void Runge_Kutta_Method() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter X0: ");
        double x = sc.nextDouble();
        
        System.out.print("Enter Y0: ");
        double y = sc.nextDouble();
        
        System.out.print("Enter [a, b]: ");
        double a, b;
        a = sc.nextDouble();
        b = sc.nextDouble();
        
        double h = b-a;
        
        System.out.print("Enter EPS: ");
        double E = sc.nextDouble();
      
        while (x <= 1.0) {
            // Четвертый порядок метода Рунге-Кутты
            double y1 = y + coeffRK(x, y, h, 4);
            
            while (true) {
                double y2 = y + coeffRK(x, y, h/2, 4);
                double y3 = y2 + coeffRK(x + h/2, y2, h/2, 4);

                System.out.println();
                System.out.print("x: " + x + ", ");
                System.out.println("y: " + y);

                System.out.println("exact: " + Exact_Solution_For_Runge_Kutta_Method(x));
            
                double delta = Math.abs(y1 - y3) / 15;
                System.out.println("delta: " + delta);
                
                if (delta < E) { 
                    y = y3;
                    
                    x = x + h;
                    
                    if (delta < E/2) {
                        h *= 2;
                    }
                    break;
                }
                h /= 2.0; y1 = y2;
            }
        }
    }
    
    static double Z_Derivative(double x, double y, double z) {
        return Math.exp(x) + Math.sin(y);
    }
    
    static void Runge_Kutta_Method_For_Shooting_Method(double x, double y, double a, double b, double E, int p) {
        double h = b-a;
        if (p == 2) {
            while (x <= b) {
                
            }
        }
        
        if (p == 4) {
            while (x <= 1.0) {
            // Четвертый порядок метода Рунге-Кутты
            double y1 = y + coeffRK(x, y, h, 4);
            
            while (true) {
                double y2 = y + coeffRK(x, y, h/2, 4);
                double y3 = y2 + coeffRK(x + h/2, y2, h/2, 4);

                System.out.println();
                System.out.print("x: " + x + ", ");
                System.out.println("y: " + y);

                System.out.println("exact: " + Exact_Solution_For_Runge_Kutta_Method(x));
            
                double delta = Math.abs(y1 - y3) / 15;
                System.out.println("delta: " + delta);
                
                if (delta < E) { 
                    y = y3;
                    
                    x = x + h;
                    
                    if (delta < E/2) {
                        h *= 2;
                    }
                    break;
                }
                h /= 2.0; y1 = y2;
            }
        }
        }
    }
    
    static void Shooting_Method() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter a: ");
        double a = sc.nextDouble();
        
        System.out.print("Enter f(a): ");
        double fa = sc.nextDouble();
        
        System.out.print("Enter b: ");
        double b = sc.nextDouble();
        
        System.out.print("Enter f(b): ");
        double fb = sc.nextDouble();
        
        System.out.print("Enter h: ");
        double h = sc.nextDouble();
        
        System.out.print("Enter EPS: ");
        double E = sc.nextDouble();
        
        System.out.print("Enter p: ");
        int p = sc.nextInt();
        
        double m1 = 1.0, m2 = 0.8;
        
        for (int iteration = 0; true; iteration++) {
            double x = a;
            
            
        }
        
    }
    
    public static void main(String[] args) {  
      //  Runge_Kutta_Method();
       
        Shooting_Method(); 
        
        /*
        
        int n, m;
        Pair p = Input_number_of_variables_and_equations();
        m = p.first; n = p.second;
        
        Matrix A = new Matrix(m, n);
        
        A.Input(n);
        A.Jacobi_Rotation_Method_For_Finding_Eigenvalues_And_Eigenvectors();
        
        */
        
        /*
        if (A.Checking_Positively_Certain() == 1) {
            A.Conjugate_Gradient_Method();
            Matrix B = new Matrix(m, n);
            B = A;
            B.Minimum_Discrepancy_Method();
        }
        */
       
        /*
        System.out.print("Enter EPS: ");
        A.E = sc.nextDouble();
        System.out.println("Maximum eigenvalue of matix: " + A.Maximum_Eigenvalue_Of_Matix());    
        */
        
    //    Bisection_Method();
    }
}