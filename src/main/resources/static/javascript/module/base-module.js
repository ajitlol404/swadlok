const toastAlert = document.getElementById("toast_alert");
const toastAlertInstance = bootstrap.Toast.getOrCreateInstance(toastAlert);
const signOutBtn = document.getElementById("sign_out_btn");

function handleSignOut(e) {
    e.preventDefault();
    const confirmed = window.confirm("Are you sure you want to sign out?");
    if (confirmed) {
        document.sign_out_form.submit();
    }
}

export { toastAlert, toastAlertInstance, signOutBtn, handleSignOut };
