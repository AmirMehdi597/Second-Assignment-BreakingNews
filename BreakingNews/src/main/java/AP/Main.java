package AP;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String apiKey = "581531e767b647a39bbb6ce74f357d66";
        Infrastructure infrastructure = new Infrastructure(apiKey);
        Scanner scanner = new Scanner(System.in);
        infrastructure.displayNewsList();
        System.out.println("Please select a news article by entering the number (1 - " + infrastructure.getNewsList().size() + "):");
        int choice = scanner.nextInt() - 1;
        infrastructure.displayFullNews(choice);
        scanner.close();
    }
}
