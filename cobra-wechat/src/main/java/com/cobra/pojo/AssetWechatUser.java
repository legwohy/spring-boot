package com.cobra.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:  
 * @Description: 
 * @author administrator
 * @date - 2018年11月06日 15时14分37秒
 */
public class AssetWechatUser implements Serializable {
    private static final long serialVersionUID = 3148176768559230877L;
    

	/** 
	 *  @Fields Id : 主键Id
	 * 
	 * */
	private Integer id;
	/** 
	 *  @Fields UserId : 借款用户Id
	 * 
	 * */
	private Integer userId;
	/** 
	 *  @Fields OpenId : 用户OpenId
	 * 
	 * */
	private String openId;
	/** 
	 *  @Fields UserAccount : 用户名
	 * 
	 * */
	private String userAccount;
	/** 
	 *  @Fields UserPhone : 用户手机号
	 * 
	 * */
	private String userPhone;
	/** 
	 *  @Fields PhotoPath : 头像信息
	 * 
	 * */
	private String photoPath;
	/** 
	 *  @Fields Gender : 性别
	 * 
	 * */
	private String gender;
	/** 
	 *  @Fields NickName : 微信名称
	 * 
	 * */
	private String nickName;
	/** 
	 *  @Fields CreateTime : 认证时间
	 * 
	 * */
	private Date createTime;
	/** 
	 *  @Fields UpdateTime : 修改时间
	 * 
	 * */
	private Date updateTime;

	/**
	 * 认证状态 0 未认证 1 已认证
	 */
	private Integer status;

	/**
	 * 认证时间
	 */
	private Date authTime;

	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getOpenId() {
		return this.openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getUserAccount() {
		return this.userAccount;
	}
	
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	public String getUserPhone() {
		return this.userPhone;
	}
	
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	public String getPhotoPath() {
		return this.photoPath;
	}
	
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getNickName() {
		return this.nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}	
	
	public Date getUpdateTime() {
		return this.updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Date getAuthTime()
	{
		return authTime;
	}

	public void setAuthTime(Date authTime)
	{
		this.authTime = authTime;
	}

	public AssetWechatUser() {
		
	}

	public AssetWechatUser(Integer id ,Integer userId ,String openId ,String userAccount ,String userPhone ,String photoPath ,String gender ,String nickName ,Date createTime ,Date updateTime ){
	super();
	this.id=id;
	this.userId=userId;
	this.openId=openId;
	this.userAccount=userAccount;
	this.userPhone=userPhone;
	this.photoPath=photoPath;
	this.gender=gender;
	this.nickName=nickName;
	this.createTime=createTime;
	this.updateTime=updateTime;
	}

	public AssetWechatUser(Integer id, Integer userId, String openId, String userAccount, String userPhone, String photoPath, String gender, String nickName, Date createTime, Date updateTime, Integer status,
					Date authTime)
	{
		this.id = id;
		this.userId = userId;
		this.openId = openId;
		this.userAccount = userAccount;
		this.userPhone = userPhone;
		this.photoPath = photoPath;
		this.gender = gender;
		this.nickName = nickName;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.status = status;
		this.authTime = authTime;
	}

	@Override
	public String toString()
	{
		return "AssetWechatUser{" +
						"id=" + id +
						", userId=" + userId +
						", openId='" + openId + '\'' +
						", userAccount='" + userAccount + '\'' +
						", userPhone='" + userPhone + '\'' +
						", photoPath='" + photoPath + '\'' +
						", gender='" + gender + '\'' +
						", nickName='" + nickName + '\'' +
						", createTime=" + createTime +
						", updateTime=" + updateTime +
						", status=" + status +
						", authTime=" + authTime +
						'}';
	}
}

