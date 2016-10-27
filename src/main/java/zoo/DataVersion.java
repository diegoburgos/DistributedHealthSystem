package zoo;

import java.util.Arrays;

class DataVersion {
	private byte data[];
	private int version;

	public DataVersion (byte[] data, int version) {
		this.data = data;
		this.version = version;
	}

	public String getDataAsString() {
		return new String(data);
	}

	public int getDataAsInt() {
		return Integer.parseInt(getDataAsString());
	}

	public int getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "DataVersion [data=" + Arrays.toString(data) + ", version=" + version + "]";
	}
}