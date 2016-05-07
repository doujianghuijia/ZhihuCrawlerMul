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
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tc on 2016/5/4.
 * 多线程对set进行修改报错
 */
public class MulCrawler {

    public static void main(String[] args) {
        MulCrawler mc = new MulCrawler();
        Business business = new Business();
        business.addUser("lang-xie-yang-wen-li", 1);
        long start = System.currentTimeMillis();
        System.out.println("开始爬虫");
        mc.begin(business);
        while (true) {
            if (business.count == business.threadMax || business.notCrawlerSet.isEmpty() && Thread.activeCount() == 1) {
                long end = System.currentTimeMillis();
                long time = (end - start) / 1000;
                System.out.println(business.allUserSet);
                System.out.println("共爬取了" + business.allUserSet.size() + "个用户信息,花费了" + time + " 秒");
                System.exit(1);
            }
        }
    }

    public void begin(final Business business){
        for(int i=0;i<business.threadMax;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        String username = business.getUser();
                        int deep = business.deepMap.get(username);
                        crawler(username, deep, business);
                        }
                    }
            }).start();
        }
    }

    public void crawler(String username,int deep,Business business){
        System.out.println(Thread.currentThread()+"crawl in");
        String preUrl = "https://www.zhihu.com/people/";
        UserInfo userInfo = business.getUserInfoDetail(preUrl + username, username);
        userInfo.setDeepLength(deep);
        SqlSession sqlSession = null;
        try {
            sqlSession = business.getSqlSession();
            sqlSession.getMapper(UserInfoMapper.class).addUserInfo(userInfo);
            sqlSession.commit();
            sqlSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(deep < business.deepMax){
            List<String> followers = business.getFollowers(userInfo);
            System.out.println(followers);
            for(String name2add:followers){
                business.addUser(name2add, deep + 1);
            }
        }
        System.out.println(Thread.currentThread()+"crawler out");
    }
}
class Business{
    public HashSet<String> allUserSet = new HashSet<String>();//所有爬取的用户
    public HashSet<String> notCrawlerSet = new HashSet<String>();//尚未爬取的用户
    public Map<String,Integer> deepMap = new HashMap<String, Integer>();//存储用户名和对应的深度
    private ReentrantLock lock = new ReentrantLock();
    private Condition getCon = lock.newCondition();
    int deepMax = 3;//最大深度
    int count = 0;//当前等待的线程数
    int threadMax = 5;//最大爬取线程数
    static String picRootPath = "D:\\知乎爬虫\\用户头像\\";

    public void addUser(String userName,int deep){
        System.out.println(Thread.currentThread().getName()+"  addUser    "+userName+"--"+deep);
        lock.lock();
        try{
            if(!allUserSet.contains(userName)){
                allUserSet.add(userName);
                notCrawlerSet.add(userName);
                deepMap.put(userName,deep);
                if(lock.hasWaiters(getCon)){
                    System.out.println("唤醒");
                    getCon.signal();
                    count--;
                }
            }
        }finally {
            lock.unlock();
        }
    }

