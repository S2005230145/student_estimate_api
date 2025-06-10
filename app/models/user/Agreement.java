package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;


@Entity
@Table(name = "v1_agreement")
public class Agreement extends Model {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;


    @Column(name = "agreement")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String agreement;

    @Column(name = "sign_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String signImgUrl;

    @Column(name = "membership_id")
    public long membershipId;

    @Column(name = "membership_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String membershipTitle;

    @Column(name = "membership_level")
    public int membershipLevel;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, Agreement> find = new Finder<>(Agreement.class);

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

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getSignImgUrl() {
        return signImgUrl;
    }

    public void setSignImgUrl(String signImgUrl) {
        this.signImgUrl = signImgUrl;
    }

    public long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(long membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipTitle() {
        return membershipTitle;
    }

    public void setMembershipTitle(String membershipTitle) {
        this.membershipTitle = membershipTitle;
    }

    public int getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", uid=" + uid +
                ", agreement='" + agreement + '\'' +
                ", signImgUrl='" + signImgUrl + '\'' +
                ", membershipId=" + membershipId +
                ", membershipTitle='" + membershipTitle + '\'' +
                ", membershipLevel=" + membershipLevel +
                ", createTime=" + createTime +
                '}';
    }
}
