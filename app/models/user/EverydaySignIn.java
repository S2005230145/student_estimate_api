package models.user;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

/**
 * 签到
 */
@Entity
@Table(name = "v1_everyday_sign_in")
public class EverydaySignIn extends Model {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uid")
    private long uid;

    @Column(name = "create_time")
    private long createdTime;

    @Transient
    public String userName;

    @Transient
    public String avatar;

    public static Finder<Long, EverydaySignIn> find = new Finder<>(EverydaySignIn.class);

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

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "EverydaySignIn{" +
                "id=" + id +
                ", uid=" + uid +
                ", createdTime=" + createdTime +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
