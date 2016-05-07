package version1.com.tangcheng.main;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import version1.com.tangcheng.mapper.UserInfoMapper;
import version1.com.tangcheng.model.UserInfo;
import version1.com.tangcheng.utils.DownLoadUtil;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tc on 2016/4/27.
 */
public class Test {
    final String url = "http://www.zhihu.com";
    static String picRootPath = "D:\\知乎爬虫\\用户头像\\";
    public static final Object obj = new Object(); //通信变量
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try {
            String firstUserName = "lang-xie-yang-wen-li";
            SqlSession sqlSession = getSqlSession();
            UserInfo firstUserInfo = new UserInfo();
            firstUserInfo.setUserName(firstUserName);
            firstUserInfo.setDeepLength(0);
            sqlSession.getMapper(UserInfoMapper.class).addUserName(firstUserInfo);
            sqlSession.commit();
            sqlSession.close();
//            for(int deep=1;deep<4;deep++){
//                List<UserInfo> userInfoList = new ArrayList<UserInfo>();
//                int d = deep-1;
//                System.out.println("======正在查询深度为"+d+"========"+Thread.currentThread());
//                userInfoList = getSqlSession().getMapper(UserInfoMapper.class).selectUserInfoBydeepLength(d);
//                GetFollowersThread getf = new GetFollowersThread(userInfoList, deep);
//                Thread thread = new Thread(getf);
//                thread.start();
//            }

//            GetdetailThread thread = new GetdetailThread();
//            Thread t = new Thread(thread);
//            t.start();
            for(int deep=1;deep<4;deep++){
                List<UserInfo> userInfoList1 = new ArrayList<UserInfo>();
                int d = deep-1;
                System.out.println("======正在查询深度为"+d+"========");
                SqlSession sqlSession11 = getSqlSession();
                userInfoList1 = sqlSession11.getMapper(UserInfoMapper.class).selectUserInfoBydeepLength(d);
                for(UserInfo userInfo:userInfoList1){
                    System.out.println(userInfo.getUserName()+"的fo");
                    List<String> followers = getFollowers(userInfo);
                    for(String name:followers){
                        System.out.println("+++++++");
                        UserInfo userInfo1 = new UserInfo();
                        userInfo1.setDeepLength(deep);
                        userInfo1.setUserName(name);
                        SqlSession sqlSession1 = getSqlSession();
                        //互相关注，这样replace出现问题,之前的sqlsession没有commit
                        System.out.println("----正在保存"+userInfo1.getUserName()+"的用户名和深度----");
                        sqlSession1.getMapper(UserInfoMapper.class).addUserName(userInfo1);
                        sqlSession1.commit();
                        sqlSession1.close();
                    }
                }
            }
            for(int deep=1;deep<5;deep++){
                List<UserInfo> userInfoList = new ArrayList<UserInfo>();
                SqlSession sqlSession1 = getSqlSession();
                userInfoList = sqlSession1.getMapper(UserInfoMapper.class).selectUserInfoBydeepLength(deep-1);
                sqlSession1.close();
                for(UserInfo userInfo:userInfoList){
                    String url = "https://www.zhihu.com/people/"+userInfo.getUserName();
                    UserInfo userInfo1 = getUserInfoDetail(url,userInfo.getUserName());
                    System.out.println("&&&&&&"+userInfo1);
                    SqlSession sqlSession2 = getSqlSession();
                    System.out.println("*****正在保存"+userInfo1.getUserName()+"的详细信息******");
                    sqlSession2.getMapper(UserInfoMapper.class).updateUserInfo(userInfo1);
                    sqlSession2.commit();
                    sqlSession2.close();
                }
            }
//            getUserInfoDetail(uersLink.get(0).attr("href"));
//            getUserInfoDetail("https://www.zhihu.com/people/william-yy");
//            getUserInfoDetail("https://www.zhihu.com/people/hui-jia-57");
//            getUserInfoDetail("https://www.zhihu.com/people/hu-jing-70-38");

        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时："+((end-start)/1000)+"秒");
    }


