package org.vgu.sqlsi.uml;

public class UMLAttribute {
	private UMLClass source;
	private String name;
	private String type;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UMLClass getSource() {
		return source;
	}

	public void setSource(UMLClass source) {
		this.source = source;
	}

}
