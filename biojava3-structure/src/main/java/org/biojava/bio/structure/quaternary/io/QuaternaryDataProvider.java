package org.biojava.bio.structure.quaternary.io;

import java.util.List;

import org.biojava.bio.structure.io.mmcif.model.PdbxStructAssembly;
import org.biojava.bio.structure.io.mmcif.model.PdbxStructAssemblyGen;
import org.biojava.bio.structure.io.mmcif.model.PdbxStructOperList;


/** Defines the methods that have to be implemented by a class that provides the data that is necessary to recreate a quaternary structure.
 * 
 * @author Andreas Prlic
 * @since 3.0.5
 */
public interface QuaternaryDataProvider {
	
	/** Tell the provider for which PDB ID the quaternary structure should be returned.
	 * 
	 * @param pdbId
	 */
	public void setPdbId(String pdbId);
	
	/** Data access method for list describing all assemblies
	 * 
	 * @return
	 */
	public List<PdbxStructAssembly> getPdbxStructAssemblies();
	
	/** Data access method for list describing all assemblies
	 * 
	 * @return
	 */
	public List<PdbxStructAssemblyGen> getPdbxStructAssemblyGens();
	
	/** Get all the possible operators
	 * 
	 * @return
	 */
	public List<PdbxStructOperList> getPdbxStructOperList();
	
	
	/** Returns the number of available biological assemblies.
	 * 
	 * @return
	 */
	public int getNrBiolAssemblies();
	
	
	/** Does the PDB ID have biological assembly information?
	 * 
	 * @return boolean flag
	 */
	public boolean hasBiolAssembly();
	
	/** get the data for a particular pdbxStructAssembly. We start counting at 0.
	 * 
	 * @param biolAssemblyNr
	 * @return
	 */
	public PdbxStructAssembly getPdbxStructAssembly(int biolAssemblyNr);
	
	
	/** get the data for a particular pdbxStructAssemblyGen. We start counting at 0.
	 * 
	 * @param biolAssemblyNr
	 * @return
	 */
	public PdbxStructAssemblyGen getPdbxStructAssemblyGen(int biolAssemblyNr);
	
	
}
