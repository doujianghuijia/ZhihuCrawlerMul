package version1.com.tangcheng.main;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by tc on 2016/4/23.
 */
public class DouBanHotShowing {
    public static void main(String[] args) throws IOException{
        Document doc = null;
        try{
//            doc = Jsoup.connect("https://movie.douban.com/nowplaying").timeout(5000).get();
            Connection connect = Jsoup.connect("https://www.zhihu.com/people/fu-jiang-zhou/followees").userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
            connect.header("cookie", "d_c0=\"AJCAa8rNvgmPTjmLt9oFCLyHzvjMpzD8wNU=|1460203453\"; _za=68132d7f-975f-45d8-994e-9ac06d0018b3; _xsrf=ecc85566f730a3254d4ae64f6350b323; _zap=0152b8c6-f8f5-4626-9f21-fbc25ac2cbd6; login=\"YWIzMDY5YThmYjJiNDE0NmExMGI2ZWYzOGE5NjQzYWM=|1462507790|c806c848edc3ecbad4b87f1fdf7be0107cb486cc\"; __utmt=1; l_n_c=1; q_c1=cdf69f5259184db196cf0959c01edbc9|1462537662000|1462537662000; cap_id=\"NzMyN2E3MGM3ODA1NDYzZWEzNTEzOGQzNDg2OTVlZDA=|1462537662|7d840abf3ffbc3909236b73365c2bf74daabce85\"; l_cap_id=\"YzBlNTMyZWRmYmM0NDM3MWI4Y2Q5MTgzZjBjMjMzZjg=|1462537662|74022ca8a8beaf51e5760ae873064b88e4d90602\"; z_c0=Mi4wQUJEQXNkU1c0UWtBa0lCcnlzMi1DUmNBQUFCaEFsVk5nQnRVVndBNF94TG9GOEVsdkQ2VTdoeGNIVi1lLW5sekVR|1462537856|d9cae186a50f6fe5abe8ee16c13511dbce7e6e6a; unlock_ticket=\"QUJEQXNkU1c0UWtYQUFBQVlRSlZUWWlWTEZjQ1lIZlJUNDBfb1Q1SWhxQlpzcXJkb1hmNWVRPT0=|1462537856|70ab597a2df30137d9de59efd7992880a6163051\"; __utma=51854390.1034949379.1462526457.1462535763.1462537645.3; __utmb=51854390.6.10.1462537645; __utmc=51854390; __utmz=51854390.1462535763.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=51854390.100-2|2=registration_date=20160506=1^3=entry_date=20160506=1");
            doc = connect.timeout(5000).get();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(doc);
//        Element ullistsElement = doc.select("ul.lists").first();
//        Elements elements = ullistsElement.children().select("li.list-item");
//        System.out.println("开始数据保存...");
//        for(Element item:elements){
//            String picPath = "D:\\豆瓣电影图片\\"+item.attr("data-title")+".jpg";
//            Movie movie =new Movie(Integer.parseInt(item.attr("id")),item.attr("data-title"),item.attr("data-score"),item.attr("data-duration"),item.attr("data-region"),
//                    item.attr("data-director"),item.attr("data-actors"),picPath);
//            System.out.println("正在保存电影："+item.attr("data-title"));
//            String picUrl = item.children().select("li.poster").select("a").select("img").attr("src");
//            DownLoadUtil.download(picUrl,picPath);
//            SqlSession sqlSession = getSqlSession();
//            try{
//                sqlSession.getMapper(MovieMapper.class).addMovie(movie);
//                sqlSession.commit();
//            }catch (Exception e){
//               throw new RuntimeException(e.getMessage());
//            }finally {
//                sqlSession.close();
//            }
//        }
//        System.out.println("完成数据保存!");
    }

    public static SqlSession getSqlSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        return sqlSessionFactory.openSession();
    }
}
