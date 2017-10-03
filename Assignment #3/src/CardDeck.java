// NAME: Alexander Hale
// ID: 260672475

import java.util.*;

class CardDeck {
    LinkedList<Integer> deck;

    // constructor, creates a deck with n cards, placed in increasing order
    CardDeck(int n) {
		deck = new LinkedList<Integer> ();
		for (int i=1;i<=n;i++) deck.addLast(new Integer(i));
    }

    // executes the card trick
    public void runTrick() {
		while (!deck.isEmpty()) {
		    // remove the first card and remove it
		    Integer topCard = deck.removeFirst();
		    System.out.println("Showing card "+topCard);
	
		    // if there's nothing left, we are done
		    if (deck.isEmpty()) break;
		    
		    // otherwise, remove the top card and place it at the back.
		    Integer secondCard = deck.removeFirst();
		    deck.addLast(secondCard);
	
		    System.out.println("Remaining deck: "+deck);
		}
    }


    // NOTE: did not need parameter n for this implementation, but left it in case it would break the automatic marking code
    public void setupDeck(int n) {
    	// sort deck (question states the cards may not be in order)
    	CardDeck sorted = new CardDeck(deck.size());
    	this.deck = sorted.deck;
    	
    	if (deck.size() > 1) {
	    	LinkedList<Integer> temp = new LinkedList<Integer>();  // temporary LinkedList
	    	temp.addFirst(this.deck.removeLast());  // transfer nth element of deck to temporary list
	    	
	    	while (!this.deck.isEmpty()){
	    		temp.addFirst(temp.removeLast());  // move last element of temporary list to front
	    		temp.addFirst(this.deck.removeLast());  // move last element of deck to temporary list
	    	}
	    	
	    	this.deck = temp; // assign reference of deck to temporary LinkedList
    	}
    }



    public static void main(String args[]) {
	// this is just creating a deck with cards in increasing order, and running the trick. 
	CardDeck d = new CardDeck(10);
	d.runTrick();
    	
    System.out.println();

	// this is calling the method you are supposed to write, and executing the trick.
	CardDeck e = new CardDeck(12);
	e.setupDeck(0);
	e.runTrick();
    }
}

    