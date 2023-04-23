package org.example;

import java.util.Arrays;
import java.util.Random;

public class Account {
    static Random random = new Random();

    // Creating properties for each card
    private int[] NUMBER = new int[16];
    private int[] PIN = new int[4];


    public void setNUMBER(int[] NUMBER) {
        this.NUMBER = NUMBER;
    }

    public void setPIN(int[] PIN) {
        this.PIN = PIN;
    }

    // Generating NUMBER
    void generateNUMBER(){

        // First number 4 or 5 are issued by banking and financial institutions
        NUMBER[0] = 4;
        for (int i = 1; i< NUMBER.length - 1; i++){
            NUMBER[i] = i < 6 ? 0 : random.nextInt(10);
        }

        NUMBER[15] = checkSum(NUMBER);
    }

    // Generating checksum
    public int checkSum(int[] NUMBER){
        int[] cardNumber = Arrays.copyOf(NUMBER, NUMBER.length);
        for(int i = 0; i< cardNumber.length - 1; i++){
            if(i % 2 == 0){
                cardNumber[i] *= 2;
            }
            if(cardNumber[i] > 9){
                cardNumber[i] -= 9;
            }
        }

        int sum = Arrays.stream(cardNumber).sum();
        return 10 - (sum % 10);
    }

    public String getNUMBER(){
        return Arrays.toString(NUMBER).replace("[", "").replace(", ", "")
                .replace("]", "");
    }

    // Generating PIN
    void generatePIN(){
        for(int i = 0; i < PIN.length; i++){
            PIN[i] = random.nextInt(9) + 1;
        }
    }

    public String getPIN(){
        return Arrays.toString(PIN).replace("[", "").replace(", ", "")
                .replace("]", "");
    }
}
