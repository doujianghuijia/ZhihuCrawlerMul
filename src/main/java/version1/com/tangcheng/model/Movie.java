package version1.com.tangcheng.model;

/**
 * Created by tc on 2016/4/23.
 * id="26359246" class="list-item hidden"
 * data-title="灵偶契约" data-score="6.2"
 * data-star="35" data-release="2016"
 * data-duration="97分钟" data-region="美国"
 * data-director="威廉·布伦特·贝尔"
 * data-actors="劳伦·科汉 / 鲁珀特·伊文斯 / 吉姆·诺顿"
 * data-category="nowplaying" data-enough="True"
 * data-showed="True" data-votecount="3469" data-subject="26359246">
 * <img src="https://img3.doubanio.com/view/movie_poster_cover/mpst/public/p2327655150.jpg"
 */
public class Movie {
    private int movie_id;     //主键
    private String movie_name;
    private String movie_score;
    private String movie_duration;
    private String movie_region;
    private String movie_director;
    private String movie_actors;
    private String movie_picture;

    public Movie() {
    }

    public Movie(int movie_id, String movie_name, String movie_score, String movie_duration, String movie_region, String movie_director, String movie_actors, String movie_picture) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.movie_score = movie_score;
        this.movie_duration = movie_duration;
        this.movie_region = movie_region;
        this.movie_director = movie_director;
        this.movie_actors = movie_actors;
        this.movie_picture = movie_picture;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_score() {
        return movie_score;
    }

    public void setMovie_score(String movie_score) {
        this.movie_score = movie_score;
    }

    public String getMovie_duration() {
        return movie_duration;
    }

    public void setMovie_duration(String movie_duration) {
        this.movie_duration = movie_duration;
    }

    public String getMovie_region() {
        return movie_region;
    }

    public void setMovie_region(String movie_region) {
        this.movie_region = movie_region;
    }

    public String getMovie_director() {
        return movie_director;
    }

    public void setMovie_director(String movie_director) {
        this.movie_director = movie_director;
    }

    public String getMovie_actors() {
        return movie_actors;
    }

    public void setMovie_actors(String movie_actors) {
        this.movie_actors = movie_actors;
    }

    public String getMovie_picture() {
        return movie_picture;
    }

    public void setMovie_picture(String movie_picture) {
        this.movie_picture = movie_picture;
    }
}
