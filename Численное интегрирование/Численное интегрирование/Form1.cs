using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Численное_интегрирование
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private double Taylor_Series(int type_of_function, double alpha, double x)
        {
            double S = 0;
            int fact = 1;
            if (type_of_function == 1)
            {
                double d = alpha;
                for (int i = 0; i < 10; i++)
                {
                    S += d;

                    d *= Math.Pow(x, 2);
                    d *= Math.Pow(alpha, 2.0);
                    d *= -1.0;

                    fact++;
                    d /= fact;
                    fact++;
                    d /= fact;
                }
            }
            if (type_of_function == 2)
            {
                double d = Math.Sqrt(x);
                for (int i = 0; i < 10; i++)
                {
                    S += d;

                    d *= Math.Pow(x, 2);
                    d *= -1.0;

                    fact++;
                    d /= fact;
                    fact++;
                    d /= fact;
                }
            }
            return S;
        }

        private double Function(double EPS, int type_of_function, double x, double alpha)
        {
            switch (type_of_function)
            {
                case 1:
                    if (Math.Abs(x) < 10*EPS)
                    {
                        return Taylor_Series(type_of_function, alpha, x);
                    }
                    else return Math.Sin(alpha * x) / x;
                case 2:
                    if (Math.Abs(x) < 10 * EPS)
                    {
                        return Taylor_Series(type_of_function, alpha, x);
                    }
                    else return Math.Sin(x) / Math.Sqrt(x);
                default: return 0; 
            }
        }

        private double Formula_of_the_Central_Rectangles(double EPS, int type_of_function, int n, double left, double right, double alpha)
        {
            double S = 0;
            double h = (right - left) / n;
            for (int i = 0; i <= n-1; i++)
            {
                S += Function(EPS, type_of_function, left + i*h + h/2,  alpha);
            }
            S *= h;
            return S;
        }

        private void Central_Rectangles_Method(double EPS, double alpha, int type_of_function, double left_bound, double right_bound)
        {
            int n = 1;
            int p = 1;
            while (true)
            {
                double S_2n = Formula_of_the_Central_Rectangles(EPS, 1, 2 * n, left_bound, right_bound, alpha);
                double S_n = Formula_of_the_Central_Rectangles(EPS, 1, n, left_bound, right_bound, alpha);

                if (Math.Abs(S_2n - S_n) / (Math.Pow(2, p) - 1) < EPS)
                {
                    n *= 2;
                    break;
                }
                n *= 2;
            }
            textBox9.Text = n.ToString();
            textBox2.Text = Formula_of_the_Central_Rectangles(EPS, 1, n, left_bound, right_bound, alpha).ToString();
            textBox5.Text = Math.Abs(Formula_of_the_Central_Rectangles(EPS, 1, n, left_bound, right_bound, alpha) - Formula_of_the_Central_Rectangles(EPS, 1, 2 * n, left_bound, right_bound, alpha)).ToString();
        }

        private double Formula_of_Trapezes(double EPS, int type_of_function, int n, double left, double right, double alpha)
        {
            double S = 0;
            double h = (right - left) / n;
            S += Function(EPS, type_of_function, left, alpha);
            S += Function(EPS, type_of_function, right, alpha);
            S /= 2;
            for (int i = 1; i <= n - 1; i++)
            {
                S += Function(EPS, type_of_function, left + i * h, alpha);
            }
            S *= h;
            return S;
        }

        private double Simpson_Formula(double EPS, int type_of_function, int n, double left, double right, double alpha)
        {
            double S = 0;
            double h = (right - left) / (2.0*n);
            S += Function(EPS, type_of_function, left, alpha);
            S += Function(EPS, type_of_function, right, alpha);

            double S1 = 0;
            for (int i = 1; i <= n; i++)
            {
                S1 += Function(EPS, type_of_function, left + (2*i-1) * h, alpha);
            }
            S1 *= 4.0;

            double S2 = 0;
            for (int i = 1; i <= n-1; i++)
            {
                S2 += Function(EPS, type_of_function, left + (2 * i) * h, alpha);
            }
            S2 *= 2.0;

            S += S1 + S2;

            S *= h/3.0;
            return S;
        }

        private void Method_of_Trapezes(double EPS, double alpha, int type_of_function, double left_bound, double right_bound)
        {
            int n = 1;
            int p = 1;
            while (true)
            {
                double S_2n = Formula_of_Trapezes(EPS, type_of_function, 2 * n, left_bound, right_bound, alpha);
                double S_n = Formula_of_Trapezes(EPS, type_of_function, n, left_bound, right_bound, alpha);
                
                if (Math.Abs(S_2n - S_n) / (Math.Pow(2, p) - 1) < EPS)
                {
                    n *= 2;
                    break;
                }
                n *= 2;
            }
            textBox10.Text = n.ToString();
            textBox3.Text = Formula_of_Trapezes(EPS, type_of_function, n, left_bound, right_bound, alpha).ToString();
        }

        private double Simpson_Rule(double EPS, double alpha, int type_of_function, double left_bound, double right_bound)
        {
            int n = 1;
            int p = 4;
            while (true)
            {
                double S_2n = Simpson_Formula(EPS, type_of_function, 2 * n, left_bound, right_bound, alpha);
                double S_n = Simpson_Formula(EPS, type_of_function, n, left_bound, right_bound, alpha);

                if (Math.Abs(S_2n - S_n) / (Math.Pow(2, p) - 1) < EPS)
                {
                    n *= 2;
                    break;
                }
                n *= 2;
            }
            textBox11.Text = n.ToString();
            double S = Simpson_Formula(EPS, type_of_function, n, left_bound, right_bound, alpha);
            textBox4.Text = S.ToString();

            if (type_of_function == 2)
            {
                S /= Math.Sqrt(2 * Math.PI);
                
            }
            
            return S;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            textBox6.Clear();
            textBox7.Clear();
            textBox9.Clear();
            textBox10.Clear();
            textBox11.Clear();

            double EPS = Convert.ToDouble(textBox1.Text);
            double alpha = Convert.ToDouble(textBox8.Text);
            int type_of_function = Convert.ToInt32(textBox12.Text);

            double left_bound = Convert.ToDouble(textBox13.Text);
            double right_bound = Convert.ToDouble(textBox14.Text);

            if (type_of_function == 2)
            {
                richTextBox1.Clear();
                
                int cnt_segments = Convert.ToInt32(textBox15.Text);
                double h = (right_bound - left_bound) / cnt_segments;
                double partical_sum = 0;
                for (int i = 1; i <= cnt_segments; i++)
                {
                    double cur_x = left_bound + h * i;
                    double prev = left_bound + h * (i - 1);
                    partical_sum += Simpson_Rule(EPS, alpha, type_of_function, prev, cur_x);

                    richTextBox1.Text += cur_x.ToString() + "\t" + partical_sum.ToString() + "\n";
                    textBox4.Text = partical_sum.ToString();
                }
            }
            else
            {
                Central_Rectangles_Method(EPS, alpha, type_of_function, left_bound, right_bound);
                Method_of_Trapezes(EPS, alpha, type_of_function, left_bound, right_bound);
                Simpson_Rule(EPS, alpha, type_of_function, left_bound, right_bound);
            } 
        }

        private void textBox5_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
