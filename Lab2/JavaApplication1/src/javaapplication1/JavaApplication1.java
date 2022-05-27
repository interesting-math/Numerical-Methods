package javaapplication1;

import java.util.*;

public class JavaApplication1 {
    public static final double EPS = 1E-5;
    
    public static Pair Input_number_of_variables_and_equations() {
        Scanner sc = new Scanner(System.in);
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
        
        Double [][] A;
        Double[] answer;
        
        public Matrix(int m, int n) {
            this.m = m; this.n = n;
            A = new Double[m][n+1];
            answer = new Double[n];
        }
        
        void Input() {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter matrix:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    A[i][j] = sc.nextDouble();
                }
            }
        }
        
        void Gaussian_Forward_Elimination() {
            for (int k = 0; k < m; k++) {
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

                for (int i = 0; i < m; i++) {
                    if (i == k) continue;

                    double multiplier = A[i][k]/A[k][k];
                    for (int j = k; j < n+1; j++) {
                        A[i][j] -= multiplier * A[k][j];
                    }
                }
            }
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
    };
    
    public static void main(String[] args) {
        int n, m;
        Pair p = Input_number_of_variables_and_equations();
        m = p.first; n = p.second;
        
        Matrix A = new Matrix(m, n);
        Matrix B = new Matrix(m, n);
        
        A.Input();
        
        for (int t = 0; t < A.n; t++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    B.A[i][j] = A.A[i][j];
                }
            }
            for (int i = 0; i < m; i++) {
                B.A[i][n] = 0.0;
            }
            B.A[t][n] = 1.0;
            
            B.Gaussian_Forward_Elimination();
            int bottom_border = B.Simplification_and_analisys();
            if (bottom_border != -1) {
                B.Output(bottom_border);
            }
        }
    }
}
