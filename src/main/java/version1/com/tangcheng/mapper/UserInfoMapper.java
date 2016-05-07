package version1.com.tangcheng.mapper;

import version1.com.tangcheng.model.UserInfo;

import java.util.List;

/**
 * Created by tc on 2016/5/3.
 */
public interface UserInfoMapper {
    public void updateUserInfo(UserInfo userInfo);
    public List<UserInfo> selectUserInfoBydeepLength(int deepLength);
    public void addUserName(UserInfo userInfo);
    public void addUserInfo(UserInfo userInfo);
}
