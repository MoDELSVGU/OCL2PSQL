package org.vgu.sqlsi.uml;

public class UMLClass {
	private String name;
	private Boolean isRoleClass;
	private Boolean isUserClass;
	
	
	public Boolean getIsRoleClass() {
		return isRoleClass;
	}

	public void setIsRoleClass(Boolean isRoleClass) {
		this.isRoleClass = isRoleClass;
	}

	public Boolean getIsUserClass() {
		return isUserClass;
	}

	public void setIsUserClass(Boolean isUserClass) {
		this.isUserClass = isUserClass;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
