package project1;

import java.util.*;
import java.lang.*;

// command-line execution example) java project1.MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
//
// In eclipse, set the argument value and file input by using the menu [Run]->[Run Configurations]->{[Arguments], [Common->Input File]}.

// Original JAVA source code: http://stackoverflow.com/questions/21547462/how-to-multiply-2-dimensional-arrays-matrix-multiplication
public class MatmultD
{
    private static Scanner sc = new Scanner(System.in);
    public static void main(String [] args)
    {
//        int thread_no=0;
//        if (args.length==1) thread_no = Integer.valueOf(args[0]);
//        else thread_no = 1;

        int a[][]=readMatrix();
        int b[][]=readMatrix();

        System.out.printf("a: %d x %d \n", a.length, a[0].length);
        System.out.printf("b: %d x %d \n", b.length, b[0].length);

        int trial = 1;

        int[] nths = new int[]{1, 2, 4, 6, 8, 10, 12, 14, 16, 32};
        for(int thread_no : nths){
            long timediff = 0;
            for(int i = 0; i < trial; i++){
                long startTime = System.currentTimeMillis();
                int[][] c = multMatrixUsingThread(a, b, thread_no);
                long endTime = System.currentTimeMillis();
                timediff += endTime - startTime;

                //printMatrix(a);
                //printMatrix(b);
                if(thread_no == 1 && i == 0)
                   printMatrix(c);
//        System.out.printf("thread_no: %d\n" , thread_no);
//        System.out.printf("Calculation Time: %d ms\n" , endTime-startTime);
            }
            System.out.printf("[thread_no]:%2d , [Time]:%4d ms\n", thread_no, timediff/trial);
        }

    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%4d " , mat[i][j]);
                sum+=mat[i][j];
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }
    public static int[][] multMatrixUsingThread(int a[][], int b[][], int nths){//a[m][n], b[n][p]
        if(a.length == 0) return new int[0][0];
        if(a[0].length != b.length) return null; //invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        int[][] ans = new int[m][p];

        multMatThread[] threads = new multMatThread[nths];
        for(int i = 0; i < nths; i++){
            threads[i] = new multMatThread(a, b, i, nths);
            threads[i].start();
        }

        for(int i = 0; i < nths; i++){
            try {
                threads[i].join();
//                System.out.println("" + nths + "_" + i + "\t" + threads[i].timeDiff());
                for(int j = i;j < m*p;j += nths){
                    ans[j/p][j%p] += threads[i].ans[j/p][j%p];
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return ans;
    }
}
class multMatThread extends Thread{
    private final int[][] left;
    private final int[][] right;
    private int id = 0;
    private final int d;
    private final int n, m, p;
    final int[][] ans;

    private long start, end;

    multMatThread(int[][] a, int[][] b, int _id, int nth){
        left = a;
        right = b;
        n = a[0].length;
        m = a.length;
        p = b[0].length;
        id = _id;
        d = nth;
        ans = new int[m][p];
    }

    @Override
    public void run() {
        super.run();
        start = System.currentTimeMillis();
        for(int i = id;i < m*p;i+=d){
            for(int k = 0;k < n;k++){
                ans[i/p][i%p] += left[i/p][k] * right[k][i%p];
            }
        }
        end = System.currentTimeMillis();
    }
    public long timeDiff(){
        return end - start;
    }
}