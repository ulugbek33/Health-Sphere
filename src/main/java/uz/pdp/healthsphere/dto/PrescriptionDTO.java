package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.Prescription}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String dosage;

    private Integer durationDays;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long medicalRecordId;

    @JsonProperty("medicineId")
    private Long medicineId;

    private String medicineName;

    private BigDecimal medicinePrice;
}