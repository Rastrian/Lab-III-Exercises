public enum FilesPath {
    HASHFILE("HashFile.bin"),
    INPUTFILE("PessoasPAA.txt"),
    BINFILE("Persons.bin");

    private String path;

    FilesPath(String PATH) {
        this.path = PATH;
    }
    
    public String getPath() {
        return path;
    }
}