    public static List<String> getFollowers(UserInfo userInfo){
        List<String> followersNameList = new ArrayList<String>();
        try{
            String userName = userInfo.getUserName();
            System.out.println(Thread.currentThread()+"---正在获取"+userName+"的关注者");
            Connection conn = Jsoup.connect("https://www.zhihu.com/people/"+userName+"/followees");
            conn.header("cookie", "q_c1=cf58fa3512c24833bf17c8f743703bf8|1460203453000|1460203453000; d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; l_n_c=1; l_cap_id=\"MzVlZWQ2Zjc4ODI4NDMyN2FjMzUzZDdhMWUyMTExMGY=|1462415449|dc515422b860aceb6da281053e4db95631a6dafc\"; cap_id=\"OWIzZWZhNDY0ZDY4NDlkZjg1MGUyOWIxOTYzOGJiZGM=|1462415449|4405aa88fd7956139ab63a9918cda93f4d7f5fb0\"; _zap=aff48701-73ad-4746-a04d-2d76f3b53bc0; login=\"YzY5ZjkwYmEyOThhNDZhOGIyNWVjZTc0ZWIwYWQzOGU=|1462415463|bbe74d438d8d68e18eb0c75b34fc8c3a80bdb7a9\"; z_c0=Mi4wQUFEQU1xRTJBQUFBa0lCcnlzMi1DUmNBQUFCaEFsVk5jejFTVndBLVFCcllxMmM4dDNUYm52RnhUQUhOLWcxNndn|1462415475|18cf06af27b9797783ce098f94b4a08c29e46564; __utmt=1; __utma=51854390.1392049907.1462448057.1462448057.1462448057.1; __utmb=51854390.2.10.1462448057; __utmc=51854390; __utmz=51854390.1462448057.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100-1|2=registration_date=20140906=1^3=entry_date=20140906=1");
            Document doc = conn.timeout(8000).get();
            Elements  uersLink = doc.select("a.zg-link");
            for(Element userLink:uersLink){
                Pattern userNamePattern = Pattern.compile("https://www.zhihu.com/people/(.+)");
                Matcher userNameMatcher = userNamePattern.matcher(userLink.attr("href"));
                if(userNameMatcher.find()){
                    String userNameGet = userNameMatcher.group(1);
                    followersNameList.add(userNameGet);
                }else {
                    System.out.println("正则错误");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return followersNameList;
    }



    public static UserInfo getUserInfoDetail(String infoUrl,String userName){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        System.out.println(Thread.currentThread()+"获取"+userName+"详细信息");
        Connection detailInfoConn = Jsoup.connect(infoUrl+"/about");
        detailInfoConn.header("cookie","q_c1=cf58fa3512c24833bf17c8f743703bf8|1460203453000|1460203453000; d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; l_n_c=1; l_cap_id=\"MzU0NWMzNTViMTc1NGRlMWEwYzRkMjFjNjBhN2RhNGY=|1462507780|5b66e350196143fe8d5c734f68827fd50199368d\"; cap_id=\"YTM1MzIzNDNlZTQ3NDE5NTg1Mjc4MTllZjU1MDE4OGY=|1462507780|61e4c283ce915bad8a1a2fd9edaf54448875c1b9\"; _zap=0152b8c6-f8f5-4626-9f21-fbc25ac2cbd6; login=\"YWIzMDY5YThmYjJiNDE0NmExMGI2ZWYzOGE5NjQzYWM=|1462507790|c806c848edc3ecbad4b87f1fdf7be0107cb486cc\"; z_c0=Mi4wQUFEQU1xRTJBQUFBa0lCcnlzMi1DUmNBQUFCaEFsVk5EcVpUVndBLTRSdGw1VXNlUTFLRU5teV9CLVQzUHpIcVlB|1462507790|c440c636315ecc74d616eb338b687c74968be1d0; __utmt=1; __utma=51854390.1034949379.1462526457.1462526457.1462526457.1; __utmb=51854390.2.10.1462526457; __utmc=51854390; __utmz=51854390.1462526457.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100-1|2=registration_date=20140906=1^3=entry_date=20140906=1");
        Document detailDoc = null;
        try {
            detailDoc = detailInfoConn.timeout(8000).get();
//            System.out.println(detailDoc);
            String nickName = detailDoc.select("a.name").first().text();
            userInfo.setNickName(nickName);
            Element oneSentenceEle = detailDoc.select("span.bio").first();
            if(oneSentenceEle != null){
                String oneSentence = oneSentenceEle.text();
                userInfo.setOneSentence(oneSentence);
            }
            Element bodyClearfix = detailDoc.select("div.body.clearfix").first();
            String smallPic = bodyClearfix.select("img.Avatar.Avatar--l").first().attr("src");
            String bigPic = smallPic.replace("_l","_xll");
            String picStr = picRootPath+nickName+".jpg";
            DownLoadUtil.download(bigPic,picStr);
            userInfo.setPicStr(picStr);
            Element locElement = detailDoc.select("span.location.item").first();
            if(locElement != null){
                String location = locElement.text();
                userInfo.setLocation(location);
            }

            Element genderEle = detailDoc.select("span.item.gender").first();
            if(genderEle != null){
                String gender = genderEle.child(0).attr("class");
                if(gender.equals("icon icon-profile-female")){
                    userInfo.setGender("女");
                }else{
                    userInfo.setGender("男");
                }
            }

            Element desElement = detailDoc.select("span.description.unfold-item").first();
            if(desElement != null){
                String description = desElement.child(0).text();
                userInfo.setDescription(description);
            }

            Element businessEle = detailDoc.select("span.business.item").first();
            if(businessEle != null){
                String business = businessEle.text();
                userInfo.setBusiness(business);
            }

            Element weiboEle = detailDoc.select("a.zm-profile-header-user-weibo").first();
            if(weiboEle != null){
                String weibo = weiboEle.attr("href");
                userInfo.setWeibo(weibo);
            }

            Element employmentEle = detailDoc.select("span.employment.item").first();
            if(employmentEle != null){
                String employment = employmentEle.text();
                userInfo.setEmployment(employment);
            }

            Element positionEle = detailDoc.select("span.position.item").first();
            if(positionEle != null){
                String position = positionEle.text();
                userInfo.setPosition(position);
            }

            StringBuilder career = new StringBuilder();
            Element careerElement = detailDoc.select("div.zm-profile-module.zg-clear").get(0);
            Elements profileItem0 = careerElement.select("div.ProfileItem");
            if(profileItem0 != null){
                for(Element item:profileItem0){
                    Elements signleInfo = item.select("div.ProfileItem-text.ProfileItem-text--bold").first().children();
                    for(Element info:signleInfo){
                        String temp = info.text();
                        career.append(temp);
                    }
                    career.append(";");
                }
                userInfo.setCareer(career.toString());
            }

            StringBuilder locationExp = new StringBuilder();
            Element locationExpElement = detailDoc.select("div.zm-profile-module.zg-clear").get(1);
            Elements profileItem1 = locationExpElement.select("div.ProfileItem");
            if(profileItem1 != null){
                for(Element item:profileItem1){
                    Elements signleInfo = item.select("div.ProfileItem-text.ProfileItem-text--bold").first().children();
                    for(Element info:signleInfo){
                        String temp = info.text();
                        locationExp.append(temp);
                    }
                    locationExp.append(";");
                }
                userInfo.setLocationExp(locationExp.toString());
            }

            StringBuilder education = new StringBuilder();
            Element educationElement = detailDoc.select("div.zm-profile-module.zg-clear").get(2);
            Elements profileItem2 = educationElement.select("div.ProfileItem");
            if(profileItem2 != null){
                for(Element item:profileItem2){
                    Elements signleInfo = item.select("div.ProfileItem-text.ProfileItem-text--bold").first().children();
                    for(Element info:signleInfo){
                        String temp = info.text();
                        education.append(temp);
                    }
                    education.append(";");
                }
                userInfo.setEducation(education.toString());
            }

            Element followeeingEle = detailDoc.select("div.zm-profile-side-following.zg-clear").first();
            String followeeStr = followeeingEle.select("a.item").get(0).select("strong").text();
            userInfo.setFolloweeNumber(Integer.parseInt(followeeStr));
            String followerStr = followeeingEle.select("a.item").get(1).select("strong").text();
            userInfo.setFollowerNumber(Integer.parseInt(followerStr));

            Element moduleEle = detailDoc.select("div.zm-profile-module.zm-profile-details-reputation").first();
            String voteStr = moduleEle.select("strong").get(0).text();
            String thankStr = moduleEle.select("strong").get(1).text();
            String favStr = moduleEle.select("strong").get(2).text();
            String shareStr = moduleEle.select("strong").get(3).text();
            userInfo.setVoteNumber(Integer.valueOf(voteStr));
            userInfo.setThankNumber(Integer.valueOf(thankStr));
            userInfo.setFavNumber(Integer.valueOf(favStr));
            userInfo.setShareNumber(Integer.valueOf(shareStr));

            String visitedStr = detailDoc.select("span.zg-gray-normal").get(2).select("strong").text();
            userInfo.setVisitedNumber(Integer.valueOf(visitedStr));

            Element asksElement = detailDoc.select("div.profile-navbar.clearfix").first();
            String askStr = asksElement.select("a").get(1).select("span").text();
            String answerStr = asksElement.select("a").get(2).select("span").text();
            String postStr = asksElement.select("a").get(3).select("span").text();
            String collectionStr = asksElement.select("a").get(4).select("span").text();
            String logStr = asksElement.select("a").get(5).select("span").text();
            userInfo.setAskNumber(Integer.parseInt(askStr));
            userInfo.setAnswerNumber(Integer.parseInt(answerStr));
            userInfo.setPostNumber(Integer.parseInt(postStr));
            userInfo.setCollectionNumber(Integer.parseInt(collectionStr));
            userInfo.setLogsNumber(Integer.parseInt(logStr));

            Element sameEle = detailDoc.select("div.zm-profile-side-same-friends").first();
            int index = 0;
            if(sameEle != null){
                index = 1;
            }
            Element followedEle = detailDoc.select("div.zm-profile-side-section-title").get(0+index);
            String followedStr = followedEle.select("strong").text();
            if(followedStr.split(" ")[1].equals("个专栏")){
                userInfo.setFollowedNumber(Integer.valueOf(followedStr.split(" ")[0]));
                Element topicEle = detailDoc.select("div.zm-profile-side-section-title").get(1+index);
                String topicStr = topicEle.select("strong").text();
                userInfo.setTopicNumber(Integer.parseInt(topicStr.split(" ")[0]));
            }else {
                userInfo.setTopicNumber(Integer.parseInt(followedStr.split(" ")[0]));
            }
//            System.out.println(userInfo);
//            System.out.println("=====================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static SqlSession getSqlSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sqlSessionFactory.openSession();
    }

    public static void saveUserInfo(UserInfo userInfo){
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
            sqlSession.getMapper(UserInfoMapper.class).updateUserInfo(userInfo);
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
    }

}

//-------------------start----------------------
//每次加载20个，根据offset进行判断起始位置，角标从0开始，之后解决
//            String  xsrfStr = doc.select("input[name=\"_xsrf\"]").first().attr("value");
//            Pattern hash_idPattern = Pattern.compile("hash_id&quot;: &quot;(.*?)&quot;}");
//            Matcher hash_idMatcher = hash_idPattern.matcher(doc.toString());
//            String hash_idStr = "";
//            if(hash_idMatcher.find()){
//                hash_idStr = hash_idMatcher.group(1);
//                System.out.println(hash_idStr);
//            }else {
//                System.out.println("未找到hash_id");
//            }
//            Connection connp = Jsoup.connect("https://www.zhihu.com/node/ProfileFolloweesListV2");
//            connp.data("Referer","https://www.zhihu.com/people/zhang-jia-wei/followees");
//            connp.data("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
//            connp.header("cookie", "q_c1=cf58fa3512c24833bf17c8f743703bf8|1460203453000|1460203453000; d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; l_n_c=1; l_cap_id=\"ZDcwY2M1YWRhNTYxNGMyM2FhNmMwNDk0YTFiODQxMzY=|1462175272|e924903560083759af75d651e6a20f132e21b4ca\"; cap_id=\"YzU0ODA1NDA0MWYyNDc5ZjlmN2Q2ZWQ1NDNiNjcxYmE=|1462175272|8dbb96a35b78e4f1220ef5cbe9c1e9879d6ee0b8\"; _zap=3d9297fe-4faa-4fce-81e1-3ff27a17b73a; __utmt=1; __utma=51854390.1566629121.1460203451.1461631927.1462175275.5; __utmb=51854390.2.10.1462175275; __utmc=51854390; __utmz=51854390.1462175275.5.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.000--|2=registration_date=20140906=1^3=entry_date=20160409=1; login=\"YWU2ZWRjZDAyNzNhNDIzZjgzZmRkNmU0YWQ3MDc3ZDU=|1462175281|eca2ac5e3c4953e8986d6bf8931e5888168b8843\"; z_c0=Mi4wQUFEQU1xRTJBQUFBa0lCcnlzMi1DUmNBQUFCaEFsVk5NWk5PVndCdzA1Tk1XN2hTb1NkMTN2ZmhWblRPYXNaQ0pB|1462175281|628113e603ac88a834eb467682e9df9ef14069cb; unlock_ticket=\"QUFEQU1xRTJBQUFYQUFBQVlRSlZUVGtOSjFjeHpWNlVOMWdVVTBTNFlzYmhKNzlscnpBMHZ3PT0=|1462175281|60fd73ebdf5893e7a8437e868072201fe516209c\"");
//            connp.data("method", "next");
//            connp.data("_xsrf",xsrfStr);
//            connp.data("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
//            connp.data("Accept-Language","zh-CN,zh;q=0.8");
//            connp.data("Accept-Encoding","gzip, deflate");
//            //待写
//            String offset = "40";
//            String params = "{\"offset\":"+offset+",\"order_by\":\"created\",\"hash_id\":\""+hash_idStr+"\"}";
//            connp.data("params",params);
//            Document document = Jsoup.parse(connp.ignoreContentType(true).timeout(5000).method(Connection.Method.GET).execute().body());
//           // Connection.Response response = connp.ignoreContentType(true).timeout(5000).method(Connection.Method.POST).execute();
//            System.out.println("<---------->");
//            System.out.println(document);
//-------------------end----------------------