    public String getUser(){
        lock.lock();
        try{
            while(true){
                if(!notCrawlerSet.isEmpty()){
                    String userName = notCrawlerSet.iterator().next();
                    System.out.println(userName+"**");
                    notCrawlerSet.remove(userName);
                    System.out.println(Thread.currentThread().getName()+"  get  "+userName);
                    return userName;
                }else{
                    System.out.println(Thread.currentThread().getName()+"休眠");
                    count++;
                    System.out.println("count:"+count);
                    getCon.await();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public static List<String> getFollowers(UserInfo userInfo){
        List<String> followersNameList = new ArrayList<String>();
        try{
            String userName = userInfo.getUserName();
            System.out.println(Thread.currentThread()+"---正在获取"+userName+"的关注者");
            String url = "https://www.zhihu.com/people/" + userName + "/followees";
            Connection conn = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
            conn.header("cookie", "q_c1=cf58fa3512c24833bf17c8f743703bf8|1460203453000|1460203453000; d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; l_n_c=1; l_cap_id=\"OWFiM2IyOWQ3ZmZjNDkwNWJmZWM4NTU0ZjJjZmUxNDI=|1462582875|e738c65ac838bf3648430ddb177e0445e3670664\"; cap_id=\"N2JkZjY3ZThhZDM0NGIxZTllZDcxYTg5MjI3NTA5NWQ=|1462582875|af88f522c3e64785100473fc73c46ada37b27b5b\"; _zap=e90095fa-d46b-4fce-aaeb-eea9cbac40e0; __utmt=1; login=\"NzAyZGZkNGE0NmUzNDZmZjgyZDA1YmU1OTdmZDMyN2Q=|1462582884|2d71077b1928dafb25730672da18a0daacf908bf\"; z_c0=Mi4wQUFEQU1xRTJBQUFBa0lCcnlzMi1DUmNBQUFCaEFsVk5mc3RVVndBbVlnc1BlZVpRZ3FSYk9Wc3kta1VQalByTmV3|1462582910|c1a73705d5e7762ab7ba50b4098cf147f6fdad7e; unlock_ticket=\"QUFEQU1xRTJBQUFYQUFBQVlRSlZUWVpGTFZkRG95clg2Zy1jX2hpMlpRc3lJRGhYZUkxRmRBPT0=|1462582910|baa1f658edde55487594463411bd27e27813da30\"; __utma=51854390.1566629121.1460203451.1461631927.1462582886.5; __utmb=51854390.8.10.1462582886; __utmc=51854390; __utmz=51854390.1462582886.5.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100-1|2=registration_date=20140906=1^3=entry_date=20140906=1");
            Document doc = conn.timeout(8000).get();
            Elements uersLink = doc.select("a.zg-link");
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
        Connection detailInfoConn = Jsoup.connect(infoUrl+"/about").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
        detailInfoConn.header("cookie","q_c1=cf58fa3512c24833bf17c8f743703bf8|1460203453000|1460203453000; d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; l_n_c=1; l_cap_id=\"OWFiM2IyOWQ3ZmZjNDkwNWJmZWM4NTU0ZjJjZmUxNDI=|1462582875|e738c65ac838bf3648430ddb177e0445e3670664\"; cap_id=\"N2JkZjY3ZThhZDM0NGIxZTllZDcxYTg5MjI3NTA5NWQ=|1462582875|af88f522c3e64785100473fc73c46ada37b27b5b\"; _zap=e90095fa-d46b-4fce-aaeb-eea9cbac40e0; __utmt=1; login=\"NzAyZGZkNGE0NmUzNDZmZjgyZDA1YmU1OTdmZDMyN2Q=|1462582884|2d71077b1928dafb25730672da18a0daacf908bf\"; z_c0=Mi4wQUFEQU1xRTJBQUFBa0lCcnlzMi1DUmNBQUFCaEFsVk5mc3RVVndBbVlnc1BlZVpRZ3FSYk9Wc3kta1VQalByTmV3|1462582910|c1a73705d5e7762ab7ba50b4098cf147f6fdad7e; unlock_ticket=\"QUFEQU1xRTJBQUFYQUFBQVlRSlZUWVpGTFZkRG95clg2Zy1jX2hpMlpRc3lJRGhYZUkxRmRBPT0=|1462582910|baa1f658edde55487594463411bd27e27813da30\"; __utma=51854390.1566629121.1460203451.1461631927.1462582886.5; __utmb=51854390.8.10.1462582886; __utmc=51854390; __utmz=51854390.1462582886.5.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100-1|2=registration_date=20140906=1^3=entry_date=20140906=1");
        Document detailDoc = null;
        try {
            detailDoc = detailInfoConn.timeout(8000).get();
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
            DownLoadUtil.download(bigPic, picStr);
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
}

