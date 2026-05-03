package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.exceptions.EntityAlreadyException;
import uz.pdp.healthsphere.mapper.DoctorMapper;
import uz.pdp.healthsphere.mapper.DoctorScheduleMapper;
import uz.pdp.healthsphere.projection.DoctorProjection;
import uz.pdp.healthsphere.repository.AppointmentRepository;
import uz.pdp.healthsphere.repository.DoctorRepository;
import uz.pdp.healthsphere.repository.DoctorScheduleRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<DoctorProjection> getAllDoctors(String specialization, BigDecimal maxFee) {

        return doctorRepository.findByDoctorsByFilter(specialization, maxFee);
    }


}
