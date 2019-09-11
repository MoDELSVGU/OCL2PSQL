/**
 * 
 */
package org.vgu.sqlsi.test.ocl;

import java.util.HashSet;
import java.util.Set;

public final class MyObject {

    public static String myStaticText = "itShouldWork";
    
    private String firstname;
    private String lastname;
    private int age;
    private int children;
    private double income;
    private double outcome;
    private boolean female;
    private boolean male;
    private Object anything;
    private MySecondObject mainAddress;
    private Set<MySecondObject> addresses = new HashSet<MySecondObject>();

    public MyObject() {
    }

    public MyObject(String firstname, String lastname) {
	super();
	this.firstname = firstname;
	this.lastname = lastname;
    }

    public String getFirstname() {
	return firstname;
    }

    public String getLastname() {
	return lastname;
    }

    public String greetings(String welcome) {
	return welcome + " " + firstname + " " + lastname;
    }

    public Integer getAge() {
	return age;
    }

    public void setAge(int age) {
	this.age = age;
    }

    public int getChildren() {
	return children;
    }

    public void setChildren(int children) {
	this.children = children;
    }

    public double getIncome() {
	return income;
    }

    public double getOutcome() {
	return outcome;
    }

    public void setIncome(double income) {
	this.income = income;
    }

    public void setOutcome(double outcome) {
	this.outcome = outcome;
    }

    public boolean isFemale() {
	return female;
    }

    public boolean isMale() {
	return male;
    }

    public void setFemale(boolean female) {
	this.female = female;
    }

    public void setMale(boolean male) {
	this.male = male;
    }

    public Object getAnything() {
	return anything;
    }
    
    public void someError() throws MyException {
	throw new MyException();
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder("TestObject:");
	builder.append("\nfirstname: ").append(firstname);
	builder.append("\nlastname: ").append(lastname);
	builder.append("\nage: ").append(age);
	builder.append("\nchildren: ").append(children);
	builder.append("\nincome: ").append(income);
	builder.append("\noutcome: ").append(outcome);
	builder.append("\nmale: ").append(male);
	builder.append("\nfemale: ").append(female);
	builder.append("\nanything: ").append(anything);
	return builder.toString();
    }

    /**
     * getMainAddress
     *
     * @return Returns the mainAddress
     */
    public MySecondObject getMainAddress() {
        return mainAddress;
    }

    /**
     * setMainAddress
     *
     * @param mainAddress The mainAddress to set.
     */
    public void setMainAddress(MySecondObject mainAddress) {
        this.mainAddress = mainAddress;
    }
    
    public void addAddress(MySecondObject address) {
	this.addresses.add(address);
    }
    
    public void removeAddress(MySecondObject address) {
	this.addresses.remove(address);
    }
    
    public Set<MySecondObject> getAddresses() {
	return addresses;
    }

    public static String myStaticText() {
	return "should be working!";
    }
}