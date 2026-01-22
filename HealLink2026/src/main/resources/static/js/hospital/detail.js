
document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById('doctorModal');
    const closeBtn = document.querySelector('.close-btn');

    window.openDoctorModal = function() {
        if (modal) {
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
        }
    };

    window.closeDoctorModal = function() {
        if (modal) {
            modal.classList.remove('active');
            document.body.style.overflow = '';
        }
    };

    if (closeBtn) {
        closeBtn.addEventListener('click', closeDoctorModal);
    }

    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeDoctorModal();
            }
        });
    }
    
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal.classList.contains('active')) {
            closeDoctorModal();
        }
    });

    const mapContainer = document.getElementById('map');
    const latInput = document.getElementById('hospitalLat');
    const lonInput = document.getElementById('hospitalLon');

    // 카카오 지도
    if (mapContainer && latInput && lonInput) {
        const initMap = () => {
            if (window.kakao && window.kakao.maps) {
                kakao.maps.load(() => {
                    const lat = parseFloat(latInput.value) || 33.450701;
                    const lon = parseFloat(lonInput.value) || 126.570667;
                    const position = new kakao.maps.LatLng(lat, lon);
    
                    const mapOption = {
                        center: position,
                        level: 3
                    };
    
                    const map = new kakao.maps.Map(mapContainer, mapOption);
    
                    const marker = new kakao.maps.Marker({
                        position: position
                    });
    
                    marker.setMap(map);
                    
                    const zoomControl = new kakao.maps.ZoomControl();
                    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
                });
            }
        };

        if (window.kakao && window.kakao.maps) {
            initMap();
        } else {
            setTimeout(initMap, 500);
        }
    }
});
