document.addEventListener('DOMContentLoaded', function () {

    var calendarEl = document.getElementById('calendar');
    var selectedDate = null;
    var selectedEvent = null;
    var modal = document.getElementById('eventModal');

    document.getElementById('closeModal').addEventListener('click', function () {
        modal.style.display = 'none';
    });
    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
    window.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' && modal.style.display === 'block') {
            modal.style.display = 'none';
        }
    });

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

        dateClick: function(info) {
            selectedDate = info.dateStr;
            selectedEvent = null; // 새 일정
            document.getElementById('eventTitle').value = '';
            modal.style.display = 'block';
        },

        eventClick: function(info) {
            selectedEvent = info.event;
            document.getElementById('eventTitle').value = info.event.title;
            modal.style.display = 'block';
        },

        events: [
            { title: '테스트 일정', start: '2026-01-14' }
        ]
    });

    calendar.render();

    document.getElementById('saveEvent').addEventListener('click', function () {
        var title = document.getElementById('eventTitle').value;
        if (!title) return;

        if (selectedEvent) {
            selectedEvent.setProp('title', title);
        } else if (selectedDate) {
            calendar.addEvent({
                title: title,
                start: selectedDate
            });
        }

        modal.style.display = 'none';
    });

    document.getElementById('deleteEvent').addEventListener('click', function () {
        if (selectedEvent) {
            selectedEvent.remove();
            selectedEvent = null;
        }
        modal.style.display = 'none';
    });

});
