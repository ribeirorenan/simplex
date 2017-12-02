package model;

/**
 * Created by renan on 11/20/17.
 */
public class Matriz {

    private double[][] matriz;
    private int linha;
    private int coluna;

    public Matriz(double[][] matriz) {
        this.matriz = matriz;
        linha = matriz.length;
        coluna = matriz[0].length;
    }

    public void print(){
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                System.out.printf("%.4f ", matriz[i][j]);
//                System.out.print(matriz[i][j]+ " ");
            }
            System.out.println();
        }
    }

    public Matriz getVetorLinha(int linha){

        double[][] vetorLinha = new double[1][this.coluna];

        for (int i = 0; i < this.coluna; i++) {
            vetorLinha[0][i] = matriz[linha][i];
        }
        return new Matriz(vetorLinha);
    }

    public double[] getVetorLinhaRaw(int linha){

        double[] vetorLinha = new double[this.coluna];

        for (int i = 0; i < this.coluna; i++) {
            vetorLinha[i] = matriz[linha][i];
        }
        return vetorLinha;
    }

    public Matriz getVetorColuna(int coluna){

        double[][] vetorColuna = new double[this.linha][1];

        for (int i = 0; i < this.linha; i++) {
            vetorColuna[i][0] = matriz[i][coluna];
        }
        return new Matriz(vetorColuna);
    }

    public double[][] getMatriz() {
        return this.matriz;
    }

    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public void setElemento(int linha, int coluna, double elemento){
        if((linha <= this.linha) && (coluna <= this.coluna)){
            matriz[linha][coluna] = elemento;
        }
    }

    public double getElemento(int linha, int coluna){
        return matriz[linha][coluna];
    }

    public double getElementoVLinha(int coluna){
        return matriz[0][coluna];
    }

    public int[] castToIntVLinha(){
        Double[] vetorLinhaDouble = new Double[this.coluna];
        int[] vetorLinha = new int[this.coluna];

        //Cast to Double
        for (int i = 0; i < this.coluna; i++) {
            vetorLinhaDouble[i] = matriz[0][i];
        }

        //Casto to Int
        if(vetorLinhaDouble == null){
            System.err.println("Error: castToIntVLinha - vetorLinhaDouble = NULL");
            return null;
        }
        for (int i = 0; i < this.coluna; i++) {
            vetorLinha[i] = vetorLinhaDouble[i].intValue();
        }

        return vetorLinha;
    }

    public void setVetorColuna(int j, Matriz coluna){
        for (int i = 0; i < coluna.getLinha(); i++) {
            this.matriz[i][j] = coluna.getElemento(i, 0);
        }
    }


}
