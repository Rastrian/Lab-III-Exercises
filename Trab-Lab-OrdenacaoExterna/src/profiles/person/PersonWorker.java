package profiles.person;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.io.RandomAccessFile;

import utils.BlockSet;

public class PersonWorker implements Runnable {
    private static final int BlOCKSIZE = 1000;
    private static final int DATACOUNT = 3;
    private static final String FILENAME = "Person.bin";
    private static BlockSet blockset;
    private static long AverageTime = 0;
    private static int countBlocks = 0;
    private static int countRepeats = 0;
    private static int countPerson = 0;
    private static long timeStart;
    private static long[] timeEnd;

    private static void saveBlock(Person[] block, RandomAccessFile data) throws IOException {
        Arrays.sort(block);

        for (Person p: block) {
            if (!p.checkSeparator()) {
                p.saveToFile(data);
            }
        }
        Person.clean().saveToFile(data);
    }

    private static void generateBlocks() throws IOException {
        Person[] PersonBlock = new Person[BlOCKSIZE];
        int pos = 0;
        int orderFile = 0;

        countBlocks = 0;
        countRepeats = 0;

        RandomAccessFile data = new RandomAccessFile(new File(FILENAME), "r");

        blockset = new BlockSet(DATACOUNT);
        blockset.inicializarArquivosBlocos();

        countPerson = data.readInt();

        for (int i = 0; i < countPerson; i++) {
            Person aux = Person.readFromFile(data);
            PersonBlock[pos] = aux;
            pos++;

            if (pos == BlOCKSIZE) {
                saveBlock(PersonBlock, blockset.getArquivoEscrita()[orderFile]);
                countBlocks++;
                pos = 0;
                Arrays.fill(PersonBlock, Person.clean());
                orderFile++;
                orderFile = (orderFile % DATACOUNT);
            }
        }

        if (!PersonBlock[0].checkSeparator()) {
            saveBlock(PersonBlock, blockset.getArquivoEscrita()[orderFile]);
            countBlocks++;
        }
        countRepeats++;

        blockset.fecharTodosArquivosEscrita();
        data.close();
    }

    private static void interlayerBlocks() throws IOException {
        blockset.alternarFontes();
        blockset.abrirArquivos();
        ExtractorPerson extractor = new ExtractorPerson();
        int generatedBlock = 0;
        int marketempFile = 0;
        int orderFile = 0;

        while (generatedBlock < countPerson) {
            if (blockset.fimDeLeitura()) {
                blockset.apagarArqLeitura();
                blockset.alternarFontes();
                blockset.abrirArquivos();
                orderFile = 0;
            } else {
                blockset.abrirArquivoEscrita(orderFile);
            }
            generatedBlock = 0;
            extractor.extrairDeTodos(blockset.getArquivoLeitura());
            while (extractor.existeFonteValida()) {
                countRepeats++;
                Map.Entry < Person, Integer > menor = extractor.retornaMenor();
                marketempFile = menor.getValue();
                menor.getKey().saveToFile(blockset.getArquivoEscrita()[orderFile]);
                generatedBlock++;
                extractor.extrairDeUm(blockset.getArquivoLeitura()[marketempFile], marketempFile);
            }
            Person.clean().saveToFile(blockset.getArquivoEscrita()[orderFile]);
            blockset.fecharArquivoEscrita(orderFile);
            orderFile++;
            orderFile %= DATACOUNT;
        }
        blockset.fecharTodosArquivosEscrita();
        blockset.fecharTodosArquivosDeLeitura();
        finish(orderFile - 1);
    }

    private static void finish(int fileOrder) {
        File order = new File("./bin/output_" + fileOrder + ".tmp");
        File rename = new File("./bin/OrderPerson.bin");
        rename.delete();
        order.renameTo(rename);
        File tempFile = new File("./bin/output_" + DATACOUNT + ".tmp");
        tempFile.delete();
        for (int i = 1; i < DATACOUNT; i++) {
            tempFile = new File("./bin/output_" + i + ".tmp");
            tempFile.delete();
            tempFile = new File("./bin/output_" + (i + DATACOUNT) + ".tmp");
            tempFile.delete();
        }
    }

    private static void calcAverageTime() throws IOException {
        System.out.println("Iniciando calculo do tempo médio...");
        timeEnd = new long[10];
        AverageTime = 0;
        for (int i = 0; i < 10; i++) {
            timeStart = System.currentTimeMillis();
            generateBlocks();
            interlayerBlocks();
            timeEnd[i] = System.currentTimeMillis() - timeStart;
            AverageTime += timeEnd[i];
        }
        System.out.println("Tempo Médio da Execução [10x]: " + (AverageTime / 1000) / 60 + " minutos.");
    }

    private static int getRepeatCount() {
        return countRepeats;
    }

    private static int getBlocksCount() {
        return countBlocks;
    }

    public void init() throws IOException {
        timeEnd = new long[10];
        AverageTime = 0;
        System.out.println("Relax and take a coffe..." + "\n\nVocê pode ver os arquivos sendo gerados na pasta " +
            "\n\nA primeira execução será unica, e em seguida o tempo médio de 10 execuções será calculado.");
        timeStart = System.currentTimeMillis();
        generateBlocks();
        interlayerBlocks();
        timeEnd[0] = System.currentTimeMillis() - timeStart;
        System.out.println("\nOrdenação única concluida." + "\n\nNumero de Blocos Gerados: " + getBlocksCount() +
            "\nQuantidades de repetições no arquivo: " + getRepeatCount() + "\nTempo: " + ((timeEnd[0] / 1000) / 60) +
            " minutos.");
        calcAverageTime();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}