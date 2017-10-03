// NAME: Alexander Hale
// ID: 260672475

import java.lang.Math.*;

class ExpressionTree {
    private String value;
    private ExpressionTree leftChild, rightChild, parent;
    
    ExpressionTree() {
        value = null; 
        leftChild = rightChild = parent = null;
    }
    
    // Constructor
    /* Arguments: String s: Value to be stored in the node
                  ExpressionTree l, r, p: the left child, right child, and parent of the node to created      
       Returns: the newly created ExpressionTree               
    */
    ExpressionTree(String s, ExpressionTree l, ExpressionTree r, ExpressionTree p) {
        value = s; 
        leftChild = l; 
        rightChild = r;
        parent = p;
    }
    
    /* Basic access methods */
    String getValue() { return value; }

    ExpressionTree getLeftChild() { return leftChild; }

    ExpressionTree getRightChild() { return rightChild; }

    ExpressionTree getParent() { return parent; }


    /* Basic setting methods */ 
    void setValue(String o) { value = o; }
    
    // sets the left child of this node to n
    void setLeftChild(ExpressionTree n) { 
        leftChild = n; 
        n.parent = this; 
    }
    
    // sets the right child of this node to n
    void setRightChild(ExpressionTree n) { 
        rightChild = n; 
        n.parent=this; 
    }
    

    // Returns the root of the tree describing the expression s
    // Watch out: it makes no validity checks whatsoever!
    ExpressionTree(String s) {
        // check if s contains parentheses. If it doesn't, then it's a leaf
        if (s.indexOf("(")==-1) setValue(s);
        else {  // it's not a leaf

            /* break the string into three parts: the operator, the left operand,
               and the right operand. ***/
            setValue( s.substring( 0 , s.indexOf( "(" ) ) );
            // delimit the left operand 2008
            int left = s.indexOf("(")+1;
            int i = left;
            int parCount = 0;
            // find the comma separating the two operands
            while (parCount>=0 && !(s.charAt(i)==',' && parCount==0)) {
                if ( s.charAt(i) == '(' ) parCount++;
                if ( s.charAt(i) == ')' ) parCount--;
                i++;
            }
            int mid=i;
            if (parCount<0) mid--;

        // recursively build the left subtree
            setLeftChild(new ExpressionTree(s.substring(left,mid)));
    
            if (parCount==0) {
                // it is a binary operator
                // find the end of the second operand.F13
                while ( ! (s.charAt(i) == ')' && parCount == 0 ) )  {
                    if ( s.charAt(i) == '(' ) parCount++;
                    if ( s.charAt(i) == ')' ) parCount--;
                    i++;
                }
                int right=i;
                setRightChild( new ExpressionTree( s.substring( mid + 1, right)));
        }
    }
    }


    // Returns a copy of the subtree rooted at this node... 2014
    ExpressionTree deepCopy() {
        ExpressionTree n = new ExpressionTree();
        n.setValue( getValue() );
        if ( getLeftChild()!=null ) n.setLeftChild( getLeftChild().deepCopy() );
        if ( getRightChild()!=null ) n.setRightChild( getRightChild().deepCopy() );
        return n;
    }
    
    // Returns a String describing the subtree rooted at a certain node.
    public String toString() {
        String ret = value;
        if ( getLeftChild() == null ) return ret;
        else ret = ret + "(" + getLeftChild().toString();
        if ( getRightChild() == null ) return ret + ")";
        else ret = ret + "," + getRightChild().toString();
        ret = ret + ")";
        return ret;
    } 


    // Returns the value of the expression rooted at a given node
    // when x has a certain value
    double evaluate(double x) {
    	String root = this.getValue();   // get the root of the current subtree
	    if (Character.isDigit(root.charAt(0))) {
	    	return Double.parseDouble(root);    // if root is a number, return that number
	    } else if (root.equals("x")) {
	    	return x;		// if root is the variable, return the value of the variable
	    } else {
	    	// the current root is an operator
	    	Double left = null, right = null;  // if the node has no left or right child, they will remain null
	    	if (this.getLeftChild() != null) {
	    		left = this.getLeftChild().deepCopy().evaluate(x);  // evaluate the expression formed by the subtree that has the left child as its root
	    	} else {
	    		// TODO: malformed expression?
	    	}
	    	if (this.getRightChild() != null) {
	    		right = this.getRightChild().deepCopy().evaluate(x); // evaluate the expression formed by the subtree that has the right child as its root
	    	} else {
	    		// TODO: malformed expression?
	    	}
	    	
	    	return operate(root, left, right);    // perform the specified operation, then return it
	    }
    }                                                 

