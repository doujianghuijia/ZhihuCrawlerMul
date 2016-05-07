package version1.com.tangcheng.model;

/**
 * Created by tc on 2016/4/27.
 */
public class UserInfo {
    private String userName;//用户名
    private String nickName;//昵称
    private String gender;//性别
    private String oneSentence;//一句话介绍
    private String description;//简介
    private String business;//所在行业
    private String weibo;//微博
    private String location;//居住地
    private String career;//职业经历
    private String employment;//所在公司
    private String position;//职位
    private String education;//教育经历
    private String locationExp;//居住信息
    private int followeeNumber;//关注数
    private int followerNumber;//被关注数
    private int voteNumber;//赞同数
    private int thankNumber;//感谢数
    private int favNumber;//收藏数
    private int shareNumber;//分享数
    private int visitedNumber;//被浏览数
    private int askNumber;//提问数
    private int answerNumber;//回答数
    private int postNumber;//文章数
    private int collectionNumber;//收藏数
    private int logsNumber;//公共编辑数
    private int followedNumber;//关注专栏数
    private int topicNumber;//关注话题数
    private String picStr;//图片路径
    private int deepLength;//深度

    public UserInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOneSentence() {
        return oneSentence;
    }

    public void setOneSentence(String oneSentence) {
        this.oneSentence = oneSentence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getFolloweeNumber() {
        return followeeNumber;
    }

    public void setFolloweeNumber(int followeeNumber) {
        this.followeeNumber = followeeNumber;
    }

    public int getFollowerNumber() {
        return followerNumber;
    }

    public void setFollowerNumber(int followerNumber) {
        this.followerNumber = followerNumber;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
    }

    public int getThankNumber() {
        return thankNumber;
    }

    public void setThankNumber(int thankNumber) {
        this.thankNumber = thankNumber;
    }

    public int getFavNumber() {
        return favNumber;
    }

    public void setFavNumber(int favNumber) {
        this.favNumber = favNumber;
    }

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public int getVisitedNumber() {
        return visitedNumber;
    }

    public void setVisitedNumber(int visitedNumber) {
        this.visitedNumber = visitedNumber;
    }

    public int getAskNumber() {
        return askNumber;
    }

    public void setAskNumber(int askNumber) {
        this.askNumber = askNumber;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public int getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(int collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public int getLogsNumber() {
        return logsNumber;
    }

    public void setLogsNumber(int logsNumber) {
        this.logsNumber = logsNumber;
    }

    public int getFollowedNumber() {
        return followedNumber;
    }

    public void setFollowedNumber(int followedNumber) {
        this.followedNumber = followedNumber;
    }

    public int getTopicNumber() {
        return topicNumber;
    }

    public void setTopicNumber(int topicNumber) {
        this.topicNumber = topicNumber;
    }

    public String getPicStr() {
        return picStr;
    }

    public void setPicStr(String picStr) {
        this.picStr = picStr;
    }

    public int getDeepLength() {
        return deepLength;
    }

    public void setDeepLength(int deepLength) {
        this.deepLength = deepLength;
    }

    public String getLocationExp() {
        return locationExp;
    }

    public void setLocationExp(String locationExp) {
        this.locationExp = locationExp;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", oneSentence='" + oneSentence + '\'' +
                ", description='" + description + '\'' +
                ", business='" + business + '\'' +
                ", weibo='" + weibo + '\'' +
                ", location='" + location + '\'' +
                ", career='" + career + '\'' +
                ", employment='" + employment + '\'' +
                ", position='" + position + '\'' +
                ", education='" + education + '\'' +
                ", locationExp='" + locationExp + '\'' +
                ", followeeNumber=" + followeeNumber +
                ", followerNumber=" + followerNumber +
                ", voteNumber=" + voteNumber +
                ", thankNumber=" + thankNumber +
                ", favNumber=" + favNumber +
                ", shareNumber=" + shareNumber +
                ", visitedNumber=" + visitedNumber +
                ", askNumber=" + askNumber +
                ", answerNumber=" + answerNumber +
                ", postNumber=" + postNumber +
                ", collectionNumber=" + collectionNumber +
                ", logsNumber=" + logsNumber +
                ", followedNumber=" + followedNumber +
                ", topicNumber=" + topicNumber +
                ", picStr='" + picStr + '\'' +
                ", deepLength=" + deepLength +
                '}';
    }
}
