public class Exercicio2 {

    // Main method to run the program
    public static void main(String[] args) {
        double soma = 0;

        // Loop through numbers 1 to 5 and print them
        for (int i =1; i<=5; i = i + 1) {
            System.out.println(i);
        }
    }

    // Method to calculate the sum of numbers up to n
    public static int somanumeros(int n){
        if (n==1) return 1; // Base case: return 1 if n is 1

        // Recursive case: return the sum of n and the sum of numbers up to n-1
        else return n+somanumeros(n-1);
    }
}