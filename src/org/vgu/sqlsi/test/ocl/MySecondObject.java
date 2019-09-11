package org.vgu.sqlsi.test.ocl;

public class MySecondObject {

    private String street;
    private String city;
    private int zipcode;
    
    

    public MySecondObject(String street, String city, int zipcode) {
	super();
	this.street = street;
	this.city = city;
	this.zipcode = zipcode;
    }

    /**
     * getStreet
     * 
     * @return Returns the street
     */
    public String getStreet() {
	return street;
    }

    /**
     * setStreet
     * 
     * @param street
     *            The street to set.
     */
    public void setStreet(String street) {
	this.street = street;
    }

    /**
     * getCity
     * 
     * @return Returns the city
     */
    public String getCity() {
	return city;
    }

    /**
     * setCity
     * 
     * @param city
     *            The city to set.
     */
    public void setCity(String city) {
	this.city = city;
    }

    /**
     * getZipcode
     * 
     * @return Returns the zipcode
     */
    public int getZipcode() {
	return zipcode;
    }

    /**
     * setZipcode
     * 
     * @param zipcode
     *            The zipcode to set.
     */
    public void setZipcode(int zipcode) {
	this.zipcode = zipcode;
    }

}
