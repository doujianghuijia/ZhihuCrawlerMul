package version1.com.tangcheng.main;

import org.apache.ibatis.session.SqlSession;
import version1.com.tangcheng.mapper.UserInfoMapper;
import version1.com.tangcheng.model.UserInfo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tc on 2016/5/4.
 */
public class GetFollowersThread implements Runnable {
    private  List<UserInfo> userInfoList;
    private int deep;

    public GetFollowersThread() {
    }

    public GetFollowersThread(List<UserInfo> userInfoList,int deep) {
        this.userInfoList = userInfoList;
        this.deep = deep;
    }

    @Override
    public void run() {
        if(userInfoList != null){
            for(Iterator<UserInfo> infoIterator = userInfoList.iterator();infoIterator.hasNext();) {
                getEachFollow(infoIterator);
            }
        }
    }

    public synchronized void getEachFollow(Iterator<UserInfo> infoIterator){
            UserInfo userInfo = infoIterator.next();
            System.out.println("----"+Thread.currentThread()+"---"+userInfo.getUserName()+"的fo");
            List<String> followers = Test.getFollowers(userInfo);
            infoIterator.remove();
            for(String name:followers){
                UserInfo userInfo1 = new UserInfo();
                userInfo1.setDeepLength(deep);
                userInfo1.setUserName(name);
                SqlSession sqlSession1 = null;
                try {
                    sqlSession1 = Test.getSqlSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //互相关注，这样replace出现问题,之前的sqlsession没有commit
                System.out.println("-线程"+Thread.currentThread()+"--正在保存"+userInfo1.getUserName()+"的用户名和深度----");
                sqlSession1.getMapper(UserInfoMapper.class).addUserName(userInfo1);
                sqlSession1.commit();
            }
        }
}
