document.addEventListener("DOMContentLoaded", () => {

    const emailId = document.getElementById("emailId");
    const emailDomain = document.getElementById("emailDomain");
    const domainSelect = document.getElementById("domainSelect");
    const emailHidden = document.getElementById("email");

    function updateEmail() {
        if (emailId.value && emailDomain.value) {
            emailHidden.value = `${emailId.value}@${emailDomain.value}`;
        } else {
            emailHidden.value = "";
        }
    }

    domainSelect.addEventListener("change", function () {
        if (this.value) {
            emailDomain.value = this.value;
            emailDomain.readOnly = true;
        } else {
            // 직접 입력 선택 시
            emailDomain.readOnly = false;
            emailDomain.value = "";
            emailDomain.focus();
        }
        updateEmail();
    });

    emailId.addEventListener("input", updateEmail);
    emailDomain.addEventListener("input", updateEmail);
});
