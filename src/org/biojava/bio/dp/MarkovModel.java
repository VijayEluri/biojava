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


package org.biojava.bio.dp;

import org.biojava.utils.*;
import org.biojava.bio.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.dist.*;

/**
 * A markov model.
 * <P>
 * All probablities are in log space.
 * <P>
 * This interface models a subset of hidden markov models with an explicit start
 * and end state. In principle, these can be combined together, so that a state
 * within one model may be an entire model in its own right, wired via
 * container->start and end->container. For the sample methods to work, the log
 * scores must be probabilities (sum to 1).
 */
public interface MarkovModel extends Changeable {
  /**
   * Signals that the architecture of the model is changing.
   * <P>
   * For a transition creation, the changed field should be a two element
   * array containing the source and destination states of the new transition,
   * and the previous field should be null. Likewise for the removal of a
   * transition, the previos should hold the array, and changed should be null.
   */
  public static final ChangeType ARCHITECTURE = new ChangeType(
    "create or destroy a transition",
    "org.biojava.bio.dp.MarkovModel",
    "ARCHITECTURE"
  );
  
  /**
   * Signals that one or more parameters have altered.
   * <P>
   * If it is clear which parameter has changed, then this should be in the
   * current and/or previous field. Otherwise, these should be null.
   */
  public static final ChangeType PARAMETER = new ChangeType(
    "parameter altered",
    "org.biojava.bio.dp.MarkovModel",
    "PARAMETER"
  );
  
  /**
   * Alphabet that is emitted by the emission states.
   */
  Alphabet emissionAlphabet();

  /**
   * FiniteAlphabet of the states.
   * <P>
   * We are modeling a finite-state-machine, so there will be a finite set of
   * states.
   * <P>
   * The MagicalState returned by getMagicalState is always contained
   * within this as the start/end state.
   *
   * @return the alphabet over states
   */
  FiniteAlphabet stateAlphabet();
  
  /**
   * The MagicalState for this model.
   */
  MagicalState magicalState();
  
  /**
   * The number of heads on this model.
   * <P>
   * Each head consumes a single SymbolList. A single-head model just consumes/
   * emits a single sequence. A two-head model performs alignment between two
   * sequences (e.g. smith-waterman). Models with more heads do more interesting
   * things.
   */
  int heads();
  
  /**
   * Get a probability Distribution over the transition from 'source'. 
   *
   * @param source  the State currently occupied
   * @return the probability Distribution over the reachable states
   * @throws IllegalSymbolException if from is not a legal state
   */
  Distribution getWeights(State source)
  throws IllegalSymbolException;

  /**
   * Set the probability distribution over the transitions from 'source'.
   * <P>
   * This should throw an IllegalAlphabetException if the source alphabet in
   * 'dist' is not the same alphabet as returned by transitionsFrom(source).
   *
   * @param source  the source State
   * @param dist    the new distribution over transitions from 'source'
   * @throws IllegalSymbolException if source is not a state in this model
   * @throws IllegalAlphabetException if the distribution has the wrong source
   *         alphabet
   * @throws ChangeVetoException if for any reason the distribution can't be
   *         replaced at this time
   */
  void setWeights(State source, Distribution dist)
  throws IllegalSymbolException, IllegalAlphabetException, ChangeVetoException;
  
  /**
   * Returns the FiniteAlphabet of all states that have a transition from 'source'.
   *
   * @param source  the source State
   * @return  a FiniteAlphabet of State objects that can reach from 'source'
   */
  FiniteAlphabet transitionsFrom(State source) throws IllegalSymbolException;
  
  /**
   * Returns the FiniteAlphabet of all states that have a transition to 'dest'.
   *
   * @param dest  the destination state
   * @return  a FiniteAlphabet of State objects that can reach 'dest'
   */
  FiniteAlphabet transitionsTo(State dest) throws IllegalSymbolException;

  /**
   * Returns wether a transition exists or not.
   *
   * @param from the transitin source
   * @param to the transition destination
   * @return true/false depending on wether this model has the transition
   * @throws IllegalSymbolException if either from or to are not states in this
   *         model
   */
  boolean containsTransition(State from, State to)
  throws IllegalSymbolException;
  
  /**
   * Makes a transition between two states legal.
   * <P>
   * This should inform each TransitionListener that a transition is to be
   * created using preCreateTransition, and if none of the listeners fire a
   * ChangeVetoException, it should create the transition, and then inform each
   * TransitionListener with postCreateTransition.
   *
   * @param from  the State currently occupied
   * @param to  the State to move to
   * @throws IllegalSymbolException if either from or to are not legal states
   * @throws ChangeVetoException if creating the transition is vetoed
   */
  void createTransition(State from, State to)
  throws IllegalSymbolException, ChangeVetoException;
   
  /**
   * Breaks a transition between two states legal.
   * <P>
   * This should inform each TransitionListener that a transition is to be
   * broken using preDestroyTransition, and if none of the listeners fire a
   * ChangeVetoException, it should break the transition, and then inform each
   * TransitionListener with postDestroyTransition.
   *
   * @param from  the State currently occupied
   * @param to  the State to move to
   * @throws IllegalSymbolException if either from or to are not legal states
   * @throws ChangeVetoException if breaking the transition is vetoed
   */
  void destroyTransition(State from, State to)
  throws IllegalSymbolException, ChangeVetoException;

  /**
   * Adds a state to the model.
   *
   * @param newState  the state to add
   * @throws IllegalSymbolException if the state is not valid or is a MagicalState
   * @throws ChangeVetoException  if either the model does not allow states to
   *         be added, or the change was vetoed
   */
  void addState(State newState)
  throws IllegalSymbolException, ChangeVetoException;

  /**
   * Remove a state from the model.
   * <P>
   * States should not be removed untill they are involved in no transitions.
   * This is to avoid producing corrupted models by accident.
   *
   * @param toGo  the state to remove
   * @throws IllegalSymbolException if the symbol is not part of this model
   *         or a MagicalState
   * @throws IllegalTransitionException if the state is currently involved in
   *         any transitions
   * @throws ChangeVetoException  if either the model does not allow states to
   *         be removed, or the change was vetoed
   */
  void removeState(State toGo)
  throws IllegalTransitionException,
  IllegalSymbolException, ChangeVetoException;
  
  /**
   * Add this as a listener to each distribution in a model. It will pump the
   * WEIGHTS and NULL_MODEL events on the distribitions over to the HMM.
   *
   * @author Matthew Pocock
   * @since 1.1
   */
  public class DistributionForwarder extends ChangeAdapter {
    public DistributionForwarder(Object source, ChangeSupport cs) {
      super(source, cs);
    }
    
    protected ChangeEvent generateEvent(ChangeEvent ce) {
      ChangeType ct = ce.getType();
      if( (ct == Distribution.WEIGHTS) || (ct == Distribution.NULL_MODEL) ) {
        return new ChangeEvent(
          getSource(),
          PARAMETER,
          null, null,
          ce
        );
      }
      return null;
    }
  }
}
