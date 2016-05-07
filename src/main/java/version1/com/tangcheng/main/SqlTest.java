package version1.com.tangcheng.main;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import version1.com.tangcheng.mapper.UserInfoMapper;
import version1.com.tangcheng.model.UserInfo;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tc on 2016/5/3.
 */
public class SqlTest {
    public static void main(String[] args) {

    }

    public static SqlSession getSqlSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sqlSessionFactory.openSession();
    }

    public static List<UserInfo> selectUserNameByDeepLength(int deep){
        SqlSession sqlSession = null;
        List<UserInfo> userinfoList = new ArrayList<UserInfo>();
        try {
            sqlSession = getSqlSession();
            userinfoList = sqlSession.getMapper(UserInfoMapper.class).selectUserInfoBydeepLength(deep);
            for(UserInfo userInfo:userinfoList){
                System.out.println(userInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return userinfoList;
    }



}
