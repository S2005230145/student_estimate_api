package models.mouth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "monthly_performance_snapshot")
public class MonthlyPerformanceSnapshot extends Model {

    @Id
    @Column(name = "record_id")
    private Long recordId;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "year")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String year;
    @Column(name = "mouth")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String mouth;
    @Column(name = "sum_mouth_score")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String sumMouthScore;
    @Column(name = "sum_final_score")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String sumFinalScore;
    @Column(name = "settle_state")
    private Long settleState;
    @Column(name = "settle_time")
    private Long settleTime;
    @Column(name = "type")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String type;

    public static final Finder<Long, MonthlyPerformanceSnapshot> find = new Finder<>(MonthlyPerformanceSnapshot.class);

}
