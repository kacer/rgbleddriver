package rgbleddriver.model;

import java.util.UUID;

public class UserConfig {
	
	private UUID id;
	private String name;
	private String mac;
	private String color;
	
	public UserConfig() {}
	
	public UserConfig(UUID id, String name, String mac, String color) {
		this.id = id;
		this.name = name;
		this.mac = mac;
		this.color = color;
	}

	public UserConfig(String name, String mac, String color) {
		id = UUID.randomUUID();
		this.name = name;
		this.mac = mac;
		this.color = color;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "UserConfig [id=" + id + ", name=" + name + ", mac=" + mac + ", color=" + color + "]";
	}

}
