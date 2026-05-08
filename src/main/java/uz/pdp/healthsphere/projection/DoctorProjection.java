package uz.pdp.healthsphere.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.Specialization;

import java.math.BigDecimal;
import java.util.List;

public interface DoctorProjection {

    Long getDoctorId();

    String getFullName();

    Integer getExperienceYears();

    BigDecimal getConsultationFee();

    String getRoomNumber();

    @JsonProperty("specialization")
    String getSpecializationName();
}
