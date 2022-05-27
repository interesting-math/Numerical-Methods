using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Collections;

namespace Interpolation_Lab2
{
    public partial class Form1 : Form
    {
        static String[] methods_of_interpolation = {"none", "LaGrange", "Newton"};
        
        const double INF = 1E9;
        const int MAXN = (int) 1E3;
        List<double> list_x = new List<double>();
        List<double> list_y = new List<double>();
        
        bool is_load_data = false;                                            // флаг: указан ли адрес файла, из которого необходимо считывать координаты

        int Up_Screen_Shift = 2;                                             // еще понадобится

        String method_of_interpolation = methods_of_interpolation[0];

        double kx = 0, ky = 0;
        double dx = 0, dy = 0;

        double min_x = INF;
        double max_x = -INF;
        double min_y = INF;
        double max_y = -INF;

        double [,]divided_differences = new double[MAXN, MAXN];
        public Form1()
        {
            InitializeComponent();  
        }

        private void Form1_Load(object sender, EventArgs e)
        {
        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {   
        }
        private void button5_Click(object sender, EventArgs e)
        {
        }
        private void openFileDialog1_FileOk(object sender, CancelEventArgs e)
        {
            textBox1.Text = openFileDialog1.FileName;
        }
        private void button3_Click(object sender, EventArgs e)
        {
        }

        private void button3_Click_1(object sender, EventArgs e)
        {
            openFileDialog1.ShowDialog();
            is_load_data = true;
        }

        private void button5_Click_1(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Graphics g = Graphics.FromHwnd(pictureBox1.Handle);
            g.Clear(Color.Black);
            list_x.Clear();
            list_y.Clear();

            is_load_data = false;
        }

        private void button4_Click(object sender, EventArgs e)
        {     
        }

        private void Clear_Lists()
        {
            list_x.Clear();
            list_y.Clear();           
        }

        private void Parsing() {
            System.IO.StreamReader file = new System.IO.StreamReader(@textBox1.Text);   // Открываем файл для чтения координат
            
            int N = Int32.Parse(file.ReadLine());  
            for (int i = 0; i < N; i++)
            {
                double X = 0, Y = 0;

                String str = file.ReadLine();
                str += ' ';
                int j = 0; ;
                bool is_negative = false;
                if (str[j] == '-')
                {
                    is_negative = true;
                    j++;
                }
                for (; j < str.Length; j++)
                {
                    if (str[j] != ' ' && str[j] != '.')
                    {
                        X *= 10;
                        X += (int)(str[j] - '0');
                    }
                    else break;
                }
                if (str[j] == '.')
                {
                    double tmp = 1;
                    for (j++; j < str.Length; j++)
                    {
                        if (str[j] != ' ')
                        {
                            tmp /= 10.0;
                            X += tmp * (int)(str[j] - '0');
                        }
                        else break;
                    }
                }
                j++;
                if (is_negative == true) X *= -1;
                is_negative = false;
                if (str[j] == '-')
                {
                    is_negative = true;
                    j++;
                }
                for (; j < str.Length; j++)
                {
                    if (str[j] != ' ' && str[j] != '.')
                    {
                        Y *= 10;
                        Y += (int)(str[j] - '0');
                    }
                    else break;
                }
                if (str[j] == '.')
                {
                    double tmp = 1;
                    for (j++; j < str.Length; j++)
                    {
                        if (str[j] != ' ')
                        {
                            tmp /= 10.0;
                            Y += tmp * (int)(str[j] - '0');
                        }
                        else break;
                    }
                }
                if (is_negative == true) Y *= -1;
                list_x.Add(X); list_y.Add(Y);
            }
        }

        private void Find_Min_Max_X()
        {
            for (int i = 0; i < list_x.Count; i++)                          // поиск минимального X
            {
                min_x = Math.Min(min_x, list_x[i]);                         
            }
            for (int i = 0; i < list_x.Count; i++)                          // поиск максимального X
            {
                max_x = Math.Max(max_x, list_x[i]);
            }
            
            double size_of_extrapolation = (max_x - min_x) * 0.003;          // размер левой и размер правой экстраполяции равен 3% от общего расстояния между первым и последним узлом по X
            // расширяем область для интерполяции/экстраполяции
            min_x -= size_of_extrapolation;
            max_x += size_of_extrapolation;
            
            for (int i = 0; i < list_x.Count; i++)
            {
                list_x[i] -= min_x;
            }
        }

        private void Find_Min_Max_Y_LaGrange()
        {
            for (int k = 0; k < pictureBox1.Width; k++)
            {
                double cur_x = dx * k;
                double cur_y = 0;
                for (int i = 0; i < list_x.Count; i++)
                {
                    double phi = 1.0;
                    for (int j = 0; j < list_x.Count; j++)
                    {
                        if (i == j) continue;
                        phi *= cur_x - list_x[j];
                        phi /= list_x[i] - list_x[j];
                    }
                    cur_y += phi * list_y[i];
                }
                min_y = Math.Min(min_y, cur_y);
                max_y = Math.Max(max_y, cur_y);
            }
            ky = ((double)pictureBox1.Height-Up_Screen_Shift) / (max_y - min_y);
        }

        private void Draw_Graph_LaGrange()
        {
            Graphics g = Graphics.FromHwnd(pictureBox1.Handle);                         // Инициализация графики
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;         // Cглаживание графики
            Pen pen = new Pen(Color.Gold, 1);                                           // Инициализация pen

            int prev_x = 0, prev_y = 0;
            for (int k = 0; k < pictureBox1.Width; k++)
            {
                double cur_x = dx * k;
                double cur_y = 0;
                for (int i = 0; i < list_x.Count; i++)
                {
                    double phi = 1.0;
                    for (int j = 0; j < list_x.Count; j++)
                    {
                        if (i == j) continue;
                        phi *= cur_x - list_x[j];
                        phi /= list_x[i] - list_x[j];
                    }
                    cur_y += phi * list_y[i];
                }
                int screen_y = pictureBox1.Height - ((int) ((cur_y - min_y)*ky) + Up_Screen_Shift);
                if (k == 0) prev_y = screen_y;
                g.DrawLine(pen, prev_x, prev_y, k, screen_y);
                if (k != 0) prev_x = k - 1;
                prev_y = screen_y;
            }
        }

        private void Draw_Nodes()
        {
            Graphics g = Graphics.FromHwnd(pictureBox1.Handle);                         // Инициализация графики
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;         // Cглаживание графики                

            for (int i = 0; i < list_x.Count; i++)
            {
                int screen_x = (int)(list_x[i] * kx);
                int screen_y = pictureBox1.Height - ((int)((list_y[i] - min_y) * ky) + Up_Screen_Shift);
                
                Brush brush = new SolidBrush(Color.BlueViolet);
                g.FillEllipse(brush, screen_x-3, screen_y-3, 6, 6);
                brush = new SolidBrush(Color.White);
                g.FillEllipse(brush, screen_x - 2, screen_y - 2, 4, 4);
            }
        }

        private void Draw_Axes()
        {
            Graphics g = Graphics.FromHwnd(pictureBox1.Handle);                         // Инициализация графики
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;         // Cглаживание графики         
            Pen pen = new Pen(Color.White, 1);                                         // Инициализация pen
            g.Clear(Color.Black);                                                       // Очистка graphics g

            g.DrawRectangle(pen, 1, pictureBox1.Height - 30, pictureBox1.Width - 2, 1);
            g.DrawRectangle(pen, 60, 1, 1, pictureBox1.Height - 2);
            int step = 70;

            Brush Кисть = new SolidBrush(Color.White);
            g.TextRenderingHint = System.Drawing.Text.TextRenderingHint.AntiAlias;

            int count_of_digits = 1000;
            for (int i = 0; i < 16; i++)
            {
                int x = (int)(i * step);
                g.DrawLine(pen, x, pictureBox1.Height - 35, x, pictureBox1.Height - 25);

                double coordinate = ((double)((int)((min_x + dx * step * i) * count_of_digits))) / count_of_digits;
                String Text = String.Format("{0}", coordinate.ToString());
                g.DrawString(Text, Font, Кисть, x-15, pictureBox1.Height - 23); // Координаты размещения текста
            }
            for (int i = 0; i <= pictureBox1.Height/(step/2); i++)
            {
                int y = (int)(i * step/2);
                g.DrawLine(pen, 55, pictureBox1.Height - y-1, 65, pictureBox1.Height - y-1);            // -1 (поправка, чтобы было видно нижнюю палочку/начертание)

                double coordinate = ((double)((int)((min_y + dy * (step/2) * i) * count_of_digits))) / count_of_digits;
                String Text = String.Format("{0}", coordinate.ToString());
                int count_of_pixels_on_one_symbol = 6;
                int cnt_symbols = 0;
                if (coordinate.ToString().Length < 8)
                {
                    cnt_symbols = 8 - coordinate.ToString().Length;
                }
                int left_shift_of_text = count_of_pixels_on_one_symbol * cnt_symbols;
                g.DrawString(Text, Font, Кисть, 2 + left_shift_of_text, pictureBox1.Height-y-7);            // Координаты размещения текста
            } 
        }

        private void Initialization()
        {
            Clear_Lists();
            if (is_load_data && (method_of_interpolation == methods_of_interpolation[1] || method_of_interpolation == methods_of_interpolation[2])) Parsing();
            Initialization_Of_Min_Max_Values();
        }

        private void Draw_LaGrange()
        {
            Find_Min_Max_X();
            dx = (max_x - min_x)/((double)pictureBox1.Width);
            Find_Min_Max_Y_LaGrange();
            dy = (max_y - min_y) / ((double)pictureBox1.Height-Up_Screen_Shift);
            kx = ((double)pictureBox1.Width) / (max_x - min_x);
            Draw_Axes();
            Draw_Graph_LaGrange();
            Draw_Nodes();
        }

        public void button1_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[1];
            if (is_load_data)
            {
                Initialization();
                Draw_LaGrange();
            }
            else
            {
            }
        }

