package models.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

/**
 * 登录日志表,以用户id来分表
 */
@Entity
@Table(name = "v1_operation_log")
public class OperationLog extends Model {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "admin_id")
    public long adminId;
    @Column(name = "org_id")
    public long orgId;

    @Column(name = "admin_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String adminName;

    @Column(name = "ip")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String ip;

    @Column(name = "place")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String place;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "create_time")
    public long createTime;//登录时间

    public static Finder<Long, OperationLog> find = new Finder<>(OperationLog.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
}
