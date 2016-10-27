package zoo;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import utils.ListComparator;

import org.apache.zookeeper.ZooKeeper;

public class WatcherIM implements Watcher {
	private ZooKeeper zk;
	private  ZooKeeperConnection zkConn;
	private List<String> imState;

	public WatcherIM (ZooKeeper zk, ZooKeeperConnection zkConn, List<String> imState) {
		this.zk = zk;
		this.zkConn = zkConn;
		this.imState = imState;
	}

	public void process(WatchedEvent event) {
		System.out.println("Wathed event IM of type" + event.getType());
		try {
			if (event.getType().equals(EventType.NodeChildrenChanged)) {
				List<String> newList = zk.getChildren(event.getPath(), false);
				if (this.imState.size() < newList.size()) {
					String item = ListComparator.addedItem(imState, newList);
					System.out.println("Added orchestrator " + item);
					String path = Constants.PATH_IMS_STATUS + "/" + item;
					zkConn.setNewValue(path, Constants.NODE_STATUS_RUNNING, zkConn.getData(path).getVersion());
				} else {
					String item = ListComparator.removedItem(imState, newList);
					System.out.println("Removed orchestrator " + item);
					String path = Constants.PATH_IMS_STATUS + "/" + item;
					zkConn.setNewValue(path, Constants.NODE_STATUS_OFF, zkConn.getData(path).getVersion());
				}
				this.imState = newList;
			}
		} catch (Exception e) {
			System.err.println("Exception in processing the watch. " + e.toString());
		}
		try {
			zk.getChildren(Constants.PATH_IMS_ACTIVE, this);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}