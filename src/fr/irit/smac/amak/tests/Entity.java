package fr.irit.smac.amak.tests;

public class Entity {

	private int age = 22;
	private String firstname = "Michael";
	
	public Entity(int age, String firstname){
		this.age = age;
		this.firstname = firstname;
	}
	public Entity(){
		
	}

	public int getAge() {
		return age;
	}

	public void setAge( int age ) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname( String firstname ) {
		this.firstname = firstname;
	}
}