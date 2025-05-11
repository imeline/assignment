// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('Spring Boot 예제 애플리케이션이 로드되었습니다.');

    // 현재 활성화된 네비게이션 링크에 active 클래스 추가
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || (href !== '/' && currentPath.startsWith(href))) {
            link.classList.add('active');
        }
    });

    // 폼 유효성 검사 강화
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
});
