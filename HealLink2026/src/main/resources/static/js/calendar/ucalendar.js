let selectedDate = null;
let selectedTimeId = null; // DoctorSchedule의 ID
let doctorSchedules = [];   // 서버에서 받아온 스케줄 목록

const grid = document.getElementById("timeGrid");
const confirmBtn = document.getElementById("confirmBtn");
const doctorIdInput = document.getElementById("doctorId");

async function loadDoctorSchedules() {
    const doctorId = doctorIdInput.value;
    if (!doctorId) {
        console.error("Doctor ID is missing!");
        return;
    }

    try {
        const response = await fetch(`/api/doctor-schedules/doctor/${doctorId}`);
        if (!response.ok) throw new Error("Failed to fetch schedules");
        
        doctorSchedules = await response.json();
        console.log("Loaded schedules:", doctorSchedules);
        
        const today = new Date().toISOString().split('T')[0];
        renderTimeSlots(today);
    } catch (error) {
        console.error("Error loading schedules:", error);
        grid.innerHTML = `<p class="guide-text" style="color:red;">스케줄을 불러오는 데 실패했습니다.</p>`;
    }
}

function renderTimeSlots(dateStr) {
    selectedDate = dateStr;
    selectedTimeId = null;
    
    grid.innerHTML = `<p class="guide-text">${dateStr} 예약 가능 시간</p>`;
    confirmBtn.style.display = "none";

    // 해당 날짜의 스케줄
    const daySchedules = doctorSchedules.filter(s => s.workDate === dateStr);

    if (daySchedules.length === 0) {
        grid.innerHTML += `<p class="guide-text">해당 날짜에는 진료 일정이 없습니다.</p>`;
        return;
    }

    daySchedules.sort((a, b) => a.startTime.localeCompare(b.startTime));

    daySchedules.forEach(schedule => {
        const btn = document.createElement("button");
        // 초 단위 제외하고 HH:mm 형식으로 표시
        const displayTime = schedule.startTime.substring(0, 5);
        btn.textContent = displayTime;
        btn.className = "time-btn";

        // 이미 예약된 경우 비활성화
        if (!schedule.isAvailable) {
            btn.disabled = true;
            btn.classList.add("reserved");
            btn.textContent += " (만료)";
        } else {
            btn.addEventListener("click", () => {
                document.querySelectorAll(".time-btn").forEach(b => b.classList.remove("selected"));
                btn.classList.add("selected");
                selectedTimeId = schedule.scheduleId;

                confirmBtn.textContent = `${displayTime}으로 예약하시겠습니까?`;
                confirmBtn.style.display = "block";
            });
        }

        grid.appendChild(btn);
    });
}

async function submitReservation() {
    if (!selectedTimeId) return;

    if (!confirm("선택하신 시간으로 예약을 진행하시겠습니까?")) {
        return;
    }

    const deptIdInput = document.getElementById("departmentId");
    const departmentId = deptIdInput ? deptIdInput.value : 1; 

    // 증상 가져오기
    const symptomInput = document.getElementById("symptom");
    const symptomValue = symptomInput ? symptomInput.value : "일반 진료";

    const reservationData = {
        scheduleId: selectedTimeId,
        departmentId: departmentId,
        symptom: symptomValue,
        note: "웹 예약을 통한 접수"
    };

    try {
        const response = await fetch('/api/appointments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(reservationData)
        });

        if (response.ok) {
            alert("예약이 성공적으로 완료되었습니다!");
            window.location.href = "/"; // 메인 페이지로 이동
        } else if (response.status === 409) {
            alert("죄송합니다. 그새 다른 분이 먼저 예약하셨네요!");
            await loadDoctorSchedules();
        } else {
            throw new Error("Reservation failed");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("예약 처리 중 오류가 발생했습니다.");
    }
}

confirmBtn.addEventListener("click", submitReservation);

// 캘린더 초기화
flatpickr(document.getElementById("datePicker"), {
    inline: true,
    locale: "ko",
    dateFormat: "Y-m-d",
    defaultDate: "today",
    minDate: "today",
    disableMobile: true,
    onChange: (_, dateStr) => renderTimeSlots(dateStr)
});

document.addEventListener("DOMContentLoaded", loadDoctorSchedules);