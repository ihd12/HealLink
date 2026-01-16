/**
 * HealLink 약국 지도 관리 스크립트
 * [구조]
 * 1. 전역 변수 설정
 * 2. 지도 초기화 로직
 * 3. 데이터 검색 및 처리 로직
 * 4. 지도 위 요소(마커, 원) 표시 로직
 * 5. 사이드바 UI 업데이트 로직
 * 6. 공통 유틸리티 함수
 */

// ==========================================
// 1. 전역 변수 설정 (어디서든 쓸 수 있는 변수)
// ==========================================
let map;             // 카카오 지도 객체
let ps;              // 장소 검색 서비스 객체
let infowindow;      // 마커 클릭 시 나타나는 정보창
let markers = [];    // 지도에 표시된 마커와 오버레이들을 담는 배열
let currentCircle = null; // 현재 지도 중심에 그려진 원(범위) 객체
let isDetailView = false; // 현재 사용자가 약국 상세 정보를 보고 있는지 여부

// ==========================================
// 2. 지도 초기화 로직
// ==========================================

/**
 * 지도를 처음 생성하고 초기 위치를 설정하는 함수
 */
function initPharmacyMap() {
    kakao.maps.load(function () {
        const container = document.getElementById('map'); // 지도를 담을 HTML 요소
        const options = {
            center: new kakao.maps.LatLng(37.566826, 126.9786567), // 기본 좌표 (서울시청)
            level: 2 // 지도 확대 레벨 (작을수록 크게 보임)
        };

        map = new kakao.maps.Map(container, options); // 지도 생성
        ps = new kakao.maps.services.Places(map);     // 검색 서비스 생성
        infowindow = new kakao.maps.InfoWindow({zIndex: 1}); // 정보창 생성

        // 브라우저의 GPS를 이용해 내 실제 위치 찾기
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;
                const locPosition = new kakao.maps.LatLng(lat, lon);

                // 내 위치를 나타낼 특별한 별 모양 마커 설정
                const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png';
                const imageSize = new kakao.maps.Size(24, 35);
                const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

                // 내 위치 마커 생성 및 지도에 표시
                new kakao.maps.Marker({
                    map: map,
                    position: locPosition,
                    image: markerImage,
                    title: "내 현재 위치"
                });

                map.setCenter(locPosition); // 내 위치를 지도 중심으로 이동
                searchPharmacies();         // 주변 약국 검색 시작
            }, function(error) {
                console.error("위치 정보를 가져오는데 실패했습니다.", error);
                searchPharmacies(); // GPS 실패 시 기본 좌표에서 검색
            });
        }

        // 지도가 멈췄을 때(idle)마다 주변 약국을 다시 검색하도록 이벤트 등록
        kakao.maps.event.addListener(map, 'idle', searchPharmacies);
    });
}

/**
 * '내 위치' 버튼 클릭 시 호출되는 함수
 */
function moveToCurrentLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const moveLatLon = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.panTo(moveLatLon); // 내 위치로 부드럽게 화면 이동
        });
    }
}

// ==========================================
// 3. 데이터 검색 및 처리 로직
// ==========================================

/**
 * 현재 지도 중심 좌표를 기준으로 약국(PM9 카테고리)을 검색하는 함수
 */
function searchPharmacies() {
    const center = map.getCenter(); // 현재 화면의 중심 좌표 가져오기

    // 이전에 그려진 원이 있다면 지도에서 지우기
    if (currentCircle !== null) {
        currentCircle.setMap(null);
    }

    // 현재 중심에 새로운 탐색 범위 원 그리기
    currentCircle = new kakao.maps.Circle({
        center: center,
        radius: 150, // 150미터 반경
        strokeWeight: 1,
        strokeColor: '#75B8FA',
        strokeOpacity: 0.5,
        fillColor: '#CFE7FF',
        fillOpacity: 0.2
    });
    currentCircle.setMap(map);

    // 카카오 카테고리 검색 실행 (PM9: 약국 코드)
    ps.categorySearch('PM9', placesSearchCB, {
        location: center,      // 중심점 기준 거리 계산
        useMapBounds: true     // 현재 지도 화면 안에서만 검색
    });
}

/**
 * 검색 결과가 도착했을 때 호출되는 콜백 함수
 */
function placesSearchCB(data, status) {
    if (status === kakao.maps.services.Status.OK) {
        removeMarkers(); // 기존 마커 싹 지우기

        // 거리순으로 데이터 정렬 (가까운 곳이 위로)
        data.sort((a, b) => a.distance - b.distance);

        // 검색된 모든 약국에 대해 마커 생성
        for (let i = 0; i < data.length; i++) {
            displayMarker(data[i], i);
        }

        // 상세보기를 하고 있는 중이 아니라면, 왼쪽 사이드바에 10개 목록 업데이트
        if (!isDetailView) {
            displayTopPharmacies(data.slice(0, 10));
        }
    }
}

// ==========================================
// 4. 지도 위 요소 표시 로직 (마커 & 오버레이)
// ==========================================

/**
 * 개별 약국 마커와 그 위의 숫자 번호표를 그리는 함수
 */
