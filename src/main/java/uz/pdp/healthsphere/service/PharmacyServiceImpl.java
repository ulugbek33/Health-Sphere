package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.MedicineDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.entity.*;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.exceptions.OutOfStockException;
import uz.pdp.healthsphere.mapper.MedicalRecordMapper;
import uz.pdp.healthsphere.mapper.MedicineMapper;
import uz.pdp.healthsphere.mapper.PatientMapper;
import uz.pdp.healthsphere.mapper.PrescriptionMapper;
import uz.pdp.healthsphere.repository.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineMapper medicineMapper;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final MedicineRepository medicineRepository;
    private final InvoiceRepository invoiceRepository;
    private final PatientMapper patientMapper;

    @Override
    public MedicalRecordDTO readPrescription(Long patientId) {

        Patient patient = patientRepository.getByIdOrThrow(patientId);

        MedicalRecord medicalRecord = medicalRecordRepository.findTopByAppointmentPatientIdOrderByCreatedAtDesc(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Bemor uchun hech qanday retsept topilmadi :" + patientId, HttpStatus.NOT_FOUND));

//        List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecord(medicalRecord);
//
//        prescriptions.stream().map(prescriptionMapper::toDTO).toList();

        return medicalRecordMapper.toDTO(medicalRecord);
    }

    @Override
    @Transactional
    public void dispense(Long medicalRecordId) {

        MedicalRecord medicalRecord = medicalRecordRepository.getByIdOrThrow(medicalRecordId);

        List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecordId(medicalRecordId);

        Appointment appointment = medicalRecord.getAppointment();

        Invoice invoice = invoiceRepository.findByAppointment(appointment)
                .orElseThrow(() -> new EntityNotFoundException("Bu qabulga hisob faktura ochilmagan ", HttpStatus.NOT_FOUND));

        BigDecimal totalMedicinePrice = BigDecimal.ZERO;

        for (Prescription prescription : prescriptions) {

            Medicine medicine = prescription.getMedicine();

            int updated = medicineRepository.decrementStock(medicine.getId());

            if (updated == 0)
                throw new OutOfStockException(medicine.getName() + " omborda yetarli emas", HttpStatus.BAD_REQUEST);

            totalMedicinePrice = totalMedicinePrice.add(medicine.getPrice());

        }

        invoice.setTotalAmount(invoice.getTotalAmount().add(totalMedicinePrice));
        invoiceRepository.save(invoice);
    }

    @Override
    public List<MedicineDTO> getMedicines() {

        List<Medicine> medicines = medicineRepository.findAll();

        return medicineMapper.toDTO(medicines);
    }

    @Override
    @Transactional
    public MedicineDTO addMedicine(MedicineDTO medicineDTO) {

        Medicine medicine = medicineMapper.toEntity(medicineDTO);

        medicineRepository.save(medicine);

        return medicineMapper.toDTO(medicine);
    }

    @Override
    public List<PatientDTO> getPatients() {

        List<Patient> patients = patientRepository.findAll();

        return patientMapper.toDTO(patients);
    }

}
