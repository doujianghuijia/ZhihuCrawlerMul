package version1.com.tangcheng.main;

import org.apache.ibatis.session.SqlSession;
import version1.com.tangcheng.mapper.UserInfoMapper;
import version1.com.tangcheng.model.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tc on 2016/5/4.
 */
public class GetdetailThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int deep=1;deep<4;deep++){
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
            try {
                userInfoList = Test.getSqlSession().getMapper(UserInfoMapper.class).selectUserInfoBydeepLength(deep-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(UserInfo userInfo:userInfoList){
                String url = "https://www.zhihu.com/people/"+userInfo.getUserName();
                UserInfo userInfo1 = Test.getUserInfoDetail(url, userInfo.getUserName());
                System.out.println("&&&&&&"+userInfo1);
                SqlSession sqlSession1 = null;
                try {
                    sqlSession1 = Test.getSqlSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("*****正在保存"+userInfo1.getUserName()+"的详细信息******");
                sqlSession1.getMapper(UserInfoMapper.class).updateUserInfo(userInfo1);
                sqlSession1.commit();
            }
        }
    }
}
