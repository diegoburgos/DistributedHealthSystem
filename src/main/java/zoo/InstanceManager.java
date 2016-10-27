package zoo;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.Watcher.Event.EventType;

public class InstanceManager extends Component {
	private String imName = null;

	public InstanceManager (String zooUrl, String imName) throws IOException, InterruptedException {
		super(zooUrl);
		this.imName = imName;
	}

	public String getImName () {
		return this.imName;
	}

	public boolean register(String url) {
		final ReentrantLock registration = new ReentrantLock();
		try {
			final String s2 = Constants.PATH_IMS_STATUS + "/"  + imName;
			if (zk.exists(s2, true) == null) {
				zk.create(s2, Constants.NODE_STATUS_STARTING.getBytes(), 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				System.err.println("The KNode " + Constants.PATH_IMS_STATUS + "/"  + imName + " existed before, "
						+ "so this node cannot be created");
			}
			zk.exists(s2, new Watcher() {
				public void process(WatchedEvent event) {
					if(event.getType().equals(EventType.NodeDataChanged) &&
							getData(s2).getDataAsString().equals(Constants.NODE_STATUS_RUNNING)) {
						registration.unlock();
					}
				}
			});
			final String s1 = Constants.PATH_IMS_ACTIVE + "/"  + imName;
			if (zk.exists(s1, true) == null) {
				zk.create(s1, url.getBytes(), 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} else {
				System.err.println("The KNode " + Constants.PATH_IMS_ACTIVE + "/"  + imName + " existed before, "
						+ "so this node cannot be created");
			}
			registration.lock();
		} catch (KeeperException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			registration.unlock();
		}
		return true;
	}
	
	public void shutdown() {
		DataVersion dv = this.getData(Constants.PATH_IMS_STATUS + "/"  + imName);
		try {
			zk.setData(Constants.PATH_IMS_STATUS + "/" + imName, "stopped".getBytes(), dv.getVersion());
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}