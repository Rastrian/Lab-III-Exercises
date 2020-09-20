package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockSet {

    public RandomAccessFile[] arquivosLeitura;
    public RandomAccessFile[] arquivosEscrita;
    
    public int qtdArquivos;
    
    public boolean fontesAlternadas = false; // em caso de true, a escrita se torna leitura, e leitura se torna escrita

    public BlockSet(int qtdArquivos){
        this.qtdArquivos = qtdArquivos;
        arquivosEscrita = new RandomAccessFile[qtdArquivos];
        arquivosLeitura = new RandomAccessFile[qtdArquivos];
    }

    public void inicializarArquivosBlocos() {
        try{
            for(int i =0; i <qtdArquivos; ++i){                                           //inicializar os arquivos temporários
                arquivosEscrita[i] = new RandomAccessFile(new File("./bin/output_"+i+".tmp"), "rw");
                arquivosEscrita[i].setLength(0);    //lembrando de apagar dados/lixo anterior
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }

    public void abrirArquivos() {
        try{
            for(int i =0; i <qtdArquivos; ++i){                                           //inicializar os arquivos temporários

                arquivosLeitura[i] = new RandomAccessFile(new File("./bin/output_"+(i+qtdArquivos)+".tmp"), "rw");
                if(fontesAlternadas) arquivosLeitura[i].setLength(0); //Apago lixo de memoria

                //Se eu tenho 3 arquivos de leitura de nome <<arquivo(i).tmp>> [0,1,2], 
                //tenho 3 de saida <<arquivo(i+QTDARQUIVOS).tmp>> [3,4,5]
                //exemplo: i=0, qtdArquivos =3, arquivo de saida = ./bin/output_3 (i+QTDArquivos = 3)
                
                arquivosEscrita[i] = new RandomAccessFile(new File("./bin/output_"+i+".tmp"),"rw");
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }

    /***
     * Metodo que abre o arquivo de escrita passado como parametro
     * @throws FileNotFoundException
     */
    public void abrirArquivoEscrita(int arq) {
        try{
            if(fontesAlternadas){
                arquivosLeitura[arq] = new RandomAccessFile("./bin/output_"+(arq+qtdArquivos)+".tmp", "rw");

            }
            else{
                arquivosEscrita[arq] = new RandomAccessFile("./bin/output_"+arq+".tmp", "rw");
            }
        }catch (FileNotFoundException E){
            E.printStackTrace();
        }
    }


    /***
     * Metodo que alterna as fontes, quem é escrita vira leitura, e quem é leitura vira escrita
     */
    public void alternarFontes(){

        if (fontesAlternadas == true)
            fontesAlternadas = false;
        else
            fontesAlternadas = true;
    }

    /***
     * Metodo que retorna se a leitura de TODOS arquivos de leitura chegou ao fim
     * @return True se chegou ao fim, False caso contrario
     * @throws IOException Excesão de entrada e saida
     */
    public boolean fimDeLeitura() throws IOException {
        // se as fontes foram alternadas, os meus arquivos de escrita serão de leitura
        if (fontesAlternadas) {
            for (int i = 0; i < qtdArquivos; i++) {
                if (arquivosEscrita[i].getFilePointer() < arquivosEscrita[i].length()) {
                    return false;
                }
            }
            return true;
            // se não os de leitura, são realmente de leitura
        } else {
            for (int i = 0; i < qtdArquivos; i++) {
                if (arquivosLeitura[i].getFilePointer() < arquivosLeitura[i].length()) {
                    return false;
                }
            }
            return true;

        }
    }

    /**
     * Metodo que retorna os arquivos de leitura baseado no flag se as fotnes foram alternadas
     * @return
     */ 
    public RandomAccessFile[] getArquivoLeitura() {
        if (fontesAlternadas) {
            return arquivosEscrita;
        } else {
            return arquivosLeitura;
        }
    }

    /**
     * Metodo que retorna o vetor de arquivos de escrita, baseado se as fontes estão alternadas
     * @return
     */ 
    public RandomAccessFile[] getArquivoEscrita() {
        if (fontesAlternadas) {
            return arquivosLeitura;
        } else {
            return arquivosEscrita;
        }
    }

    /***
     * Metodo que fecha o arquivo de escrita especificicado, 
     * Caso tenha acabado de escrever no arquivo [X], fecha esse arquivo
     * @param arq posição no vetor de arquivos, do arquivo a ser fechado
     * @throws IOException
     */
    public void fecharArquivoEscrita(int arq){
        try{
            if (fontesAlternadas) {
                arquivosLeitura[arq].close();
            } else {
                arquivosEscrita[arq].close();
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }


    /**
     * Metodo que fecha todos os arquivos de escrita
     * @throws IOException
     */
    public void fecharTodosArquivosEscrita(){
        try{
            if(fontesAlternadas){
                for(int i=0; i< qtdArquivos;i++){
                    arquivosLeitura[i].close();
                }
            }else{
                for(int i=0; i< qtdArquivos;i++){
                    arquivosEscrita[i].close();
                }
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }

        /**
     * Metodo que fecha todos os arquivos de escrita
     * @throws IOException
     */
    public void fecharTodosArquivosDeLeitura() throws IOException {
        try{
            if(fontesAlternadas){
                for(int i=0; i< qtdArquivos;i++){
                    arquivosEscrita[i].close();
                }
            }else{
                for(int i=0; i< qtdArquivos;i++){
                    arquivosLeitura[i].close();
                }
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }


    /**
     * Metodo que apaga o conteudo do arquivo dos arquivos de leitura
     * @throws IOException
     */
    public void apagarArqLeitura(){
        try{
            if (fontesAlternadas) {
                for (int i = 0; i < qtdArquivos; i++) {
                    arquivosEscrita[i].setLength(0);
                }
            } else {
                for (int i = 0; i < qtdArquivos; i++) {
                    arquivosLeitura[i].setLength(0);
                }
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }


}
