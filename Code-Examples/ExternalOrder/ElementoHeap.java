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


 /**
  * Classe que encapsula um elemendo de dado para uma fila de prioridades
  */
public class ElementoHeap implements Comparable<ElementoHeap>{

    public Pessoa dadoHeap;
    public boolean flag;

    /**
     * construtor.
     * @param dado o dado/pessoa a ser armazenado
     * @param proximoBloco  flag para dizer se a pessoa pode fazer parte deste bloco de intercalação
     */
    public ElementoHeap(Pessoa dado, boolean proximoBloco){
        this.dadoHeap = dado;
        this.flag = proximoBloco;
    }

    /**
     * Comparador padrão: quem está marcado, sempre perde. Caso contrário, a decisão é pelo comparador de Pessoa
     */
    @Override
    public int compareTo(ElementoHeap outro) {
        
        if(this.flag && !outro.flag) return 1;
        else if(!this.flag && outro.flag) return -1;
        else return dadoHeap.compareTo(outro.dadoHeap);
    }



    
}