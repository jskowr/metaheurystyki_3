package zad_3;

public class Configuration {

	private String[] fileNames;
	private int filesCount;
	private int iter;
	private int n_size;
	private int runs;
	private boolean isSwap;
	private boolean isInversion;
	private double startTemperature;
	private double alpha;

	public Configuration() {
		filesCount = 1;

		fileNames = new String[this.filesCount];
//		this.fileNames[0] = "A-n32-k5";
//		this.fileNames[0] = "A-n37-k6";
//		this.fileNames[0] = "A-n39-k5";
//		this.fileNames[0] = "A-n45-k6";
//		this.fileNames[0] = "A-n48-k7";
//		this.fileNames[0] = "A-n54-k7";
		this.fileNames[0] = "A-n60-k9";

		iter = 20000;
		n_size = 120;
		runs = 10;
		isSwap = false;
		isInversion = true;
		startTemperature = 5000;
		alpha = 0.9995;
	}

	public double getStartTemperature() {
		return startTemperature;
	}

	public void setStartTemperature(double startTemperature) {
		this.startTemperature = startTemperature;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public boolean isSwap() {
		return isSwap;
	}

	public void setSwap(boolean isSwap) {
		this.isSwap = isSwap;
	}

	public boolean isInversion() {
		return isInversion;
	}

	public void setInversion(boolean isInversion) {
		this.isInversion = isInversion;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public int getFilesCount() {
		return filesCount;
	}

	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}

	public int getIter() {
		return iter;
	}

	public void setIter(int iter) {
		this.iter = iter;
	}

	public int getN_size() {
		return n_size;
	}

	public void setN_size(int n_size) {
		this.n_size = n_size;
	}
}
