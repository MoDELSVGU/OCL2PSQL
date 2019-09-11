package org.vgu.sqlsi.uml;

public class UMLAssociationEnd {
	
	private String name;
	private UMLClass source;
	private UMLClass target;
	private UMLMultiplicity multiplicity;
	
	public UMLMultiplicity getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(UMLMultiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}
	public String getOpposite() {
		return opposite;
	}
	public void setOpposite(String opposite) {
		this.opposite = opposite;
	}
	private String opposite;
	
	public UMLClass getSource() {
		return source;
	}
	public void setSource(UMLClass source) {
		this.source = source;
	}
	public UMLClass getTarget() {
		return target;
	}
	public void setTarget(UMLClass target) {
		this.target = target;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
