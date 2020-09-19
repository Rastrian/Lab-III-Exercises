package app;

/*
 * MIT License
 *
 * Copyright(c) 2020 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class App {

    static final int TAMBLOCO = 1000; // tamanho do bloco em memória principal
    static final int QTARQ = 3; // quantidade de fontes de dados (devem ser usadas para leitura e para escrita)
    static final String NOMEARQ = "pessoas.dat";

    /*
     * // FORMATO DO ARQUIVO: PESSOAS // CABEÇALHO: INT COM Nº DE REGISTROS // RG:
     * INT // NOME: STRING UTF // NASCIMENTO: STRING UTF
     *
     */

    /*
     *
     * Método para receber um bloco de dados, ordená-lo, gravá-lo sequencialmente em
     * um arquivo. Ao final, inclui um registro "separador" de blocos
     */
    public static void gravarBloco(Pessoa[] bloco, RandomAccessFile arq) throws IOException {
        Arrays.sort(bloco); // ordenar o bloco. polimorfismo universal paramétrico. (genérico)

        for (Pessoa p : bloco) { // escrever alternadamente nos arquivos temporários
            p.saveToFile(arq);
        }

        Pessoa.separador().saveToFile(arq);
    }

    public static void gerarBlocosFixos() throws IOException {
        Pessoa[] blocoPessoas = new Pessoa[TAMBLOCO];   //array que comportará os dados parciais para ordenação em memória
        int posAtual = 0;   //marca a posição atual da leitura do arquivo inteiro
        int arqAtual = 0;   //marca qual arquivo temporário está sendo usado para gravação do bloco ordenado
        int qtdBlocos = 0;

        RandomAccessFile arqDados = new RandomAccessFile(new File(NOMEARQ), "r");    //arquivo com todos os dados
        RandomAccessFile[] arqTemp = new RandomAccessFile[QTARQ];                          //array com arquivos temporários que conterão os blocos
        
        for(int i =0; i <QTARQ; ++i){                                           //inicializar os arquivos temporários
            arqTemp[i] = new RandomAccessFile(new File(i+"temp.tmp"), "rw");
            arqTemp[i].setLength(0);    //lembrando de apagar dados/lixo anterior
        }

        int qtdPessoas = arqDados.readInt();    //cabeçalho do arquivo de dados contém o número total de registros

        for(int i=0; i< qtdPessoas; i++){                   //para cada registro...
            Pessoa aux = Pessoa.readFromFile(arqDados);     //leitura embutida na classe de dados 
            blocoPessoas[posAtual] = aux;                   //jogamos para a mem principal
            posAtual++;
            if(posAtual == TAMBLOCO){                       //caso haja enchido o bloco, fazer a ordenação e gravação

                    // acabamos de formar um bloco. O que eu faço agora?
                    gravarBloco(blocoPessoas, arqTemp[arqAtual]);    //gravar os dados
                    qtdBlocos++;
                    posAtual=0;                                     //limpeza para o próximo bloco
                    Arrays.fill(blocoPessoas, Pessoa.separador());  //limpamos os dados com o "separador"
                    
                    arqAtual++; 
                    arqAtual = (arqAtual % QTARQ);  //passar para o próximo arquivo.
                                        
                    //
                    //  ...........X...........X...........X...........
                    //  ...........X...........X...........X.....XXXXXX
                    //  ...........X...........X...........XXXXXXXXXXXX
                    //
            }   
        }
        System.out.println("QTD = "+qtdBlocos);
        arqDados.close();                   //lembrem-se de fechar os arquivos dados!!!!
        for(int i =0; i <QTARQ; ++i){
            arqTemp[i].close();
        }
    }

    /**
     * Gerador de blocos de tamanho variável usando uma fila de prioridades
     * @throws IOException para casos de arquivo não encontrado, erro de leitura etc
     */
    public static void gerarBlocosVariaveis() throws IOException {
        
        //utilizaremos uma fila de prioridades para retornar o menor elemento a cada passo
        PriorityQueue<ElementoHeap> tempHeap = new PriorityQueue<ElementoHeap>(TAMBLOCO);
        
        RandomAccessFile arqDados = new RandomAccessFile(new File(NOMEARQ), "r");    //arquivo com todos os dados
        int totalPessoas = arqDados.readInt();  //total de pessoas no arquivo original

        RandomAccessFile[] arqTemp = new RandomAccessFile[QTARQ];                          //array com arquivos temporários que conterão os blocos
        int arqAtual = 0;

        for(int i=0; i<QTARQ; ++i){                                           //inicializar os arquivos temporários
            arqTemp[i] = new RandomAccessFile(new File(i+"temp.tmp"), "rw");
            arqTemp[i].setLength(0);    //lembrando de apagar dados/lixo anterior
        }

        for(int i=0; i<TAMBLOCO; i++){                      //enchemos a heap até a capacidade determinada de memória
            Pessoa aux = Pessoa.readFromFile(arqDados);
            ElementoHeap novo = new ElementoHeap(aux, false);
            tempHeap.add(novo);
        }
        
        for(int i=TAMBLOCO; i<totalPessoas; i++){   //agora, até atingir o total de pessoas no arquivo original
            ElementoHeap aux = tempHeap.poll();     //retirar o menor (mais prioritário) da heap
            Pessoa menor = aux.dadoHeap;
            ElementoHeap novo;

            if(aux.flag){           //menor está marcado: acabou o bloco anterior
                Pessoa.separador().saveToFile(arqTemp[arqAtual]);   //gravamos o separador
                arqAtual++;                                         //avançamos para o próximo arquivo
                arqAtual = arqAtual%QTARQ;
                for (ElementoHeap elemento : tempHeap) {            //limpar as marcas/flags da heap
                    elemento.flag = false;
                }

            }
            menor.saveToFile(arqTemp[arqAtual]);    //salvamos o dado no arquivo para intercalação

            Pessoa novaPessoa = Pessoa.readFromFile(arqDados);  
            if(novaPessoa.compareTo(menor)==-1)                 //após ler o próximo, decidir se pode fazer parte do arquivo atual
                novo = new ElementoHeap(novaPessoa, true);
            else
                novo = new ElementoHeap(novaPessoa, false);

            tempHeap.add(novo);
        }

        for(int i=0; i<QTARQ; ++i){             
            arqTemp[i].close();
        }
        arqDados.close();

    }

    /**
     * Método para testar se um bloco está parcialmente ordenado. Lê o arquivo temporário indicado e mostra até achar um separador.
     */
    public  static void testeLeituraBlocos(String nomeArq) throws IOException {
        Scanner leitor = new Scanner(System.in);
        RandomAccessFile temp = new RandomAccessFile(new File(nomeArq), "r");
        Pessoa aux = Pessoa.readFromFile(temp);
        boolean fimDeArq = false;
        while (!fimDeArq) {
            if (aux.compareTo(Pessoa.separador()) != 0)
                System.out.println(aux);
            else
                leitor.nextLine();
            aux = Pessoa.readFromFile(temp);
        }
    }

    public static void main(String[] args) throws Exception {
        /*System.out.println("FIXO");
        gerarBlocosFixos();
        System.out.println("------");
        System.out.println("VAR");
        gerarBlocosVariaveis();   */
    }


    
}