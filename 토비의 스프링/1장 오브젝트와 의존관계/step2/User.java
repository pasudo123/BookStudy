package tobi_spring.chapter1.step2;

public class User {
	String id;
	String name;
	String password;
	
	// DAO
	// Data Access Object 
	// DB 를 사용해 데이터를 조회하거나 조작하는 기능을 전담하도록 만든 오브젝트
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