function displayMarker(place, index) {
    const markerPosition = new kakao.maps.LatLng(place.y, place.x);

    // 1. 기본 핀 마커 생성
    const marker = new kakao.maps.Marker({
        map: map,
        position: markerPosition
    });
    markers.push(marker);

    // 2. 마커 머리 위에 띄울 숫자 번호표(커스텀 오버레이) 생성
    const content = `
        <div style="background:#2980b9; color:white; border-radius:50%; width:20px; height:20px; 
                    line-height:20px; text-align:center; font-size:12px; font-weight:bold;
                    border:2px solid white; box-shadow:0px 2px 4px rgba(0,0,0,0.3);
                    position:relative; bottom:45px;">
            ${index + 1}
        </div>`;

    const customOverlay = new kakao.maps.CustomOverlay({
        position: markerPosition,
        content: content,
        yAnchor: 1
    });
    customOverlay.setMap(map);
    markers.push(customOverlay);

    // 3. 마커를 클릭했을 때 이벤트 등록
    kakao.maps.event.addListener(marker, 'click', function () {
        infowindow.setContent(`<div style="padding:5px;font-size:12px;">${place.place_name}</div>`);
        infowindow.open(map, marker);
        map.panTo(markerPosition);
        updateSidePanel(place); // 사이드바를 상세보기로 전환
    });
}

// ==========================================
// 5. 사이드바 UI 업데이트 로직
// ==========================================

/**
 * 상위 10개 약국 목록을 사이드바에 표시하는 함수
 */
function displayTopPharmacies(topList) {
    const sidePanel = document.getElementById('side-panel');

    // 레이아웃 틀 생성 (제목 고정, 목록 스크롤)
    sidePanel.innerHTML = `
        <div style="display: flex; flex-direction: column; height: 100%;">
            <div style="padding: 20px; background: #fff; border-bottom: 1px solid #eee;">
                <h3 style="color: #2c3e50; margin: 0;">📍 주변 약국 목록</h3>
                <p style="font-size: 12px; color: #7f8c8d; margin-top: 5px;">현재 지도 중심에서 가까운 순서</p>
            </div>
            <div id="pharmacy-list" style="flex: 1; overflow-y: auto; padding: 15px;"></div>
        </div>
    `;

    const listContainer = document.getElementById('pharmacy-list');
    let cardsHtml = '';

    topList.forEach((place, index) => {
        const distanceStr = place.distance ? `${place.distance}m` : "측정중";
        cardsHtml += `
            <div onclick="focusPharmacy(${index})" style="cursor:pointer; border: 1px solid #eee; padding: 15px; border-radius: 8px; margin-bottom: 12px; background: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.05);">
                <div style="display: flex; justify-content: space-between;">
                    <strong style="color: #2980b9;">${index + 1}. ${place.place_name}</strong>
                    <span style="font-size: 12px; color: #3498db; font-weight: bold;">${distanceStr}</span>
                </div>
                <p style="margin: 8px 0 0 0; font-size: 13px; color: #666;">${place.address_name}</p>
                <p style="margin: 4px 0 0 0; font-size: 12px; color: #95a5a6;">📞 ${place.phone || '번호 없음'}</p>
            </div>
        `;
    });

    listContainer.innerHTML = cardsHtml;
    window.currentTopList = topList; // 클릭 이벤트를 위해 목록 저장
}

/**
 * 특정 약국 클릭 시 해당 위치로 지도 이동 및 상세 보기 출력
 */
function focusPharmacy(index) {
    const place = window.currentTopList[index];
    const moveLatLon = new kakao.maps.LatLng(place.y, place.x);
    map.panTo(moveLatLon);
    updateSidePanel(place);
}

/**
 * 사이드바를 특정 약국의 상세 정보 화면으로 교체
 */
function updateSidePanel(place) {
    isDetailView = true;
    const sidePanel = document.getElementById('side-panel');
    sidePanel.innerHTML = `
        <div style="padding: 20px;">
            <button onclick="backToList()" style="cursor:pointer; border:none; background:#eee; padding:5px 10px; border-radius:4px; margin-bottom:15px;">
                ← 목록으로 돌아가기
            </button>
            <h2 style="color: #2c3e50; margin-bottom: 5px;">${place.place_name}</h2>
            <hr>
            <p>📍 주소: ${place.road_address_name || place.address_name}</p>
            <p>📞 전화: ${place.phone || '정보 없음'}</p>
            <a href="${place.place_url}" target="_blank" style="display:block; text-align:center; padding:15px; background:#ffeb00; text-decoration:none; border-radius:8px; font-weight:bold; color:#000;">
               영업시간 확인(카카오맵으로 이동합니다)
            </a>
        </div>
    `;
}

/**
 * 상세 정보를 닫고 다시 목록 화면으로 복구
 */
function backToList() {
    isDetailView = false;
    searchPharmacies(); // 다시 검색하여 목록 갱신
}

// ==========================================
// 6. 공통 유틸리티 함수
// ==========================================

/**
 * 지도에 표시된 모든 마커와 오버레이를 지우는 함수
 */
function removeMarkers() {
    markers.forEach(marker => marker.setMap(null));
    markers = [];
}