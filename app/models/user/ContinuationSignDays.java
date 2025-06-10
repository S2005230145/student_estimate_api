package models.user;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

/**
 * 连续签到天数
 */
@Entity
@Table(name = "v1_continuation_sign_days")
public class ContinuationSignDays extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uid")
    private long uid;

    @Column(name = "days")
    private long days;


    public static Finder<Long, ContinuationSignDays> find = new Finder<>(ContinuationSignDays.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }
}