    /* returns the root of a new expression tree representing the derivative of the
       original expression */
    ExpressionTree differentiate() {
		// create a copy of the original expression
    	ExpressionTree diff = this.deepCopy();
    	
    	String root = diff.getValue();  // get root for this recursive call
    	if (root.equals("x")) {
    		diff.setValue("1");   // if root = x, change value of root to 1
    	} else if (Character.isDigit(root.charAt(0))) {
    		diff.setValue("0");   // if root = x, change value of root to 0
    	} else if (root.equals("add") || root.equals("minus")) {
    		// if root = + or root = -, leave root and differentiate children
    		diff.setLeftChild(diff.leftChild.differentiate());
    		diff.setRightChild(diff.rightChild.differentiate());
    	} else if (root.equals("mult")) {
    		// if root = *, change root and its children to represent diff(f)*g + f*diff(g)
    		ExpressionTree leftChild = diff.getLeftChild().deepCopy();   // need to copy children to temporary variables
    		ExpressionTree rightChild = diff.getRightChild().deepCopy();
    		diff.setLeftChild(new ExpressionTree("mult", leftChild.differentiate(), rightChild, diff));
    		diff.setRightChild(new ExpressionTree("mult", leftChild, rightChild.differentiate(), diff));
    		diff.setValue("add");
    	} else if (root.equals("sin")) {
    		// find the child (could be in the right or left child position)
    		ExpressionTree child;
    		if (diff.getLeftChild() == null) child = diff.getRightChild();
    		else child = diff.getLeftChild();
    		
    		// if root = sin, change root and its children to represent cos(f)*diff(f)
    		diff.setLeftChild(new ExpressionTree("cos", child, null, diff));
    		diff.setRightChild(child.differentiate());
    		diff.setValue("mult");
    	} else if (root.equals("cos")) {
    		// find the child (could be in the right or left child position)
    		ExpressionTree child;
    		if (diff.getLeftChild() == null) child = diff.getRightChild();
    		else child = diff.getLeftChild();
    		
    		// if root = cos, change root and its children to represent -sin(f)*diff(f)
    		diff.setRightChild(new ExpressionTree("mult", new ExpressionTree("sin", child, null, diff.getRightChild()), child.differentiate(), diff));
    		diff.setLeftChild(new ExpressionTree("0"));
    		diff.setValue("minus");
    	} else if (root.equals("exp")) {
    		// find the child (could be in the right or left child position)
    		ExpressionTree child;
    		if (diff.leftChild == null) child = diff.getRightChild();
    		else child = diff.getLeftChild();
    		
    		// if root = exp, change root and its children to represent exp(f)*diff(f)
    		diff.setLeftChild(new ExpressionTree("exp", child, null, diff));
    		diff.setRightChild(child.differentiate());
    		diff.setValue("mult");
    	} else {
    		// TODO: malformed expression?
    	}                    
        return diff;
    }
    
    // helper method for evaluate(). Takes an operator and two operands, then performs the appropriate operation.
    Double operate(String op, Double l, Double r) {
		if (op.equals("add")) {
			return l + r;
		} else if (op.equals("mult")) {
			return l * r;
		} else if (op.equals("minus")) {
			return l - r;
		} else if (op.equals("sin")){
			if (l == null) return Math.sin(r);
			else if (r == null) return Math.sin(l);
			else return null;    // TODO: malformed expression?
		} else if (op.equals("cos")) {
			if (l == null) return Math.cos(r);
			else if (r == null) return Math.cos(l);
			else return null;    // TODO: malformed expression?
		} else if (op.equals("exp")) {
			if (l == null) return Math.exp(r);
			else if (r == null) return Math.exp(l);
			else return null;    // TODO: malformed expression?
		} else {
			return null;  // TODO: malformed expression?
		}
    }
        
    
    public static void main(String args[]) {
	   	ExpressionTree e = new ExpressionTree("mult(add(2,x),cos(x))");
        System.out.println(e);
        System.out.println(e.evaluate(1));
        System.out.println(e.differentiate());
        System.out.println(e.differentiate().evaluate(1));    // should be -1.98
        
        System.out.println();
        
        ExpressionTree f = new ExpressionTree("mult(x,add(add(2,x),cos(minus(x,4))))");
        System.out.println(f);
        System.out.println(f.evaluate(1));
        System.out.println(f.differentiate());
        System.out.println(f.differentiate().evaluate(1));    // should be 3.15
 }
}
