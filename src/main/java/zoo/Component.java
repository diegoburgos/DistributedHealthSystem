package zoo;

import java.io.IOException;

public class Component extends ZooKeeperConnection {
	
	public Component (String zoo) {
		try {
			this.connect(zoo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
