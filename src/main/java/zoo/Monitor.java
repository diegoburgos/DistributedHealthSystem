package zoo;

import java.util.LinkedList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

public class Monitor extends ZookeeperConnection {
	private List<String> orcChilds = new LinkedList<String>();
	private List<String> imChilds = new LinkedList<String>();

	public Monitor (String zooUrl) {
		this.connect(zooUrl);
	}

	public boolean start() {
		try {
			if (zk.exists(Constants.PATH_APP_NAME, true) == null) {
				zk.create(Constants.PATH_APP_NAME, "MyApp".getBytes(), 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				
				zk.create(Constants.PATH_QUERY_DEPLOYMENT, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create(Constants.PATH_ORCHESTRATOR, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create(Constants.PATH_ORCHESTRATOR_ACTIVE, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create(Constants.PATH_ORCHESTRATOR_STATUS, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				
				zk.create(Constants.PATH_IMS, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create(Constants.PATH_IMS_ACTIVE, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create(Constants.PATH_IMS_STATUS, null, 
						ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				return false;
			}
		} catch (KeeperException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(2);
		}
		this.createIMWatcher();
		this.createOrchWatcher();
		return true;
	}
	
	private void createOrchWatcher () {
		WatcherOrch w = new WatcherOrch(zk,this, orcChilds);
		try {
			orcChilds = zk.getChildren(Constants.PATH_ORCHESTRATOR_ACTIVE, w);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void createIMWatcher () {
		WatcherIM w = new WatcherIM(zk, this, imChilds);
		try {
			imChilds = zk.getChildren(Constants.PATH_IMS_ACTIVE, w);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
