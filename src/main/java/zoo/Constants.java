package zoo;

public class Constants {
	public static final String ZOO_URL = "localhost:2181";
	
	public static final String PATH_APP_NAME = "/application";

	public static final String PATH_ORCHESTRATOR = PATH_APP_NAME + "/orchestrator";
	public static final String PATH_ORCHESTRATOR_ACTIVE = PATH_ORCHESTRATOR + "/activeNodes";
	public static final String PATH_ORCHESTRATOR_STATUS = PATH_ORCHESTRATOR + "/status";
	
	public static final String PATH_IMS = PATH_APP_NAME + "/ims";
	public static final String PATH_IMS_ACTIVE = PATH_IMS + "/activeNodes";
	public static final String PATH_IMS_STATUS = PATH_IMS + "/status";

	public static final String NODE_STATUS_STARTING = "starting";
	public static final String NODE_STATUS_RUNNING = "running";
	public static final String NODE_STATUS_OFF = "off";
	
	public static final String PATH_QUERY_DEPLOYMENT = PATH_APP_NAME + "/queries";
}