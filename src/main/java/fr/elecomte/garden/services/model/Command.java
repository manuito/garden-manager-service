package fr.elecomte.garden.services.model;

/**
 * @author elecomte
 * @since v0.0.3
 * @version 1
 */
public class Command {

	private String nodeId;

	private String command;

	private boolean hasOther;

	/**
	 * 
	 */
	public Command() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return this.nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the hasOther
	 */
	public boolean isHasOther() {
		return this.hasOther;
	}

	/**
	 * @param hasOther
	 *            the hasOther to set
	 */
	public void setHasOther(boolean hasOther) {
		this.hasOther = hasOther;
	}

}
