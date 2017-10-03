// STUDENT_NAME: Alexander Hale
// STUDENT_ID: 260672475

import java.util.*;
import java.io.*;



public class Scrabble{

    static HashSet<String> myDictionary; // this is where the words of the dictionary are stored

    // DO NOT CHANGE THIS METHOD
    // Reads dictionary from file
    public static void readDictionaryFromFile(String fileName) throws Exception {
        myDictionary=new HashSet<String>();

        BufferedReader myFileReader = new BufferedReader(new FileReader(fileName) );

        String word;
        while ((word=myFileReader.readLine())!=null) myDictionary.add(word);

	myFileReader.close();
    }



    /* Arguments: 
        char availableLetters[] : array of characters containing the letters that remain available
        String prefix : Word assembled to date
        Returns: String corresponding to the longest English word starting with prefix, completed with zero or more letters from availableLetters. 
	         If no such word exists, it returns the String ""
     */
     public static String longestWord(char availableLetters[], String prefix) {
    	 // base case
    	 if (availableLetters.length == 0) {
    		 if (myDictionary.contains(prefix)) {
    			 return prefix;
    		 } else {
    			 return "";
    		 }
    	 }
    	 
    	 // place to store valid words found at the current recursive step
    	 ArrayList<String> completedWords = new ArrayList<>();
    	 
    	 // check if the prefix itself is a valid word
		 if (myDictionary.contains(prefix)) {
			 completedWords.add(prefix);
		 }
    	 
		 // try all other possibilities by adding one extra letter, then recursively calling longestWord
    	 for (int i = 0; i < availableLetters.length; i++) {
    		 
    		 // copy and alter prefix and availableLetters for new combinations
    		 String newPref = prefix + availableLetters[i];
    		 char[] newLett = new char[availableLetters.length-1];
    		 for (int j = 0; j < availableLetters.length-1; j++) {
    			 if (j < i) {
    				 newLett[j] = availableLetters[j];
    			 } else {
    				 newLett[j] = availableLetters[j+1];
    			 }
    		 }
			 completedWords.add(longestWord(newLett, newPref));
    	 }
    	 
    	 // find longest word from the list
    	 String longest = completedWords.get(0);
    	 for (int z = 1; z < completedWords.size(); z++) {
    		 if (completedWords.get(z).length() > longest.length()) {
    			 longest = completedWords.get(z);
    		 }
    	 }
    	 
		 return longest;
    }

    
    
    /* main method
        You should not need to change anything here.
     */
    public static void main (String args[]) throws Exception {
       
		// First, read the dictionary
		try {
		    readDictionaryFromFile("englishDictionary.txt");
	        }
	        catch(Exception e) {
	            System.out.println("Error reading the dictionary: "+e);
	        }
	        
	        
	    // Ask user to type in letters
	    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in) );
	    char letters[]; 
	    do {
	        System.out.println("Enter your letters (no spaces or commas):");
	        
	        letters = keyboard.readLine().toCharArray();
	
	    // now, enumerate the words that can be formed
	        String longest = longestWord(letters, "");
	        System.out.println("The longest word is " + longest);
	    } while (letters.length!=0);
	
	    keyboard.close();
    }
}