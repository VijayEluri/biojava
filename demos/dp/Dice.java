/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

import org.biojava.bio.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.dp.*;

/**
 * This demo file is a simulation of the "The occasionally dishonest casino" example
 * from the book by R. Durbin, S. Eddy, A. Krogh, G. Mitchison, 
 * "Biological Sequence Analysis", 
 * Chapter 3 Markov Chains and hidden Markov models, Section 2, pp55-57.
 * <P>
 * Use: <code>Dice</code>
 * <p>
 * The output consists of three lines:  line 1 represents the output sequence generated
 * by the hidden markov model (f for fair and l for loaded).  Line 2 contains the name of 
 * the die which emitted the corresponding output symbol.  Line 3 shows the state 
 * sequence predicted by the Viterbi algorithm. 
 * <P>
 *
 * @author Samiul Hasan
 */

public class Dice
{
    public static void main(String[] args) throws Exception
    {
    	Symbol[] rolls=new Symbol[6];
	
    	//set up the dice alphabet
    	SimpleAlphabet diceAlphabet=new SimpleAlphabet();
    	diceAlphabet.setName("DiceAlphabet");
    
    	for(int i=1;i<7;i++)
    	{
	    rolls[i-1]= new SimpleSymbol((char)('0'+i),""+i,Annotation.EMPTY_ANNOTATION);
	    diceAlphabet.addSymbol(rolls[i-1]);
	}  
  
    	int [] advance = { 1 };
	EmissionState fair   = StateFactory.DEFAULT.createState(diceAlphabet, advance, "fair");
	EmissionState loaded = StateFactory.DEFAULT.createState(diceAlphabet, advance, "loaded");
	
	SimpleMarkovModel casino=new SimpleMarkovModel(1, diceAlphabet);
	casino.addState(fair);
	casino.addState(loaded);
	
	//set up transitions between states.
	casino.createTransition(casino.magicalState(),fair);
	casino.createTransition(casino.magicalState(),loaded);
	casino.createTransition(fair,casino.magicalState());
	casino.createTransition(loaded,casino.magicalState());
	casino.createTransition(fair,loaded);
	casino.createTransition(loaded,fair);
	casino.createTransition(fair,fair);
	casino.createTransition(loaded,loaded);
	
	//set up emission probabilities.
	for(int i=0;i<rolls.length;i++)
	{
	    fair.setWeight(rolls[i],-Math.log(6));
	    loaded.setWeight(rolls[i],-Math.log(10));
	}
	loaded.setWeight(rolls[5],-Math.log(2));
	
	//set up transition scores.
	casino.setTransitionScore(casino.magicalState(),fair,  Math.log(0.8));
	casino.setTransitionScore(casino.magicalState(),loaded,Math.log(0.2));

	casino.setTransitionScore(fair,loaded,               Math.log(0.04));
	casino.setTransitionScore(fair,fair,                 Math.log(0.95));
	casino.setTransitionScore(fair,casino.magicalState(),Math.log(0.01));
	
	casino.setTransitionScore(loaded,fair,                 Math.log(0.09));
	casino.setTransitionScore(loaded,loaded,               Math.log(0.90));
	casino.setTransitionScore(loaded,casino.magicalState(),Math.log(0.01));
	
	DP dp=DPFactory.createDP(casino);
	StatePath obs_rolls = dp.generate(300);
	
	SymbolList roll_sequence = obs_rolls.symbolListForLabel(StatePath.SEQUENCE);
	SymbolList[] res_array = {roll_sequence};
	StatePath v = dp.viterbi(res_array);
	
	//print out obs_sequence, output, state symbols.
	for(int i = 1; i <= obs_rolls.length()/60; i++) {
	  for(int j=i*60; j<Math.min((i+1)*60, obs_rolls.length()); j++)  {
	    System.out.print(obs_rolls.symbolAt(StatePath.SEQUENCE, j+1).getToken());
	  }
	  System.out.print("\n");
	  for(int j=i*60; j<Math.min((i+1)*60, obs_rolls.length()); j++)  {
	    System.out.print(obs_rolls.symbolAt(StatePath.STATES, j+1).getToken());
	  }
	  System.out.print("\n");
	  for(int j=i*60; j<Math.min((i+1)*60, obs_rolls.length()); j++)  {
	    System.out.print(v.symbolAt(StatePath.STATES, j+1).getToken());
	  }
	  System.out.print("\n\n");	  
	}	
    }
}
