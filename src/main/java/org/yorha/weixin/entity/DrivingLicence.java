package org.yorha.weixin.entity;

public class DrivingLicence {
	/*
	 * 姓名
	 */
	private String name;
	/*
	 * 驾驶证号
	 */
	private String num;
	/*
	 * 准驾车型
	 */
	private String vehicleType;
	/*
	 * 有效期开始时间
	 */
	private String startDate;
	/*
	 * 有效期结束时间
	 */
	private String endDate;
	/*
	 * 初次领证时间
	 */
	private String issueDate;
	/*
	 * 地址
	 */
	private String addr;
	/*
	 * 性别
	 */
	private String sex;
	/*
	 * 首页结果状态
	 */
	private boolean  faceSuccess;
	/*
	 * 档案编号
	 */
	private String archiveNo;
	/*
	 * 副页结果状态
	 */
	private boolean backSuccess;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public boolean isFaceSuccess() {
		return faceSuccess;
	}
	public void setFaceSuccess(boolean faceSuccess) {
		this.faceSuccess = faceSuccess;
	}
	public String getArchiveNo() {
		return archiveNo;
	}
	public void setArchiveNo(String archiveNo) {
		this.archiveNo = archiveNo;
	}
	public boolean isBackSuccess() {
		return backSuccess;
	}
	public void setBackSuccess(boolean backSuccess) {
		this.backSuccess = backSuccess;
	}
	
}
