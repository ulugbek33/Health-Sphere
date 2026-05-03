package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.MedicalRecord}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String diagnosis;

    private String notes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long appointmentId;

    private List<PrescriptionDTO> prescriptions;
}