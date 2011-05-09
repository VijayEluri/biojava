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
 * Created on 2011.05.09 by kohchuanhock
 *
 */
package org.biojava3.aaProp;

import java.util.Hashtable;

import org.biojava3.core.sequence.ProteinSequence;


/**
 * An interface to generate some basic physico-chemical properties of protein sequences
 * @author kohchuanhock
 * @version 2011.05.09
 */
public interface AAProperties {
	/**
	 * @param sequence
	 * @return Molecular weight of sequence.
	 */
	public double getMolecularWeight(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @param assumeCysReduced
	 * @return Extinction coefficient of sequence. Two possible assumption 1) Cys are reduced 2) Cys form cystines.
	 */
	public double getExtinctionCoefficient(ProteinSequence sequence, boolean assumeCysReduced);
	/**
	 * @param sequence
	 * @return Instability index of sequence.
	 */
	public double getInstabilityIndex(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @return Aliphatic Index of sequence.
	 */
	public double getApliphaticindex(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @return Grand Average of Hydropathy of sequence.
	 */
	public double getAvgHydropathy(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @return Isoelectric point of sequence.
	 */
	public double getIsoPoint(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @return Length of sequence.
	 */
	public double getLength(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @return Net charge of sequence
	 */
	public double getNetCharge(ProteinSequence sequence);
	/**
	 * @param sequence
	 * @param aminoAcidCode
	 * @return Composition of aminoAcidCode in sequence. Total number of aminoAcidCode / Length of sequence.
	 */
	public double getEnrichment(ProteinSequence sequence, char aminoAcidCode);
	/**
	 * @param sequence
	 * @return Hashtable that contains the composition of all amino acids.
	 */
	public Hashtable<Character, Double> getAAComposition(ProteinSequence sequence); 
}
