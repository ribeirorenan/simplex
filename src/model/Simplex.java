package model;

/**
 * Created by renan on 12/1/17.
 */
public class Simplex {

    MatrizOperacoes matrizOperacoes = new MatrizOperacoes();
    double objetivo = 0;

    public Matriz calcula (Matriz A, Matriz b, Matriz c, int[] indiceBase, int[] indiceNaoBase, int m, int n){

        /**
         * Variáveis necessárias
         */
        double custoEscolhido;
        int iteracao = 0;

        System.out.println("Matriz A:");
        A.print();
        System.out.println();
        System.out.println("Vetor b:");
        b.print();
        System.out.println();
        System.out.println("Vetor c:");
        c.print();
        System.out.println();

        for (int treta = 0; treta < 20; treta++) {


            /**
             * Passo 1: Calculando SBF Inicial
             */
            //  # Exibe indices basicos e nao basicos da iteracao atual (apenas debug)
            System.out.println("Iteracao # " + iteracao);

            System.out.println("Indices Basicos: ");
            for (int i = 0; i < indiceBase.length; i++) {
                System.out.print(indiceBase[i] + " ");
            }
            System.out.println();

            System.out.println("Indices nao Basicos: ");
            for (int i = 0; i < indiceNaoBase.length; i++) {
                System.out.print(indiceNaoBase[i] + " ");
            }

            System.out.println();
            //Cria o espaço reservado para a matriz basica B de dimensao m x n
            double[][] rawB = new double[m][m];
            Matriz B = new Matriz(rawB);

            //Copia as colunas que formam a base inicial
            formaBaseInicial(B, A, indiceBase);
            System.out.println("Base:");
            B.print();

            //Calcula a SBF inicial pelo produta da inversa de B com b
            Matriz BMenosUm = this.matrizOperacoes.calculoInversa(B);
            Matriz x = matrizOperacoes.multiplicacaoMatriz(BMenosUm, matrizOperacoes.calculoTransposta(b));
//            x = matrizOperacoes.calculoTransposta(x);

            System.out.println("Base^-1:");
            BMenosUm.print();
            System.out.println();

            //Exibe Solução básica factível da interação corrente, apenas variáveis básicas
            System.out.println("SBF # " + iteracao);
            for (int i = 0; i < x.getLinha(); i++) {
                System.out.println("x[" + indiceBase[i] + "] = " + x.getMatriz()[i][0]);
            }
            System.out.println();

            //Exibe o valor da função objetivo
            for (int i = 0; i < m; i++) {
                objetivo = objetivo + c.getElemento(0, indiceBase[i] - 1) * x.getElemento(i, 0);
            }
            System.out.println("Objetivo: " + objetivo);
            System.out.println();

            /**
             * Passo 2: Calculando os custos reduzidos dos indices nao básicos
             */

            // #Para cada indice nao base, calcula o custo reduzido correspondente
            // #Vetor de custo basico, i.e., parte de c que está relacionado com as variáveis básicas atuais
            //Cria vetor linha custoBase 1xm
            double[][] custoBaseRaw = new double[m][1];
            for (int i = 0; i < m; i++) {
                custoBaseRaw[i][0] = c.getElemento(0, indiceBase[i] - 1);
            }
            Matriz custoBase = new Matriz(custoBaseRaw);
            System.out.println("Custo Basico:");
            for (int i = 0; i < x.getLinha(); i++) {
                System.out.println("c_B[" + indiceBase[i] + "] = " + custoBase.getMatriz()[i][0]);
            }

            // # Escolhe algum indice nao basico cujo custo reduzido e' negativo. Dentre os negativos, escolhe
            // # o 'mais negativo
            int jotaEscolhido = -1;
            custoEscolhido = Double.POSITIVE_INFINITY;

            for (int j : indiceNaoBase) {
                A.getVetorColuna(j-1).print();
                System.out.println();
                // # Calcula a j-esima direcao factivel pelo produto -B^{-1}A_j, apenas para debug
                Matriz direcao = matrizOperacoes.multiplicacaoMatriz(matrizOperacoes.multiplicacaoEscalar(-1., BMenosUm), A.getVetorColuna(j - 1));

                // # Calcula o custo reduzido
                Matriz matrizMultA = matrizOperacoes.calculoTransposta(matrizOperacoes.multiplicacaoMatriz(BMenosUm, A.getVetorColuna(j - 1)));
                Matriz custoMatriz = matrizOperacoes.multiplicacaoMatriz(matrizMultA, custoBase);
                double custo = c.getElemento(0, j - 1) - custoMatriz.getElemento(0, 0);


                // # Guarda um indice de direcao basica factivel com custo reduzido negativo, se houver
                if(custo < 0){
                    // # Verifica se a j-ésima direcao básica é a que contem o custo reduzido 'mais negativo'
                    if (custo < custoEscolhido) {
                        // # Atualiza a candidata a entrar na base
                        jotaEscolhido = j;
                        custoEscolhido = custo;
                    }
                }

                // # Exibe a j-ésima direcao factivel (apenas para debug)
                System.out.println("Direcao Factivel " + j + ", Custo Reduzido = " + custo);
                for (int i = 0; i < m; i++) {
                    System.out.println("\td_B[" + indiceBase[i] + "] = " + direcao.getElemento(i, 0));
                }
            }


            //# Se nao encontrou nenhum indice com custo reduzido negativo, e' porque chegamos no otimo
            if (jotaEscolhido == -1) {
                double valorObjetivo = 0;
                for (int i = 0; i < m; i++) {
                    valorObjetivo = valorObjetivo + custoBase.getElemento(i, 0) * x.getElemento(i, 0);
                }

                System.out.println("Objetivo = " + valorObjetivo + " (encontrado na iteracao " + iteracao + ")");

                double[][] solucaoRaw = new double[1][n];

                for (int i = 0; i < m; i++) {
                    solucaoRaw[0][indiceBase[i]-1] = x.getElemento(i, 0);
                }

                Matriz solucao = new Matriz(solucaoRaw);
                System.out.println("Solução:");
                solucao.print();
                return x;
            }

            // # Exibe quem entra na base
            System.out.println();
            System.out.println("Variavel Entra na Base: x[" + jotaEscolhido + "]");

            /**
             * Passo 3: Computar vetor u
             */

            // # Não chegamos em uma solucao ótima ainda. Alguma variável básica deve sair da base para dar
            // # lugar a entrada de uma variável não básica. Computa 'u' para verificar se solucao é ilimitada

            Matriz u = matrizOperacoes.multiplicacaoMatriz(BMenosUm, A.getVetorColuna(jotaEscolhido - 1));

            // 	# Verifica se nenhum componente de u e' positivo
            boolean existePositivo = false;
            for (int i = 0; i < m; i++) {
                if (u.getElemento(i, 0) > 0) {
                    existePositivo = true;
                }
            }

            // # Testa. Se não houver no vetor 'u' (sinal inverso da direcao factivel) nenhum componente
            // # positivo, é porque o valor ótimo é - infinito.
            if (!existePositivo) {
                System.out.println("Custo Infinito");
                return null;
            }

            /**
             * PASSO 4 Determina o valor de Theta
             */
            // # Chuta um valor alto para o theta, e vai reduzindo de acordo com a razao x_i / u_i
            double theta = Double.POSITIVE_INFINITY;
            int indiceL = -1;


            // # Varre indices basicos determinando o valor de theta que garante factibilidade
            for (int i = 0; i < m; i++) {
                // # calcula razao
                if (u.getElemento(i, 0) > 0) {
                    // # Calcula a razao
                    double razao = x.getElemento(i, 0) / u.getElemento(i, 0);

                    // # Atualiza a razao, pois encontramos um menor valor de theta
                    if (razao < theta) {
                        theta = razao;
                        indiceL = indiceBase[i];
                    }
                }
            }
            // # Exibe variavel que irá deixar a base (apenas debug)
            System.out.println("Variavel sai Base: x[" + indiceL + "], Theta = " + theta);

            /**
             * PASSO 5: Passo 5: Atualiza variável básica e não-básica
             */

            // # Calcula novo valor da nao-basica, e atualiza base

            for (int i = 0; i < m; i++) {
                // # Se encontramos o L-ésimo indica da variável básica que deixará a base, substitui-a
                // # pela variável não-básica correspondente à j-ésima direção factível de redução de custo
                if (indiceBase[i] == indiceL) {
                    x.setElemento(i, 0, theta);
                    indiceBase[i] = jotaEscolhido;
                }
            }


            // # Para as demais variáveis não básicas, apenas atualiza o índice de quem saiu da base (e
            // # entrou no conjunto das não-básicas
            int inc = n - m;
            for (int i = 0; i < inc; i++) {
                if (indiceNaoBase[i] == jotaEscolhido) {
                    indiceNaoBase[i] = indiceL;
                }
            }

            iteracao++;

            System.out.println();

            objetivo = 0;
        }

        return null;

    }




    void formaBaseInicial(Matriz B, Matriz A, int[] indiceBase){
        for (int j = 0; j < indiceBase.length; j++) {
            B.setVetorColuna(j, A.getVetorColuna(indiceBase[j] - 1));
        }
    }

}
