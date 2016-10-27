package zoo;

import java.io.Serializable;

public class ProcessInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String processName, ip, status;

	public ProcessInfo(String processName, String ip, String status) {
		super();
		this.processName = processName;
		this.ip = ip;
		this.status = status;
	}

	public String getProcessName() {
		return processName;
	}
	public String getIp() {
		return ip;
	}
	public String getStatus() {
		return status;
	}
}
