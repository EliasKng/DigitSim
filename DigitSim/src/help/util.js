function login() {
	var username = $("#username").val();
	var password = $("#password").val();

	if (username == "kuribo" && password == "immobilien") {
		$("#login").modal("hide");
		$("#login-field").remove();
	}
}