        private void Form1_DragDrop(object sender, DragEventArgs e)
        {
        }

        private void Form1_DragEnter(object sender, DragEventArgs e)
        {
        }

        private void Form1_DragLeave(object sender, EventArgs e)
        {
        }

        private void Form1_DragOver(object sender, DragEventArgs e)
        {
        }

        private void Form1_Resize(object sender, EventArgs e)
        {
        }

        private void Form1_Move(object sender, EventArgs e)
        {

            if (is_load_data)
            {
                Initialization();
                if (method_of_interpolation == methods_of_interpolation[1]) Draw_LaGrange();
                if (method_of_interpolation == methods_of_interpolation[2]) Draw_Newton();
            }
        }

        private void button3_Move(object sender, EventArgs e)
        {
        }

        private void button1_DragOver(object sender, DragEventArgs e)
        {
        }

        private void button1_Paint(object sender, PaintEventArgs e)
        {  
        }

        private void button1_Validated(object sender, EventArgs e)
        {
        }

        private void Initialization_Of_Min_Max_Values()
        {
            min_x = INF;
            max_x = -INF;
            min_y = INF;
            max_y = -INF;   
        }

        private void Find_Min_Max_Y_Newton()
        {
            for (int k = 0; k < pictureBox1.Width; k++)
            {
                double cur_x = dx * k;
                double cur_y = 0;
                double phi = 1.0;
                for (int i = 0; i < list_x.Count; i++)
                {
                    cur_y += phi * divided_differences[0, i];
                    if (i+1 != list_x.Count) phi *= cur_x - list_x[i];
                }
                min_y = Math.Min(min_y, cur_y);
                max_y = Math.Max(max_y, cur_y);
            }
            ky = ((double)pictureBox1.Height-Up_Screen_Shift) / (max_y - min_y);    
        }

