document.addEventListener('DOMContentLoaded', function() {
    
    const heroImageArea = document.getElementById('heroImageArea');
    const imageInput = document.getElementById('imageInput');
    
    // URL 병원 ID 추출 (예: /hospitals/A1123456 -> A1123456)
    // 사용자가 병원측 사용자인지 일반 사용자인지 구분을 위하여 사용(이미지수정은 병원측사용자만 가능하게 병원id확인용)
    const pathParts = window.location.pathname.split('/');
    const hospitalId = pathParts[pathParts.length - 1];

    if (heroImageArea && imageInput) {
        
        // 클릭시 파일 선택창 열기
        heroImageArea.addEventListener('click', function() {
            imageInput.click();
        });

        imageInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (!file) return;

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드할 수 있습니다.');
                return;
            }
            const formData = new FormData();
            formData.append('file', file);

            fetch(`/hospitals/${hospitalId}/image`, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('업로드 실패');
                }
            })
            .then(data => {
                alert('대표 이미지가 변경되었습니다.');
                window.location.reload(); //갱신
            })
            .catch(error => {
                console.error('Error:', error);
                alert('이미지 변경 중 오류가 발생했습니다.');
            });
        });
    }

});
