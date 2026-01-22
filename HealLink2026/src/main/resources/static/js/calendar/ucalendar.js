let selectedDate = null;
let selectedTimeStr = null;
let reservedSchedules = []; // 이미 예약된 스케줄 목록

const grid = document.getElementById("timeGrid");
const confirmBtn = document.getElementById("confirmBtn");
const doctorIdInput = document.getElementById("doctorId");

async function loadReservedSchedules() {
    const doctorId = doctorIdInput.value;
    if (!doctorId) {
        console.error("Doctor ID is missing!");
        return;
    }

    try {
        const response = await fetch(`/api/doctor-schedules/doctor/${doctorId}`);
        if (!response.ok) throw new Error("Failed to fetch schedules");
        
        const allSchedules = await response.json();
        reservedSchedules = allSchedules.filter(s => !s.isAvailable);
        
    } catch (error) {
        console.error("Error loading schedules:", error);
    }
}

function renderTimeSlots(dateStr) {
    selectedDate = dateStr;
    selectedTimeStr = null;
    
    grid.innerHTML = `<p class="guide-text">${dateStr} 진료 가능 시간</p>`;
    confirmBtn.style.display = "none";

    // 운영 시간 설정
    const startHour = 8;
    const endHour = 18;
    const intervalMinutes = 30;

    const timeSlots = [];
    let current = new Date(`2000-01-01T${String(startHour).padStart(2, '0')}:00:00`);
    const end = new Date(`2000-01-01T${String(endHour).padStart(2, '0')}:00:00`);

    while (current < end) {
        // 점심시간 버튼제외
        if (current.getHours() === 12) {
            current.setMinutes(current.getMinutes() + intervalMinutes);
            continue;
        }

        // HH:mm:ss 형식
        const timeStr = current.toTimeString().split(' ')[0];
        timeSlots.push(timeStr);
        current.setMinutes(current.getMinutes() + intervalMinutes);
    }

    // 해당 날짜의 예약된 시간 목록 추출
    const reservedTimes = reservedSchedules
        .filter(s => s.workDate === dateStr)
        .map(s => s.startTime); // HH:mm:ss

    // 버튼 생성
    timeSlots.forEach(time => {
        const btn = document.createElement("button");
        const displayTime = time.substring(0, 5); // HH:mm
        btn.textContent = displayTime;
        btn.className = "time-btn";

        // 이미 예약된 시간이면 비활성화
        if (reservedTimes.includes(time)) {
            btn.disabled = true;
            btn.classList.add("reserved");
            btn.textContent += " (마감)";
        } else {
            btn.addEventListener("click", () => {
                document.querySelectorAll(".time-btn").forEach(b => b.classList.remove("selected"));
                btn.classList.add("selected");
                
                selectedTimeStr = time;

                confirmBtn.textContent = `${displayTime}으로 예약하시겠습니까?`;
                confirmBtn.style.display = "block";
            });
        }

        grid.appendChild(btn);
    });
}

//  예약 요청 전송
async function submitReservation() {
    if (!selectedDate || !selectedTimeStr) return;

    if (!confirm("선택하신 시간으로 예약을 진행하시겠습니까?")) {
        return;
    }

    const doctorId = doctorIdInput.value;
    const deptIdInput = document.getElementById("departmentId");
    
    // 진료과 ID 안전하게 가져오기 (없으면 기본값 1)
    let departmentId = 1;
    if (deptIdInput && deptIdInput.value) {
        const parsed = parseInt(deptIdInput.value);
        if (!isNaN(parsed) && parsed > 0) {
            departmentId = parsed;
        }
    }

    const symptomInput = document.getElementById("symptom");
    const symptomValue = symptomInput ? symptomInput.value : "일반 진료";

    const reservationData = {
        doctorId: doctorId,
        workDate: selectedDate,
        startTime: selectedTimeStr,
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
            window.location.href = "/"; 
        } else if (response.status === 409) {
            alert("죄송합니다. 이미 예약된 시간입니다.");
            await loadReservedSchedules();
            renderTimeSlots(selectedDate);
        } else {
            throw new Error("Reservation failed");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("예약 처리 중 오류가 발생했습니다.");
    }
}

confirmBtn.addEventListener("click", submitReservation);

document.addEventListener("DOMContentLoaded", async () => {
    console.log("DOM loaded, initializing calendar...");
    
    // 예약 현황 먼저 가져오기
    await loadReservedSchedules();

    const datePickerEl = document.getElementById("datePicker");
    if (datePickerEl) {
        flatpickr(datePickerEl, {
            inline: true,
            locale: "ko",
            dateFormat: "Y-m-d",
            defaultDate: "today",
            minDate: "today",
            disableMobile: true,
            onChange: (_, dateStr) => {
                console.log("Date selected:", dateStr);
                renderTimeSlots(dateStr);
            }
        });
        
        const today = new Date().toISOString().split('T')[0];
        renderTimeSlots(today);
        
        console.log("Flatpickr initialized!");
    } else {
        console.error("Critical Error: #datePicker element not found!");
    }
});
