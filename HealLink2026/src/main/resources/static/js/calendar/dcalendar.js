document.addEventListener('DOMContentLoaded', function () {

    var calendarEl = document.getElementById('calendar');
    var selectedDate = null;
    var selectedEventId = null; // 수정/삭제를 위한 ID 저장
    var modal = document.getElementById('eventModal');

    // 의사 ID
    const currentDoctorId = 1;

    // 모달 닫기
    document.getElementById('closeModal').addEventListener('click', function () {
        closeModal();
    });
    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });
    window.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });

    // 모달 닫기
    function closeModal() {
        modal.style.display = 'none';
        document.getElementById('eventTitle').value = '';
        selectedEventId = null;
        selectedDate = null;
    }

    var calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'ko',
        initialView: 'dayGridMonth',
        showNonCurrentDates: false,
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        selectable: true,
        editable: true,
        height: 900,
        slotLabelFormat: {hour: '2-digit', minute: '2-digit', hour12: false},

        // 서버에서 일정 가져오기
        events: function (fetchInfo, successCallback, failureCallback) {
            fetch(`/api/doctor-schedules/doctor/${currentDoctorId}`)
                .then(response => response.json())
                .then(data => {
                    const events = data.map(schedule => {
                        let eventTitle = '';
                        let bgColor = '';
                        
                        if (schedule.patientName) {
                            // 예약된 경우: 환자 이름 + 증상
                            eventTitle = `${schedule.patientName} (${schedule.symptom || '-'})`;
                            bgColor = '#dc3545'; // 빨간색 (예약됨)
                        } else {
                            // 예약 없는 경우
                            eventTitle = schedule.isAvailable ? '진료 가능' : '예약 마감';
                            bgColor = schedule.isAvailable ? '#3788d8' : '#6c757d'; // 파랑 or 회색
                        }

                        return {
                            id: schedule.scheduleId,
                            title: eventTitle,
                            start: `${schedule.workDate}T${schedule.startTime}`,
                            end: `${schedule.workDate}T${schedule.endTime}`,
                            backgroundColor: bgColor,
                            borderColor: bgColor,
                            extendedProps: {
                                workDate: schedule.workDate,
                                startTime: schedule.startTime,
                                endTime: schedule.endTime,
                                isAvailable: schedule.isAvailable,
                                patientName: schedule.patientName, // 상세 정보용
                                symptom: schedule.symptom
                            }
                        };
                    });
                    successCallback(events);
                })
                .catch(error => {
                    console.error('Error fetching schedules:', error);
                    failureCallback(error);
                });
        },

        // 새 일정 등록
        dateClick: function(info) {
            selectedDate = info.dateStr;
            selectedEventId = null;
            
            // 모달 초기화
            document.getElementById('eventTitle').value = '진료 가능';
            document.getElementById('startTime').value = '08:00';
            document.getElementById('endTime').value = '18:00';
            document.getElementById('isAvailable').checked = true;
            
            modal.style.display = 'block';
        },

        // 일정 수정/삭제
        eventClick: function(info) {
            selectedEventId = info.event.id;
            selectedDate = info.event.extendedProps.workDate; // yyyy-MM-dd
            
            document.getElementById('eventTitle').value = info.event.title;
            
            // 시간 포맷 처리 (HH:mm:ss -> HH:mm)
            let start = info.event.extendedProps.startTime || '08:00';
            let end = info.event.extendedProps.endTime || '18:00';
            
            if(start.length > 5) start = start.substring(0, 5);
            if(end.length > 5) end = end.substring(0, 5);

            document.getElementById('startTime').value = start;
            document.getElementById('endTime').value = end;
            document.getElementById('isAvailable').checked = info.event.extendedProps.isAvailable;
            
            modal.style.display = 'block';
        }
    });

    calendar.render();

    // 저장 등록/수정
    document.getElementById('saveEvent').addEventListener('click', function () {
        var title = document.getElementById('eventTitle').value;
        
        // 입력 값 가져오기
        var startTime = document.getElementById('startTime').value; // HH:mm
        var endTime = document.getElementById('endTime').value;     // HH:mm
        var isAvailable = document.getElementById('isAvailable').checked;

        if (!selectedDate) return;

        // 초 단위 추가 (LocalTime 형식 맞추기 위해 :00 붙임)
        if(startTime.length === 5) startTime += ':00';
        if(endTime.length === 5) endTime += ':00';

        const scheduleData = {
            doctorId: currentDoctorId,
            workDate: selectedDate,
            startTime: startTime,
            endTime: endTime,
            isAvailable: isAvailable
        };

        let url = '/api/doctor-schedules';
        let method = 'POST';

        if (selectedEventId) {
            url += `/${selectedEventId}`;
            method = 'PUT';
            scheduleData.scheduleId = selectedEventId;
        }

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(scheduleData)
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok');
        })
        .then(data => {
            // 성공 시 달력 갱신 및 모달 닫기
            calendar.refetchEvents();
            closeModal();
            alert(selectedEventId ? '수정되었습니다.' : '등록되었습니다.');
        })
        .catch(error => {
            console.error('Error saving schedule:', error);
            alert('저장에 실패했습니다.');
        });
    });

    // 삭제 버튼
    document.getElementById('deleteEvent').addEventListener('click', function () {
        if (!selectedEventId) {
            alert('삭제할 일정이 선택되지 않았습니다.');
            return;
        }

        if (!confirm('정말 삭제하시겠습니까?')) return;

        fetch(`/api/doctor-schedules/${selectedEventId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                calendar.refetchEvents();
                closeModal();
                alert('삭제되었습니다.');
            } else {
                alert('삭제 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error deleting schedule:', error);
            alert('오류가 발생했습니다.');
        });
    });

});
