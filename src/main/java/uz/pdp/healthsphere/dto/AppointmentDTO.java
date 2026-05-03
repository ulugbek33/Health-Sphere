package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.StatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.Appointment}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private StatusEnum status;

    private String symptoms;

    private Long doctorId;

    private Long patientId;
}