package main;

import model.Matriz;
import model.MatrizOperacoes;
import model.Simplex;

/**
 * Created by renan on 11/20/17.
 */
public class Main {

    public static void main(String[] args) {

        /**
         * SIMPLEX TEST REDMIKKS
         */

//        double[][] a = {{6, 4, 1, 0, 0, 0}, { 1, 2, 0, 1, 0, 0}, {-1, 1, 0, 0, 1, 0}, {0, 1, 0, 0, 0, 1}};
//        double[][] vb = {{24, 6, 1, 2}};
//        double[][] vc = {{-5, -4, 0, 0, 0, 0}};

//        Matriz A = new Matriz(a);
//        Matriz b = new Matriz(vb);
//        Matriz c = new Matriz(vc);

//        int[] indiceBase = {3, 4, 5, 6};
//        int[] indiceNaoBase = {1, 2};

//        int m = 4;
//        int n = 6;

//        Simplex simplex = new Simplex();
//        simplex.calcula(A, b, c, indiceBase, indiceNaoBase, m, n);

        /**
         * SIMPLEX TEST Motores - Lachthermarcher
         */


        double[][] a = {{1,  0,   0,  1,  0,   0, 0, 0}, { 0,  1,   0,  0,  1,   0, 0, 0}, {0,  0,   1,  0,  0,   1, 0, 0}, {1,  2, 0.5,  0,  0,   0, 1, 0}, {2.5,  1,   4,  0,  0,   0, 0, 1}};
        double[][] vb = {{3000, 2500, 500, 6000, 10000}};
        double[][] vc = {{50, 90, 120, 65, 92, 140, 0, 0, 0}};


        Matriz A = new Matriz(a);
        Matriz b = new Matriz(vb);
        Matriz c = new Matriz(vc);

        int[] indiceBase = {4,5,6,7,8};
        int[] indiceNaoBase = {1,2,3};

        int m = 5;
        int n = 8;

        Simplex simplex = new Simplex();
        simplex.calcula(A, b, c, indiceBase, indiceNaoBase, m, n);


    }
}
