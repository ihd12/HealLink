package org.zerock.obj2026.appointment.service;

public interface AppointmentService {
    /*
     * 예약 생성.
     * @param scheduleId 예약할 의사 스케줄 ID
     * @param patientId 예약을 하는 환자 ID
     * @param departmentId 예약할 진료과 ID
     * @param symptom 증상
     * @param note 참고사항
     * @return 생성된 예약의 ID
     */
    Long createAppointment(Long scheduleId, Long patientId, Long departmentId, String symptom, String note);
}
