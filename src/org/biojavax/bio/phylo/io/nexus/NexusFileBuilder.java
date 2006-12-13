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
package org.biojavax.bio.phylo.io.nexus;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.seq.io.ParseException;

/**
 * Builds a Nexus file by listening to events.
 * 
 * @author Richard Holland
 * @author Tobias Thierer
 * @author Jim Balhoff
 * @since 1.6
 */
public class NexusFileBuilder extends NexusFileListener.Abstract {

	private NexusFile file;

	private NexusComment comment;

	public void setDefaultBlockParsers() {
		this.setBlockParser(NexusBlockParser.UNKNOWN_BLOCK,
				new UnknownBlockParser());
	}

	protected void blockEnded(final NexusBlockParser blockParser) {
		final NexusBlockListener listener = blockParser.getBlockListener();
		if (listener instanceof NexusBlockBuilder)
			file.addObject(((NexusBlockBuilder) listener).getNexusBlock());
	}

	public void startFile() {
		this.file = new NexusFile();
	}

	public void endFile() {
		// We don't care.
	}

	/**
	 * Obtain the constructed file.
	 * 
	 * @return the constructed file.
	 */
	public NexusFile getNexusFile() {
		return this.file;
	}

	public void beginFileComment() {
		if (this.comment != null)
			this.comment.openSubComment();
		else
			this.comment = new NexusComment();
	}

	public void fileCommentText(String comment) {
		this.comment.addText(comment);
	}

	public void endFileComment() {
		if (this.comment != null && this.comment.hasOpenSubComment())
			this.comment.closeSubComment();
		else {
			this.file.addObject(this.comment);
			this.comment = null;
		}
	}

	private static class UnknownBlockParser extends NexusBlockParser.Abstract {
		private UnknownBlockBuilder builder;

		public UnknownBlockParser() {
			super(new UnknownBlockBuilder());
			this.builder = (UnknownBlockBuilder) this.getBlockListener();
		}

		public void parseToken(final String token) throws ParseException {
			this.builder.getComponents().add(token);
		}

		private static class UnknownBlockBuilder extends
				NexusBlockBuilder.Abstract {

			private UnknownBlock block;

			private List getComponents() {
				return this.block.getComponents();
			}

			public void endTokenGroup() {
				// Only write not-first, as we also receive the one
				// from after the BEGIN statement.
				if (this.getComponents().size() > 0)
					this.getComponents().add(";");
			}

			public void endBlock() {
				// We don't care.
			}

			public void addComment(NexusComment comment) {
				this.getComponents().add(comment);
			}

			public NexusBlock startBlockObject() {
				this.block = new UnknownBlock(this.getBlockName());
				return this.block;
			}

			private static class UnknownBlock extends NexusBlock.Abstract {

				private List components = new ArrayList();

				/**
				 * Construct a new unknown block.
				 * 
				 * @param blockName
				 *            the name of it.
				 */
				public UnknownBlock(String blockName) {
					super(blockName);
				}

				private List getComponents() {
					return this.components;
				}

				public void writeBlockContents(final Writer writer)
						throws IOException {
					for (final Iterator i = this.components.iterator(); i
							.hasNext();) {
						final Object obj = (Object) i.next();
						if (obj instanceof NexusComment)
							((NexusComment) obj).writeObject(writer);
						else {
							String text = (String) obj;
							text = text.replaceAll("'", "''");
							if (text.trim().length() > 0)
								text = text.replaceAll(" ", "_");
							if (text.indexOf('[') >= 0
									|| text.indexOf(']') >= 0)
								text = "'" + text + "'";
							writer.write(text);
						}
					}
				}
			}
		}
	}
}
