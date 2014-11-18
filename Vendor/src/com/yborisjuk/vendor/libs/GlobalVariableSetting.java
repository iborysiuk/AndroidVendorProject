package com.yborisjuk.vendor.libs;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalVariableSetting extends Application {

	// Global variables for users
	private static String uid;
	private static String name;
	private static String email;
	private static String password;
	private static String city;
	private static String image;
	private static Bitmap bitmap;

	// Global variables for vendors
	private String vUID;
	private String vName;
	private String vEmail;
	private String vPhonenumber;
	private String vCountry;
	private String vCity;
	private String vAddress;
	private String vPostalCode;
	private String vImgLink;
	private String vWorkTime;

	// Global variables arrays for vendors
	private static String[] array_vUID;
	private static String[] array_vName;
	private static String[] array_vEmail;
	private static String[] array_vPhone;
	private static String[] array_vCountry;
	private static String[] array_vCity;
	private static String[] array_vAddress;
	private static String[] array_vPostalCode;
	private static String[] array_vImgLink;
	private static String[] array_vWorkTime;

	// Global variables for slider
	//private String sImgName;
	private String sImgLink;

	//private static String[] array_sImgName;
	private static String[] array_sImgLink;

	
	public GlobalVariableSetting() {
		// TODO Auto-generated constructor stub
	}

	public GlobalVariableSetting(String email, String password) {
		GlobalVariableSetting.email = email;
		GlobalVariableSetting.password = password;
	}

	public GlobalVariableSetting(String name, String email, String password,
			String city) {
		GlobalVariableSetting.name = name;
		GlobalVariableSetting.email = email;
		GlobalVariableSetting.password = password;
		GlobalVariableSetting.city = city;
	}

	public GlobalVariableSetting(String uid, String name, String email,
			String tel, String country, String city, String address,
			String postalCode, String imgLink, String vWorkTime) {
		super();
		this.vUID = uid;
		this.vName = name;
		this.vEmail = email;
		this.vPhonenumber = tel;
		this.vCountry = country;
		this.vCity = city;
		this.vAddress = address;
		this.vPostalCode = postalCode;
		this.vImgLink = imgLink;
		this.vWorkTime = vWorkTime;
	}

	public GlobalVariableSetting(String[] array_vUID, String[] array_vName,
			String[] array_vEmail, String[] array_vPhone,
			String[] array_vCountry, String[] array_vCity,
			String[] array_vAddress, String[] array_vPostalCode,
			String[] array_vImgLink, String[] array_vWorkTime) {

		GlobalVariableSetting.array_vUID = array_vUID;
		GlobalVariableSetting.array_vName = array_vName;
		GlobalVariableSetting.array_vEmail = array_vEmail;
		GlobalVariableSetting.array_vPhone = array_vPhone;
		GlobalVariableSetting.array_vCountry = array_vCountry;
		GlobalVariableSetting.array_vCity = array_vCity;
		GlobalVariableSetting.array_vAddress = array_vAddress;
		GlobalVariableSetting.array_vPostalCode = array_vPostalCode;
		GlobalVariableSetting.array_vImgLink = array_vImgLink;
		GlobalVariableSetting.array_vWorkTime = array_vWorkTime;
	}

	public GlobalVariableSetting(String sImgLink) {
		super();
		this.sImgLink = sImgLink;
	}
	
	public GlobalVariableSetting(String[] array_sImgLink) {

		GlobalVariableSetting.array_sImgLink = array_sImgLink;
	}
		
	public String getUID() {
		return uid;
	}

	public void setUID(String uid) {
		GlobalVariableSetting.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		GlobalVariableSetting.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		GlobalVariableSetting.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		GlobalVariableSetting.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		GlobalVariableSetting.city = city;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		GlobalVariableSetting.image = image;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		GlobalVariableSetting.bitmap = bitmap;
	}

	/**
	 * 
	 * @return
	 */
	public String getvUID() {
		return vUID;
	}

	public String getvName() {
		return vName;
	}

	public String getvEmail() {
		return vEmail;
	}

	public String getvPhonenumber() {
		return vPhonenumber;
	}

	public String getvCountry() {
		return vCountry;
	}

	public String getvCity() {
		return vCity;
	}

	public String getvAddress() {
		return vAddress;
	}

	public String getvPostalCode() {
		return vPostalCode;
	}

	public String getvImgLink() {
		return vImgLink;
	}

	public String getvWorkTime() {
		return vWorkTime;
	}

	/**
	 * Set global vendor's arrays
	 * 
	 * @return
	 */

	public String[] getArray_vUID() {
		return array_vUID;
	}
	
	public String[] getArray_vName() {
		return array_vName;
	}

	public String[] getArray_vEmail() {
		return array_vEmail;
	}

	public String[] getArray_vPhone() {
		return array_vPhone;
	}

	public String[] getArray_vCountry() {
		return array_vCountry;
	}

	public String[] getArray_vCity() {
		return array_vCity;
	}

	public String[] getArray_vAddress() {
		return array_vAddress;
	}

	public String[] getArray_vPostalCode() {
		return array_vPostalCode;
	}

	public String[] getArray_vImgLink() {
		return array_vImgLink;
	}

	public String[] getArray_vWorkTime() {
		return array_vWorkTime;
	}
	
	/**
	 * 
	 */
	
	/*public String getsImgName() {
		return sImgName;
	}
	
	public String[] getArray_sImgName() {
		return array_sImgName;
	}*/
	

	public String getsImgLink() {
		return sImgLink;
	}
	
	public String[] getArray_sImgLink() {
		return array_sImgLink;
	}
	
}
