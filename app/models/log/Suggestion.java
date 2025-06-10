package models.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;

import java.io.Serializable;

@Entity
@Table(name = "v1_suggestion")
public class Suggestion extends Model implements Serializable {
    private static final long serialVersionUID = -1885841224604019263L;

    public static final int STATUS_NOT_HANDLE = 1;
    public static final int STATUS_NEED_FURTHER_CONTACT = 2;
    public static final int STATUS_HANDLED = 3;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "uid")
    public long uid;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "status")
    public int status;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "create_time")
    public long createTime;//创建时间

    public static Finder<Long, Suggestion> find = new Finder<>(Suggestion.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