        private void Draw_Graph_Newton()
        {
            Graphics g = Graphics.FromHwnd(pictureBox1.Handle);                         // Инициализация графики
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;         // Cглаживание графики
            Pen pen = new Pen(Color.OrangeRed, 1);                                      // Инициализация pen

            int prev_x = 0, prev_y = 0;
            for (int k = 0; k < pictureBox1.Width; k++)
            {
                double cur_x = dx * k;
                double cur_y = 0;
                double phi = 1.0;
                for (int i = 0; i < list_x.Count; i++)
                {
                    cur_y += phi * divided_differences[0, i];
                    if (i + 1 != list_x.Count) phi *= cur_x - list_x[i];
                }
                int screen_y = pictureBox1.Height - ((int)((cur_y - min_y) * ky) + Up_Screen_Shift);
                if (k == 0) prev_y = screen_y;
                g.DrawLine(pen, prev_x, prev_y, k, screen_y);
                if (k != 0) prev_x = k - 1;
                prev_y = screen_y;
            }
        }

        private void Draw_Newton() {
            for (int t = 0; t < list_x.Count; t++)
            {
                divided_differences[t, 0] = list_y[t];
                for (int i = t-1, j = 1; i >= 0; i--, j++)
                {
                    divided_differences[i, j] = (divided_differences[i+1, j-1]-divided_differences[i, j-1]) / (list_x[i+j]-list_x[i]);
                }
            }

            Find_Min_Max_X();
            dx = (max_x - min_x) / ((double)pictureBox1.Width);
            Find_Min_Max_Y_Newton();
            dy = (max_y - min_y) / ((double)pictureBox1.Height-Up_Screen_Shift);
            kx = ((double)pictureBox1.Width) / (max_x - min_x);
            Draw_Axes();
            Draw_Graph_Newton();
            Draw_Nodes();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[2];
            if (is_load_data)
            {
                Initialization();
                Draw_Newton();
            }
            else
            {

            }
        }

