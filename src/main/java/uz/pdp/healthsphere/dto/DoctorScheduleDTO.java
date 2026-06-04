package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.DayOfWeekEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.DoctorSchedule}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer slotDuration;

    private DayOfWeekEnum dayOfWeek;

    @JsonIgnore
    private Long doctorId;
}