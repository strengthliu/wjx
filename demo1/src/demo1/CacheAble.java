package demo1;

import java.util.Date;

public interface CacheAble {
	   // 创建缓存时间  
    Date createTime = new Date();   
      
    // 缓存期满时间  
    long expireTime = 1;  
      
    // 缓存实体  
    Object entity = null;   
       
    // 判断缓存是否超时  
    public boolean isExpired() ;
  
    public Date getCreateTime() ;
  
    public void setCreateTime(Date createTime) ;
  
    public Object getEntity() ;
  
    public void setEntity(Object entity) ;
  
    public long getExpireTime() ;
  
    public void setExpireTime(long expireTime) ;

}
