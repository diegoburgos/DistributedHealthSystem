package zoo;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.Watcher.Event.EventType;

public class Orchestrator extends Component {
	private String orchName;
	boolean started = false;

	public Orchestrator (String zooUrl, String orchName) {
		super(zooUrl);
		this.orchName = orchName;
	}

	public boolean register(String url) throws InterruptedException {
		final ReentrantLock registration = new ReentrantLock();
		try {
			final String s2 = Constants.PATH_ORCHESTRATOR_STATUS + "/" + orchName;
			if (zk.exists(s2, true) == null) {
				zk.create(s2, "starting".getBytes(), 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				System.err.println(s2 + " existed before, so this node cannot be created");
			}
			zk.exists(s2, new Watcher() {
				public void process(WatchedEvent event) {
					if(event.getType().equals(EventType.NodeDataChanged) &&
							getData(s2).getDataAsString().equals(Constants.NODE_STATUS_RUNNING)) {
						registration.unlock();
					}
				}
			});
			final String s1 = Constants.PATH_ORCHESTRATOR_ACTIVE + "/"  + orchName;
			if (zk.exists(s1, true) == null) {
				zk.create(s1, url.getBytes(), 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} else {
				System.err.println(s1 + " existed before, so this node cannot be created");
			}
			registration.lock();
		} catch (KeeperException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			registration.unlock();
		}
		return true;
	}
	
	public String getOrchName() {
		return orchName;
	}
	
	public void shutdown() {
		DataVersion dv = this.getData(Constants.PATH_ORCHESTRATOR_STATUS + "/" + orchName);
		try {
			zk.setData(Constants.PATH_ORCHESTRATOR_STATUS + "/" + orchName, "stopped".getBytes(), dv.getVersion());
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}