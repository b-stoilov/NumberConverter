package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here

        System.out.print("Enter your number here: ");
        int number = 0;
        boolean pass = false;

        while (!pass) {
            try {
                Scanner sc = new Scanner(System.in);
                number = sc.nextInt();
                pass = true;
                sc.close();
            } catch (Exception e) {
                System.out.print("Entered value is wrong! Please enter a new value: ");
            }
        }


        System.out.println("Your number is: " + number);
        System.out.println("Your number as a string in bulgarian language: ");
        System.out.println(displaySentence(number));


    }

    /** Display the whole sentence which is the string represantation of the number */
    private static String displaySentence (int number) {
        String s = "";

        if (isNegative(number)) {
            s = "минус ";
        }

        s += reformatSentence(generateWholeNumber(number));

        return s;
    }

    /** If somehow there is more than one space between the words, remove it */
    public static String reformatSentence (String s) {
        return s.replaceAll("\\s+", " ");
    }

    /** Generate the whole number */
    private static String generateWholeNumber(int number) {
        String s;

        if (isCornerCase(number)) {
            s = cornerCasesFormat(number);
        } else s = normalCaseFormat(number);

        return s;
    }

    /** If not a corner case, then we are talking about a normal wording, so format it here */
    private static String normalCaseFormat (int number) {
        String sentence = "";

        ArrayList<String> list = numberToList(number);
        int i = list.size();

        for (String word : list) {
            if ((word.equals("1") || word.equals("001")) && list.size() == 2) {
                sentence += "хиляда ";
            } else if ((word.equals("1") || word.equals("001")) && i == 3) {
                sentence += "един милион ";
            } else if (word.equals("1") && i == 4) {
                sentence += "един милиард ";
            } else {
                sentence += genSingleNumber(Integer.parseInt(word)) + figureNumber(i);
            }

            i--;

        }

        return sentence;
    }

    /** Format the corner cases */
    private static String cornerCasesFormat (int number) {
        String sentence = "";

        if (number == 1000000) {
            sentence = "един милион";
        } else if (number == 1000000000) {
            sentence = "един милиард";
        } else if (number == 1000) {
            sentence = "хиляда";
        } else if (number == 0) {
            sentence = "нула";
        }

        return sentence;
    }

    /** Bulgarian wording is weird and there some corner cases that change the formulating of the
     * string represantation that have to be considered, so check it */
    private static boolean isCornerCase (int number) {
        return number == 1000000 || number == 1000000000 || number == 1000 || number == 0;
    }

    /** Determine if we are talking about a million, thousand or billion (but in bulgarian) */
    private static String figureNumber (int listSize) {
        String w = "";

        if (listSize == 2) {
            w = " хиляди ";
        } else if (listSize == 3) {
            w = " милиона ";
        } else if (listSize == 4) {
            w = " милиарда ";
        }

        return w;
    }

    /** Generate the number that is going to contribute to the whole string representation of the number */
    private static String genSingleNumber(int number) {

        String additional = checkAdditionalAnd(number);

        if (betweenTenTwenty(number) && String.valueOf(number).length() > 2) {
            return getHundreds(number) + " и " + formatTenTwenty(number);
        }

        if (betweenTenTwenty(number) && String.valueOf(number).length() < 3) {
            return formatTenTwenty(number);
        }

        return getHundreds(number) + getTens(number) + additional + getNumberValue(number % 100 % 10);
    }

    /** Check if the number needs the word "и" in its string representation */
    private static String checkAdditionalAnd (int number) {
        if (String.valueOf(number).length() > 1 && !betweenTenTwenty(number)) {
            return " и ";
        }

        return " ";
    }

    /** reverse the number, which is converted into a word */
    private static String reverseWord (String word) {
        String revWord = "";

        for (int i = word.length() - 1; i >= 0; i --) {
            revWord += word.charAt(i);
        }

        return revWord;

    }

    /** Convert the number into an array list */
    private static ArrayList<String> numberToList (int number) {
        String temp = formatNumber(number);
        ArrayList<String> numArray = new ArrayList<>();
        String word = "";
        int j = 0;

        for (int i = temp.length() - 1; i >= 0; i --, j ++) {
            if (j != 0 && j % 3 == 0) {
                numArray.add(reverseWord(word));
                word = "";
                j = 0;
            }
            word += temp.charAt(i);
        }

        numArray.add(reverseWord(word));

        Collections.reverse(numArray);

        return numArray;
    }

    /** Format the number because somewhere in the naming of the numbers i have a weird space */
    private static String formatNumber (int number) {
        String stringNumber = String.valueOf(number);

        if (isNegative(number)) {
            stringNumber = stringNumber.substring(1);
        }

        return stringNumber;
    }

    /** Determine if the number has a negative value */
    private static boolean isNegative (int number) {
        return String.valueOf(number).charAt(0) == '-';
    }


    /** Get the name of the first digit of a 3 digit number */
    private static String getHundreds (int number) {
        int hund = number / 100;
        String word = "";

        if (hund != 0) {
            if (hund == 1) {
                word += "сто ";
            } else if (hund == 2) {
                word += "двеста ";
            } else if (hund == 3) {
                word += "триста ";
            } else word += getNumberValue(hund) + "стотин ";
        }

        return word;
    }

    /** If the number is between 10 and 20, format it so it outputs the right word */
    private static String formatTenTwenty (int number) {
        int altTens = number % 100;
        String word = "";

        if (altTens > 10 && altTens < 20) {
            if (altTens == 11) {
                word += "единадесет ";
            } else if (altTens == 12) {
                word += "дванадесет ";
            } else {
                word += getNumberValue(altTens % 10) + "надесет ";
            }
        }

        return word;
    }

    /** Determine if the is between 10 and 20 (again it is 2 digits but its name is different
     * than any 2 digit number outside of these values) */
    private static boolean betweenTenTwenty (int number) {
        return number % 100 > 10 && number % 100 < 20;
    }

    /** A method to get the name of the second number from right to left (десетици на бг) */
    private static String getTens (int number) {

        int tens = number / 10 % 10;
        String word = "";


        if (tens != 0) {
            if (tens == 1) {
                word += "десет ";
            } else if (tens == 2) {
                word += "двадесет ";
            } else {
                word += " " + getNumberValue(tens) + "десет ";
            }
        }

        return word;
    }

    /** A method to get the name of an integer between 1 and 9 */
    public static String getNumberValue (int i) {
        String numberWord = "";

        if (i == 1) {
            numberWord = "едно";
        } else if (i == 2) {
            numberWord = "две";
        } else if (i == 3) {
            numberWord = "три";
        } else if (i == 4) {
            numberWord = "четири";
        } else if (i == 5) {
            numberWord = "пет";
        } else if (i == 6) {
            numberWord = "шест";
        } else if (i == 7) {
            numberWord = "седем";
        } else if (i == 8) {
            numberWord = "осем";
        } else if (i == 9) {
            numberWord = "девет";
        }

        return numberWord;
    }

}
