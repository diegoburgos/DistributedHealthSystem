package zoo;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperConnection {

	// Declare zookeeper instance to access ZooKeeper ensemble
	protected ZooKeeper zk;
	final CountDownLatch connectedSignal = new CountDownLatch(1);

	// Method to connect zookeeper ensemble.
	public ZooKeeper connect(String host) throws IOException,InterruptedException {
		zk = new ZooKeeper(host,5000,new Watcher() {
			public void process(WatchedEvent we) {
				if (we.getState() == KeeperState.SyncConnected) {
					connectedSignal.countDown();
				}
			}
		});

		connectedSignal.await();
		return zk;
	}

	// Method to disconnect from zookeeper server
	public void close() throws InterruptedException {
		zk.close();
	}

	public boolean createValueInZK (String path, String value) throws KeeperException, InterruptedException {
		if (zk.exists(path, true) == null) {
			zk.create(path, value.getBytes(), 
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			return true;
		}
		return false;
	}

	public DataVersion getData (String path) {
		DataVersion ret = null;
		try {
			Stat st = new Stat();
			ret = new DataVersion (zk.getData(path, false, st), st.getVersion());
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public void setNewValue(String path, String value, int version) {
		try {
			zk.setData(path, value.getBytes(), version);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Stat znode_exists(String path) throws 
	KeeperException,InterruptedException {
		return zk.exists(path,true);
	}
}