package zoo;

public class Component extends ZookeeperConnection {
	
	public Component (String zoo) {
		this.connect(zoo);
	}
	
}
