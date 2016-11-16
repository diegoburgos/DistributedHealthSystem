package zoo;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import utils.ListComparator;

public class WatcherOrch implements Watcher {
	private ZooKeeper zk;
	private ZookeeperConnection zkConn;
	private List<String> orcState;

	public WatcherOrch (ZooKeeper zk, ZookeeperConnection zkConn, List<String> orcState) {
		this.zk = zk;
		this.zkConn = zkConn;
		this.orcState = orcState;
	}

	public void process(WatchedEvent event) {
		System.out.println("Wathed event Orchestrator of type" + event.getType());
		try {
			if (event.getType().equals(EventType.NodeChildrenChanged)) {
				List<String> newList = zk.getChildren(event.getPath(), false);
				if (this.orcState.size() < newList.size()) {
					String item = ListComparator.addedItem(orcState, newList);
					System.out.println("\tAdded orchestrator " + item);
					String path = Constants.PATH_ORCHESTRATOR_STATUS + "/" + item;
					zkConn.setNewValue(path, Constants.NODE_STATUS_RUNNING, zkConn.getData(path).getVersion());
				} else {
					String item = ListComparator.removedItem(orcState, newList);
					System.out.println("\tRemoved orchestrator " + item);
					String path = Constants.PATH_ORCHESTRATOR_STATUS + "/" + item;
					zkConn.setNewValue(path, Constants.NODE_STATUS_OFF, zkConn.getData(path).getVersion());
				}
				this.orcState = newList;
			}
		} catch (Exception e) {
			System.err.println("Exception in processing the watch. " + e.toString());
		}
		try {
			zk.getChildren(Constants.PATH_ORCHESTRATOR_ACTIVE, this);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
