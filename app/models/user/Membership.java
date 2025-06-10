package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "v1_membership")
public class Membership extends Model {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "price")
    public long price;

    @Column(name = "old_price")
    public long oldPrice;

    @Column(name = "sort")
    public int sort;

    @Column(name = "days")
    public int days;

    @Column(name = "level")
    public int level;

    @Column(name = "first_dealer_award")
    public long firstDealerAward;

    @Column(name = "second_dealer_award")
    public long secondDealerAward;

    @Column(name = "beauty_card")
    public long beautyCard;

    @Column(name = "tx_direct_award")
    public double txDirectAward;

    @Column(name = "tx_indirect_award")
    public double txIndirectAward;

    @Column(name = "need_balance")
    public boolean needBalance;

    @Column(name = "need_mail")
    public boolean needMail;

    @Column(name = "need_cooperator_award")
    public boolean needCooperatorAward;

    @Column(name = "allow_cooperator_award_level")
    public String allowCooperatorAwardLevel;

    @Column(name = "need_show")
    public boolean needShow;

    @Column(name = "detail")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String detail;

    @Column(name = "allow_category_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String allowCategoryId;

    @Column(name = "allow_category_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String allowCategoryName;

    @Transient
    public List<MembershipRight> membershipRightList = new ArrayList<>();

    public static Finder<Long, Membership> find = new Finder<>(Membership.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isNeedBalance() {
        return needBalance;
    }

    public void setNeedBalance(boolean needBalance) {
        this.needBalance = needBalance;
    }

    public long getFirstDealerAward() {
        return firstDealerAward;
    }

    public void setFirstDealerAward(long firstDealerAward) {
        this.firstDealerAward = firstDealerAward;
    }

    public long getSecondDealerAward() {
        return secondDealerAward;
    }

    public void setSecondDealerAward(long secondDealerAward) {
        this.secondDealerAward = secondDealerAward;
    }

    public double getTxDirectAward() {
        return txDirectAward;
    }

    public void setTxDirectAward(double txDirectAward) {
        this.txDirectAward = txDirectAward;
    }

    public double getTxIndirectAward() {
        return txIndirectAward;
    }

    public void setTxIndirectAward(double txIndirectAward) {
        this.txIndirectAward = txIndirectAward;
    }

    public long getBeautyCard() {
        return beautyCard;
    }

    public void setBeautyCard(long beautyCard) {
        this.beautyCard = beautyCard;
    }

    public String getAllowCategoryId() {
        return allowCategoryId;
    }

    public void setAllowCategoryId(String allowCategoryId) {
        this.allowCategoryId = allowCategoryId;
    }

    public String getAllowCategoryName() {
        return allowCategoryName;
    }

    public void setAllowCategoryName(String allowCategoryName) {
        this.allowCategoryName = allowCategoryName;
    }

    public boolean isNeedCooperatorAward() {
        return needCooperatorAward;
    }

    public void setNeedCooperatorAward(boolean needCooperatorAward) {
        this.needCooperatorAward = needCooperatorAward;
    }

    public boolean isNeedShow() {
        return needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    public String getAllowCooperatorAwardLevel() {
        return allowCooperatorAwardLevel;
    }

    public void setAllowCooperatorAwardLevel(String allowCooperatorAwardLevel) {
        this.allowCooperatorAwardLevel = allowCooperatorAwardLevel;
    }

    public boolean isNeedMail() {
        return needMail;
    }

    public void setNeedMail(boolean needMail) {
        this.needMail = needMail;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", oldPrice=" + oldPrice +
                ", sort=" + sort +
                ", days=" + days +
                ", level=" + level +
                ", firstDealerAward=" + firstDealerAward +
                ", secondDealerAward=" + secondDealerAward +
                ", beautyCard=" + beautyCard +
                ", txDirectAward=" + txDirectAward +
                ", txIndirectAward=" + txIndirectAward +
                ", needBalance=" + needBalance +
                ", needMail=" + needMail +
                ", needCooperatorAward=" + needCooperatorAward +
                ", allowCooperatorAwardLevel='" + allowCooperatorAwardLevel + '\'' +
                ", needShow=" + needShow +
                ", detail='" + detail + '\'' +
                ", allowCategoryId='" + allowCategoryId + '\'' +
                ", allowCategoryName='" + allowCategoryName + '\'' +
                '}';
    }
}
