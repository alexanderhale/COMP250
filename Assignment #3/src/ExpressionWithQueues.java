// NAME: Alexander Hale
// ID: 260672475

import java.io.*;
import java.util.*;

public class ExpressionWithQueues {
	static String delimiters="+-*%()";
	
	
	// Returns the value of the arithmetic Expression described by expr
	// Throws an exception if the Expression is malformed
	static Integer evaluate(String expr) throws Exception {      
	    StringTokenizer st = new StringTokenizer( expr , delimiters , true );
	    
	    // Check that parentheses match
	    /* TODO: Integrate parentheses checking into the other sections of the algorithm
	    Stack<String> parenStack = new Stack<String>();
	    while (st.hasMoreTokens()) {
	    	String element = st.nextToken();
	    	// check that parentheses match
	    	if (element.equals("(")) {
	    		parenStack.push(element);
	    	} else if (element.equals(")")) {
	    		if (parenStack.isEmpty()) {
	    			throw new Exception("Missing opening parentheses.");
	    		} else {
	    			parenStack.pop();
	    			// only one parentheses type, so we don't have to match types	
	    		}
	    	}	    	
	    }
	    // check that there are no opening parentheses that haven't been closed
	    if (!parenStack.isEmpty()) {
	    	throw new Exception("Missing closing parentheses.");
	    } */
	    
	    // Put all characters into queue
	    st = new StringTokenizer( expr , delimiters , true );
	    Queue<String> characters = new LinkedList<String>();
	    while (st.hasMoreTokens()) {
	    	characters.add(st.nextToken());
	    }
	    
	    Integer total = null, currentNumber = null;
	    Character currentOperator = ' ';
	    
	    while (characters.size() > 0) {
	    	// while there are elements remaining, dequeue the top character
	    	Character topChar = characters.remove().charAt(0);
	    	
	    	// if character is a number  (TODO: this section needs refinement)
	    	if (Character.isDigit(topChar)) {
	    		if (currentNumber != null) {
	    			// add digit on to current number
	    		} else if (currentOperator == ' ') {
	    			if (total == null) total = Character.getNumericValue(topChar);
	    			else throw new Exception("Two-digit number or missing operator.");
	    		} else {
	    			total = operate(total, Character.getNumericValue(topChar), currentOperator);
	    			currentOperator = ' ';
	    			// TODO: must wait to operate until we have all the digits of the number (in case the number is more than one digit)
	    		}
	    	} else if (topChar == '+' || topChar == '-' || topChar == '*' || topChar == '%') {
	    		if (currentOperator == ' ') currentOperator = topChar;
	    		else if (total == null || currentOperator != ' ') throw new Exception("Misplaced operator");
    		} else if (topChar == '(') {
    			String remaining = "";
    			Queue<String> temp = new LinkedList<String>();
    			temp.addAll(characters);
    			while (temp.size() > 0) {
    				remaining += temp.remove().charAt(0);
    			}
    			
    			Integer insideBrackets;
    			try {
					insideBrackets = evaluate(remaining);
				} catch (Exception e) {
					throw new Exception(e);
				}
    			
    			// dequeue until *after* the *corresponding* closing bracket
    				// hard part! Haven't figured out an algorithm for this yet. Maybe use parentheses matching technique?
    			do {
    				topChar = characters.remove().charAt(0);
    			} while (topChar != ')');
    			
    			// follow same procedure as if insideBrackets is a number that just got dequeued
    			if (currentOperator == ' ') {
	    			if (total == null) total = insideBrackets;
	    			else throw new Exception("Two-digit number or missing operator.");
	    			// TODO: 2-digit numbers are allowed!
	    		} else {
	    			total = operate(total, insideBrackets, currentOperator);
	    			currentOperator = ' ';
	    		}
    		} else {
    			return total;
    		}
    	
	    }
	    if (currentOperator != ' ') throw new Exception("Final character is an opeator.");
	    return total;
	    // TODO: write out all possible types of malformed expressions, and make sure all are handled
	}
	
	// carry out the specified operation on two Integers x and y
		static Integer operate(Integer x, Integer y, Character op) {
			if (op == '+') {
				return x += y;
			} else if (op == '-') {
				return x -= y;
			} else if (op == '*') {
				return x *= y;
			} else {
				return x /= y;
			}
		}
		
	public static void main(String args[]) throws Exception {
		String line;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                                      	                        
		do {
			line=stdin.readLine();
			if (line.length()>0) {
				try {
					Integer x=evaluate(line);
					System.out.println(" = " + x);
				}
				catch (Exception e) {
					System.out.println("Malformed Expression: "+e);
				}
			}
		} while (line.length()>0);
	}
}