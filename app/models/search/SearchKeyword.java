package models.search;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

/**
 * 关键字-搜索
 */
@Entity
@Table(name = "v1_search_keyword")
public class SearchKeyword extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @Column(name = "keyword")
    public String keyword;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "source")
    public int source;//搜索来源

    @Column(name = "enable")
    public boolean enable;//是否显示


    @Column(name = "sort")
    public long sort;

    public static Finder<Long, SearchKeyword> find = new Finder<>(SearchKeyword.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }
}
