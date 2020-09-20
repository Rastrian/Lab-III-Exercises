package profiles.person;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ExtractorPerson {
    // Extrator para retirada do menor dado, composto por <Person> uma Person
    // extraida
    // <Integer> arquivo no qual foi extraido

    public TreeMap<Person, Integer> mapExtrator;

    public ExtractorPerson() {
        mapExtrator = new TreeMap<Person, Integer>();
    }

    public void extrairDeTodos(RandomAccessFile[] arquivos){
        try{
            for(int i=0;i<arquivos.length;i++){
                if(arquivos[i].getFilePointer() < arquivos[i].length()){
                    Person aux = Person.readFromFile(arquivos[i]);
                    //Se eu realmente li uma Person eu coloco ela no extrator
                    //Se essa Person for um separador, eu não faço nada;
                    if(!aux.checkSeparator()) mapExtrator.put(aux, i);
                }
            }
        }catch (IOException E){
            E.printStackTrace();
        }
    }

    public void extrairDeUm(RandomAccessFile arquivo, int nArquivo){
        try{
            if(arquivo.getFilePointer() < arquivo.length()){
                Person aux = Person.readFromFile(arquivo);
                //Se eu realmente li uma Person eu coloco ela no extrator
                //Se essa Person for um separador, eu não faço nada;
                if(!aux.checkSeparator()) mapExtrator.put(aux, nArquivo);
            }
        }catch (IOException E){
            E.printStackTrace();
        }            
    }

    public boolean existeFonteValida(){
        if(mapExtrator.size() != 0) return true;

        return false;
    }

    public Entry<Person,Integer> retornaMenor(){
        //O TreeMap sempre vai colocar o menor elemento na primeira posição do arranjo
        Entry <Person, Integer> E = mapExtrator.entrySet().iterator().next();
        
        //se o menor elemento do arrannjo não é um separador, comparação só pra garantir
        if(!E.getKey().checkSeparator()){
            //removo o elemento do arranjo de comparações e o retorno 
            mapExtrator.remove(E.getKey());
            return E;
        }
        
        return E;     

    }
}
