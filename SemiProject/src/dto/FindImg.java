package dto;

public class FindImg {
	
	private int imageNo;
	private int findNo;
	private String originImg;
	private String storedImg;
	
	
	@Override
	public String toString() {
		return "FimdImg [imageNum=" + imageNo + ", findNo=" + findNo + ", originImg=" + originImg + ", storedImg="
				+ storedImg + "]";
	}
	
	
	public int getImgNum() {
		return imageNo;
	}
	public void setImgNum(int imgNum) {
		this.imageNo = imgNum;
	}
	public int getFindNo() {
		return findNo;
	}
	public void setFindNo(int findNo) {
		this.findNo = findNo;
	}
	public String getOriginImg() {
		return originImg;
	}
	public void setOriginImg(String originImg) {
		this.originImg = originImg;
	}
	public String getStoredImg() {
		return storedImg;
	}
	public void setStoredImg(String storedImg) {
		this.storedImg = storedImg;
	}
	
}
