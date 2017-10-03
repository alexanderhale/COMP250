// NAME: Alexander Hale
// ID: 260672475

import java.io.*;
import java.util.*;

public class Expression {
	static String delimiters="+-*%()";
	
	
	// Returns the value of the arithmetic Expression described by expr
	// Throws an exception if the Expression is malformed
	static Integer evaluate(String expr) throws Exception {      
	    StringTokenizer st = new StringTokenizer( expr , delimiters , true );   // split input string into tokens
	    
	    // Check that parentheses match
	    Stack<String> parenStack = new Stack<String>();
	    while (st.hasMoreTokens()) {
	    	String element = st.nextToken();
	    	// check that parentheses match
	    	if (element.equals("(")) {
	    		parenStack.push(element);   // waiting for match
	    	} else if (element.equals(")")) {
	    		if (parenStack.isEmpty()) {
	    			throw new Exception("Missing opening parentheses.");
	    		} else {
	    			parenStack.pop();  // match found
	    			// only one parentheses type, so we don't have to match types	
	    		}
	    	}	    	
	    }
	    if (!parenStack.isEmpty()) throw new Exception("Missing closing parentheses.");  // check for unclosed opening parentheses
	    
	    st = new StringTokenizer(expr, delimiters, true);
	    Stack<String> operators = new Stack<String>();  // interim holding stack
	    Stack<String> output = new Stack<String>();   // needs to be inverted at the end
	    boolean wasDigit = false;   // to take care of multi-digit numbers, track whether the last character was a number
	    
	    // convert infix expression into postfix notation (ignore operator precedence, respect brackets)
	    while (st.hasMoreTokens()) {
	    	String next = st.nextToken();	    	
	    	if (Character.isDigit(next.charAt(0))) {  // if token is a number, add it to the output stack
	    		if (wasDigit) {
	    			output.push(output.pop() + next); // multi-digit number
	    		} else {
	    			output.push(next);
	    			wasDigit = true;  // in case next token is number number
	    		}
	    	} else if (next.equals("+") || next.equals("-") || next.equals("*") || next.equals("%")) {
	    		wasDigit = false;
	    		if (operators.isEmpty() || operators.peek().equals("(")) {
	    			operators.push(next);  // hold for later
	    		} else {
	    			output.push(operators.pop());  // output top
	    			operators.push(next);  // hold for later
	    		}
    		} else if (next.equals("(")) {  // if token is an opening parentheses, hold it for later
    			wasDigit = false;
    			operators.push(next);
    		} else {					// if token is a closing parentheses, output from holding stack until an opening parentheses is reached
    			wasDigit = false;
    			while (!operators.peek().equals("(")) {
    				output.push(operators.pop());
    				if (operators.isEmpty()) throw new Exception("Too many closing parantheses");
    			}
    			operators.pop();  // remove the matched opening parentheses
    		}
	    }
	    while (!operators.isEmpty()) {
	    	if (operators.peek() == "(") throw new Exception("Too many opening parentheses");
	    	else output.push(operators.pop());  // output the remaining operators
	    }
	    
	    // invert output
	    Stack<String> postfix = new Stack<String>();
	    while (!output.isEmpty()) {
	    	postfix.push(output.pop());
	    }
	    
	    // evaluate postfix expression
	    Stack<Integer> values = new Stack<Integer>();
	    while (!postfix.isEmpty()) {
	    	String next = postfix.pop();
	    	if (Character.isDigit(next.charAt(0))) values.push(Integer.parseInt(next));   // if token is a number, hold it for later
	    	else {
	    		if (values.size() < 2) throw new Exception("Improper operators."); // also handles case of empty brackets (e.g. "()+1" is malformed, but 1*()12 = 12)
	    		else {
	    			Integer op1 = values.pop();
	    			Integer op2 = values.pop();
	    			values.push(operate(op2, op1, next.charAt(0)));   // perform the operation on the top two numbers in the stack, then push the result back onto the stack
	    		}
	    	}
	    }
	    if (values.size() != 1) {
	    	throw new Exception("Improper operators.");
	    } else {
	    	return values.pop();   // result is the only remaining value in the stack
	    }
	}
	
	// carry out the specified operation on two Integers x and y
		static Integer operate(Integer x, Integer y, Character op) {
			if (op == '+') {
				return x + y;
			} else if (op == '-') {
				return x - y;
			} else if (op == '*') {
				return x * y;
			} else {
				return x / y;
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