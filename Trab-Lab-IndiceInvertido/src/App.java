import java.util.Scanner;

public class App {
    private static Index index;

    public static void main(String[] args) throws Exception {
        index = new Index();
        System.out.println("Amount: "+index.getAmount());
        search();
    }

    private static void search() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Insira o termo de pesquisa:");
        String input = userInput.nextLine().trim().toLowerCase();
        userInput.close();

        index.searchEvent(input);
        search();
    }
}
