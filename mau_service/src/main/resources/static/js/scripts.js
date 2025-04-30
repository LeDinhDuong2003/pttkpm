// JavaScript for the application
document.addEventListener('DOMContentLoaded', function() {
    // Form validation
    const forms = document.querySelectorAll('.needs-validation');

    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                // Show loading on submit buttons
                const submitBtn = form.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';
                }
            }

            form.classList.add('was-validated');
        }, false);
    });

    // Hiệu ứng hover cho các card
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.15)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
        });
    });

    // Xử lý hiển thị tên file khi chọn file upload
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const fileName = this.files[0]?.name;
            if (fileName) {
                // Hiển thị tên file đã chọn
                const fileNameDisplay = document.createElement('div');
                fileNameDisplay.classList.add('selected-file', 'mt-2');
                fileNameDisplay.innerHTML = `<i class="fas fa-file-video"></i> ${fileName}`;

                // Xóa thông báo cũ nếu có
                const oldDisplay = this.parentElement.querySelector('.selected-file');
                if (oldDisplay) {
                    oldDisplay.remove();
                }

                // Thêm thông báo mới
                this.parentElement.appendChild(fileNameDisplay);
            }
        });
    });

    // Hiệu ứng active cho navbar
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        const linkPath = link.getAttribute('href');
        if (currentPath.startsWith(linkPath) && linkPath !== '/') {
            link.classList.add('active');
            link.style.backgroundColor = 'rgba(255, 255, 255, 0.2)';
        }
    });

    // Thêm data-label cho responsive table
    const tables = document.querySelectorAll('.table');
    tables.forEach(table => {
        const headerTexts = Array.from(table.querySelectorAll('thead th')).map(th => th.textContent.trim());

        const bodyRows = table.querySelectorAll('tbody tr');
        bodyRows.forEach(row => {
            const cells = row.querySelectorAll('td');
            cells.forEach((cell, index) => {
                if (headerTexts[index]) {
                    cell.setAttribute('data-label', headerTexts[index]);
                }
            });
        });
    });

    // Hiệu ứng khi tải trang
    const mainContent = document.querySelector('.container');
    if (mainContent) {
        setTimeout(() => {
            mainContent.classList.add('fade-in');
        }, 100);
    }
});