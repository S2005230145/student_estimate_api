package models.search;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

/**
 * 文章搜索
 */
@Entity
@Table(name = "v1_news_search")
public class NewsSearch extends Model {
    public static final int TYPE_RECOMMENT = 1;
    public static final int TYPE_TIMELINE = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "keyword")
    public String keyword;

    @Column(name = "views")
    public long views;

    @Column(name = "org_id")
    private long orgId;

    public static Finder<Long, NewsSearch> find = new Finder<>(NewsSearch.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
}
