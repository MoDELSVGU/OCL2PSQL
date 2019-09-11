package org.vgu.sqlsi.uml;

import java.util.ArrayList;
import java.util.List;

public class UMLContextBis {
	private List<UMLClass> classes = new ArrayList<UMLClass>();
	private List<UMLGeneralization> generalizations = new ArrayList<UMLGeneralization>();
	private List<UMLAttribute> attributes = new ArrayList<UMLAttribute>();
	private List<UMLAssociationEnd> associationends = new ArrayList<UMLAssociationEnd>(); 
	private List<UMLAssociation> associations = new ArrayList<UMLAssociation>(); 
	private List<UMLMultiplicity> multiplicities = new ArrayList<UMLMultiplicity>(); 
	private UMLClass userClass;
	private UMLClass roleClass;
	
	public UMLClass getUserClass() {
		return userClass;
	}
	public void setUserClass(UMLClass userClass) {
		this.userClass = userClass;
	}
	public UMLClass getRoleClass() {
		return roleClass;
	}
	public void setRoleClass(UMLClass roleClass) {
		this.roleClass = roleClass;
	}
	public List<UMLClass> getClasses() {
		return classes;
	}
	public void setClasses(List<UMLClass> classes) {
		this.classes = classes;
	}
	public List<UMLAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<UMLAttribute> attributes) {
		this.attributes = attributes;
	}
	public List<UMLAssociation> getAssociations() {
		return associations;
	}
	public void setAssociations(List<UMLAssociation> associations) {
		this.associations = associations;
	}
	
	public boolean isEnum(String name) {
		return false;
	}
	
	public boolean isType(String name) {
		boolean found = false;
		for (UMLClass umlclass : this.getClasses()) {
			if (umlclass.getName().equals(name)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public String getFullName(String name) {
		return name;
	}
	
	public boolean isAttribute(String name) {
		boolean found = false;
		for (UMLAttribute umlattribute : this.getAttributes()) {
			if (umlattribute.getName().equals(name)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public boolean isAssociationEnd(String name) {
		boolean found = false;
		for (UMLAssociationEnd umlassociationend : this.getAssociationends()) {
			if (umlassociationend.getName().equals(name)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	
	public List<String> getSubsorts(String name) {
		List<String> result = new ArrayList<String>();
		for (UMLGeneralization umlgeneralization : this.getGeneralizations()) {
			if (umlgeneralization.getSuperclass().getName().equals(name)) {
				result.add(umlgeneralization.getSubclass().getName());
			}
		}
		return result;
	}
	
	public boolean areBothEndsMulti(UMLAssociation umlassociation) {
		if (this.isMulti(umlassociation.getSource())
				&& this.isMulti(umlassociation.getTarget())) {
			return true;
		} else {
			return false;
		}
		
	}
	public boolean isMulti(UMLAssociationEnd associationend) {
		boolean result = false;
		for(UMLMultiplicity umlmultiplicity : this.getMultiplicities()) {
			if (umlmultiplicity.getAssociationend().getName().equals(associationend.getName())) {
				if (umlmultiplicity.getMultiplicity() == Multiplicity.Multi) {
					result = true;
					break;
				}
			}	
		}
		return result;
	}
	
	public boolean areBothEndsAtMostOne(UMLAssociation umlassociation) {
		if (this.isAtMostOne(umlassociation.getSource())
				&& this.isAtMostOne(umlassociation.getTarget())) {
			return true;
		} else {
			return false;
		}
		
	}
	public boolean isAtMostOne(UMLAssociationEnd associationend) {
		boolean result = false;
		for(UMLMultiplicity umlmultiplicity : this.getMultiplicities()) {
			if (umlmultiplicity.getAssociationend().getName().equals(associationend.getName())) {
				if (umlmultiplicity.getMultiplicity() == Multiplicity.AtMostOne) {
					result = true;
					break;
				}
			}	
		}
		return result;
	}
	
	public boolean areBothEndsMixed(UMLAssociation umlassociation) {
		if (this.isMulti(umlassociation.getSource())
				&& this.isAtMostOne(umlassociation.getTarget())) {
			return true;
		} else if (this.isMulti(umlassociation.getTarget())
				&& this.isAtMostOne(umlassociation.getSource())){
			return true;
		} else {
			return false;
		}
		
	}
	
	public List<UMLGeneralization> getGeneralizations() {
		return generalizations;
	}
	public void setGeneralizations(List<UMLGeneralization> generalizations) {
		this.generalizations = generalizations;
	}
	public List<UMLAssociationEnd> getAssociationends() {
		return associationends;
	}
	public void setAssociationends(List<UMLAssociationEnd> associationends) {
		this.associationends = associationends;
	}
	public List<UMLMultiplicity> getMultiplicities() {
		return multiplicities;
	}
	public void setMultiplicities(List<UMLMultiplicity> multiplicities) {
		this.multiplicities = multiplicities;
	}


	}
	
	


