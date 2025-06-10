package models.search;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

/**
 * 搜索
 */
@Entity
@Table(name = "v1_product_search")
public class ProductSearch extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "keyword")
    public String keyword;

    @Column(name = "views")
    public long views;

    @Column(name = "station_id")
    public long stationId;

    public static Finder<Long, ProductSearch> find = new Finder<>(ProductSearch.class);

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

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }
}