        private void label5_Click(object sender, EventArgs e)
        {
        }

        private void panel2_Paint(object sender, PaintEventArgs e)
        {
        }

        private void Initialization_of_5_points()
        {
            for (int i = 0; i < 5; i++)
            {
                list_x.Add(-1.0 + i * 2.0/5.0);
            }
            list_y.Add(0.1);
            list_y.Add(0.8);
            list_y.Add(0.5);
            list_y.Add(0.2);
            list_y.Add(0.9);
        }

        private void button6_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            Initialization_of_5_points();
            Draw_LaGrange();
        }

        private void button7_Click(object sender, EventArgs e)
        {
            button8.Visible = true;
            button9.Visible = true;
            button10.Visible = true;
        }

        private void label5_Click_1(object sender, EventArgs e)
        {

        }

        private void Initialization_of_n_points_for_LaGrange_Interpolation(int n, bool is_zeros_of_Chebyshev_polynomial)
        {
            double h = 2.0 / n;
            int N = 2*(n+1);
            if (is_zeros_of_Chebyshev_polynomial)
            {
                for (int i = 0; i <= n; i++)
                {
                    list_x.Add(Math.Cos((2 * i + 1) * Math.PI / N));
                    list_y.Add(1.0 / (1 + (10 + 2) * Math.Pow(list_x[i], 2)));
                }
            }
            else
            {
                for (int i = 0; i <= n; i++)
                {
                    list_x.Add(-1.0 + i * h);
                    list_y.Add(1.0 / (1 + (10 + 2) * Math.Pow(-1.0 + i * h, 2)));
                }
            }
            Draw_LaGrange();
        }

        private void Initialization_of_n_points_for_Newton_Interpolation(int n, bool is_zeros_of_Chebyshev_polynomial, string function)
        {
            double h = 2.0 / n;
            int N = 2 * (n + 1);
            is_zeros_of_Chebyshev_polynomial = true;
            for (int i = 0; i <= n; i++)
            {
                if (is_zeros_of_Chebyshev_polynomial) list_x.Add(Math.Cos((2 * i + 1) * Math.PI / N));
                else list_x.Add(-1.0 + i * h);
            }

            if (function == "abs")
            {
                for (int i = 0; i <= n; i++)
                {
                    list_y.Add(Math.Abs(list_x[i]));
                }
            }
            if (function == "cube")
            {
                for (int i = 0; i <= n; i++)
                {
                    list_y.Add(Math.Pow(list_x[i], 3));
                }
            }
            if (function == "cos")
            {
                for (int i = 0; i <= n; i++)
                {
                    list_y.Add(Math.Abs(Math.PI/2 * list_x[i]));
                }
            }
            Draw_Newton();
        }

        private void button8_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();

            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(4, false);
        }

        private void button9_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();

            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(10, false);
        }

        private void button10_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();

            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(20, false);
        }

        private void label6_Click(object sender, EventArgs e)
        {

        }

        private void button14_Click(object sender, EventArgs e)
        {
            button11.Visible = true;
            button12.Visible = true;
            button13.Visible = true;
        }

        private void button13_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();

            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(4, true);
        }

        private void button12_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(10, true);
        }

        private void button11_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_LaGrange_Interpolation(20, true);
        }

        private void button15_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            Initialization_of_5_points();
            Draw_Newton();
        }

        private void button18_Click(object sender, EventArgs e)
        {
            button16.Visible = true;
            button17.Visible = true;
        }

        private void button21_Click(object sender, EventArgs e)
        {
            button19.Visible = true;
            button20.Visible = true;
        }

        private void button24_Click(object sender, EventArgs e)
        {
            button22.Visible = true;
            button23.Visible = true;
        }

        private void button22_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(20, false, "cos");
        }

        private void button17_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(10, false, "abs");
        }

        private void button16_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(20, false, "abs");
        }

        private void button20_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(10, false, "cube");
        }

        private void button19_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(20, false, "cube");
        }

        private void button23_Click(object sender, EventArgs e)
        {
            method_of_interpolation = methods_of_interpolation[0];
            Initialization();
            
            button8.Visible = false;
            button9.Visible = false;
            button10.Visible = false;

            button11.Visible = false;
            button12.Visible = false;
            button13.Visible = false;

            button16.Visible = false;
            button17.Visible = false;

            button19.Visible = false;
            button20.Visible = false;

            button22.Visible = false;
            button23.Visible = false;

            Initialization_of_n_points_for_Newton_Interpolation(10, false, "cos");
        }
    }
}
