package model;

    /**
     * Created by renan on 11/22/17.
     */
    public  class MatrizOperacoes {


        /**
         * A Matriz passada é alterada por referência (Como todos os objetdos nesta porcaria)
         * Uma Matriz que é o vetor Pivot
         * @param n num Colunas
         * @param A matriz para LU
         * @return vetorPivot
         */
        public Matriz decomposicaoLU(int n, Matriz A){

            /*
             * Variáveis de utilização para decomposição LU
             */
            int p; //Indice da linha k do maior elemento |A[k,j]|, j+1 <= k <= n
            double t; //temporaria para swap das linhas de A
            double m; //temporaria para armazenar permutacoes de A no vetor pivot
            double multiplicador; //temporaria utilizada pra calculo de componentes de L e elementos de U

            //Inicialização do Vetor Pivot
            double[][] vPivot = new double[1][n];

            for (int i = 0; i < n; i++) {
                vPivot[0][i] = i;
            }

            Matriz vetorPivot = new Matriz(vPivot);

            //MAIN
            for (int j = 0; j < n-1; j++) {
                //escolha do pivot -> indice da linha k do maior elemento |A[k,j]|, j+1 <= k <= n
                p = j;
                for (int k = j + 1; k < n; k++) {
                    if(A.getElemento(k, j) > A.getElemento(p, j)){
                        p = k;
                    }
                }
                //Troca das linhas de A (p e j) pivotação Parcial
                if(p != j){
                    for (int k = 0; k < n; k++) {
                        t = A.getElemento(j,k);
                        A.setElemento(j, k, A.getElemento(p, k));
                        A.setElemento(p, k, t);
                    }
                    //Atualiza vetor Pivot de acordo com as permutações
                    m = vetorPivot.getElemento(0, j);
                    vetorPivot.setElemento(0, j, vetorPivot.getElemento(0, p));
                    vetorPivot.setElemento(0, p, m);
                }

                if(A.getElemento(j,j) != 0){
                    for (int i = j+1; i < n; i++) {
                        multiplicador = A.getElemento(i, j) / A.getElemento(j, j); //Pivoteamento por eliminação de Gauss
                        A.setElemento(i, j, multiplicador); //Elementos mij de L

                        //Coeficientes Uij de U
                        for (int k = j + 1; k < n; k++) {
                            A.setElemento(i, k, (A.getElemento(i, k) - (multiplicador * A.getElemento(j, k))));
                        }

                    }
                }

            }

            return vetorPivot;
        }

        public Matriz substituicoesSucessivasPivotal(int n, Matriz L, Matriz b, Matriz matVetorPivot){

            /*
             * Variáveis de utilização para substituicoesSucessivasPivotal
             */
            double soma;
            int k;
            int[] vetorPivot = matVetorPivot.castToIntVLinha();
            double[][] y = new double[1][n];

            k = vetorPivot[0]; //primeiro elemento vetor Pivot
            y[0][0] = b.getElementoVLinha(k);

            for (int i = 1; i < n; i++) {
                soma = 0;
                for (int j = 0; j < i; j++) {
                    soma = (soma + (L.getElemento(i,j) * y[0][j]));
                }
                k = vetorPivot[i];
                y[0][i] = (b.getElementoVLinha(k) - soma);
            }

            Matriz vetorY = new Matriz(y);
            return vetorY;
        }

        public Matriz substituicoesRetroativas(int n, Matriz U, Matriz y){

            /*
             * Variáveis de utilização para substituicoesSucessivasPivotal
             */
            double soma;
            double[][] x = new double[1][n];

            x[0][n-1] = y.getElementoVLinha(n-1)/U.getElemento(n-1,n-1);


            for (int i = (n-1); i > 0 - 1; i--) {
                soma = 0;
                for (int j = (i + 1); j < n; j++) {
                    soma = (soma + U.getElemento(i,j) * x[0][j]);
                }
                x[0][i] = (y.getElementoVLinha(i) - soma) / U.getElemento(i, i);
            }

            Matriz vetorX = new Matriz(x);
            return vetorX;
        }

        public Matriz calculoInversa(Matriz matrizA){

            //Clonando matrizA para não alterar objeto
            double[][] matrizRaw = new double[matrizA.getColuna()][matrizA.getLinha()];

            for (int i = 0; i < matrizA.getColuna(); i++) {
                for (int j = 0; j < matrizA.getLinha(); j++) {
                    matrizRaw[i][j] = matrizA.getMatriz()[i][j];
                }
            }

            Matriz matriz = new Matriz(matrizRaw);

            double[][] matrizIdentidade = new double[matriz.getLinha()][matriz.getColuna()];
            double[][] matrizInvertida = new double[matriz.getLinha()][matriz.getColuna()];

            //Criando Matriz Identidade
            for (int i = 0; i < matriz.getLinha(); i++) {
                for (int j = 0; j < matriz.getColuna(); j++) {
                    if(i != j){
                        matrizIdentidade[i][j] = 0;
                    }
                    else {
                        matrizIdentidade[i][j] = 1;
                    }
                }
            }

            Matriz identidade = new Matriz(matrizIdentidade);

            Matriz vetorPivot = this.decomposicaoLU(matriz.getColuna(), matriz);

            for (int i = 0; i < matriz.getLinha(); i++) {
                Matriz y = this.substituicoesSucessivasPivotal(matrizA.getLinha(), matriz, identidade.getVetorLinha(i), vetorPivot);
                matrizInvertida[i] = this.substituicoesRetroativas(matrizA.getLinha(), matriz, y).getVetorLinhaRaw(0);
            }

            Matriz inversa = new Matriz(matrizInvertida);
            inversa = calculoTransposta(inversa);

            return inversa;
        }

        public Matriz resolveSistema(Matriz matriz, Matriz b){

            double[][] newMatriz = new double[matriz.getLinha()][matriz.getColuna()];
            for (int i = 0; i < matriz.getLinha(); i++) {
                for (int j = 0; j < matriz.getColuna(); j++) {
                    newMatriz[i][j] = matriz.getMatriz()[i][j];
                }
            }

            double[][] c = new double[b.getLinha()][b.getColuna()];
            for (int i = 0; i < b.getLinha(); i++) {
                for (int j = 0; j < b.getColuna(); j++) {
                    c[i][j] = b.getMatriz()[i][j];
                }
            }

            Matriz A = new Matriz(newMatriz);
            Matriz vetorB = new Matriz(c);

            vetorB.print();

            Matriz vetorPivot = this.decomposicaoLU(A.getColuna(), A);

            Matriz y = this.substituicoesSucessivasPivotal(A.getColuna(), A, vetorB, vetorPivot);
            Matriz x = this.substituicoesRetroativas(A.getColuna(), A, y);

            return x;
        }

        public Matriz multiplicacaoEscalar(Double escalar, Matriz matriz){

            if(matriz == null){
                return null;
            }

            int linha = matriz.getLinha();
            int coluna = matriz.getColuna();

            double[][] matrizResultado = new double[linha][coluna];

            for (int i = 0; i < linha; i++) {
                for (int j = 0; j < coluna; j++) {
                    matrizResultado[i][j] = matriz.getElemento(i,j)*escalar;
                }
            }

            return new Matriz(matrizResultado);
        }

        public Matriz multiplicacaoMatriz(Matriz matrizA, Matriz matrizB){

            if((matrizA == null) || (matrizB == null)){
                return null;
            }

            //Verificar Compatibilidade
            if(matrizA.getColuna() != matrizB.getLinha()){
                return null;
            }

            //Cria nova matriz com as novas dimensões
            double[][] matrizResultado = new double[matrizA.getLinha()][matrizB.getColuna()];

            //variaveis de controle da matrix
            int linhaA = 0;
            int colunaB = 0;

            for (int colA = 0; colA < matrizA.getColuna(); colA++) {
                for (int linB = 0; linB < matrizB.getLinha(); linB++) {

                }
            }

            for (int qtdLinhasA = 0; qtdLinhasA < matrizA.getLinha(); qtdLinhasA++) {

                for (int qtdColunasB = 0; qtdColunasB < matrizB.getColuna(); qtdColunasB++) {
                    matrizResultado[linhaA][colunaB] = multiplicacaoVetor(matrizA.getVetorLinha(linhaA), matrizB.getVetorColuna(colunaB));
                    colunaB++;
                }
                linhaA++;
                colunaB = 0;
            }

            return new Matriz(matrizResultado);
        }

        private double multiplicacaoVetor(Matriz matrizA, Matriz matrizB){

            double resultado = 0;

            if(matrizA.getColuna() == matrizB.getLinha()){
                for (int i = 0; i < matrizA.getLinha(); i++) {
                    for (int j = 0; j < matrizA.getColuna(); j++) {
                        resultado += matrizA.getMatriz()[i][j] * matrizB.getMatriz()[j][i];
                    }
                }
            }
            else if(matrizA.getLinha() == matrizB.getLinha())
            {
                for (int i = 0; i < matrizA.getLinha(); i++) {
                    for (int j = 0; j < matrizA.getColuna(); j++) {
                        resultado += matrizA.getMatriz()[i][j] * matrizB.getMatriz()[i][j];
                    }
                }
            }else
            {
                System.err.println("multiplicacaoVetor: Dimensões inválidas");
                return 0;
            }

            return resultado;
        }

        public Matriz calculoTransposta(Matriz matriz){

            double[][] matrizResultado = new double[matriz.getColuna()][matriz.getLinha()];

            for (int j = 0; j < matriz.getColuna(); j++) {
                for (int i = 0; i < matriz.getLinha(); i++) {
                    matrizResultado[j][i] = matriz.getMatriz()[i][j];
                }
            }

            return new Matriz(matrizResultado);
        }

    }

