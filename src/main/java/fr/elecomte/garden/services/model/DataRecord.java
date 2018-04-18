package fr.elecomte.garden.services.model;

/**
 * @author elecomte
 * @since v0.0.3
 * @version 1
 */
public class DataRecord {

	private String nodeId;

	private String payload;

	/**
	 * 
	 */
	public DataRecord() {
		super();
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
	 * @return the payload
	 */
	public String getPayload() {
		return this.payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
