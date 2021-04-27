package com.scd.model;

public class ClientInfo {
	
	private String aesKey;
	
	private String picData;
	
	private String userInfo;
	
	private String clientPub;

	private String fileType;

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getPicData() {
		return picData;
	}

	public void setPicData(String picData) {
		this.picData = picData;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getClientPub() {
		return clientPub;
	}

	public void setClientPub(String clientPub) {
		this.clientPub = clientPub;
	}

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientInfo)) return false;

        ClientInfo that = (ClientInfo) o;

        if (!getAesKey().equals(that.getAesKey())) return false;
        if (!getPicData().equals(that.getPicData())) return false;
        if (!getUserInfo().equals(that.getUserInfo())) return false;
        if (!getClientPub().equals(that.getClientPub())) return false;
        return getFileType().equals(that.getFileType());
    }

    @Override
    public int hashCode() {
        int result = getAesKey().hashCode();
        result = 31 * result + getPicData().hashCode();
        result = 31 * result + getUserInfo().hashCode();
        result = 31 * result + getClientPub().hashCode();
        result = 31 * result + getFileType().hashCode();
        return result;
    }
}
