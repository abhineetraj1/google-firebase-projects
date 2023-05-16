document.addEventListener('DOMContentLoaded', function() {
  var registerBtn = document.getElementById('registerBtn');
  var loginBtn = document.getElementById('loginBtn');
  registerBtn.addEventListener('click', function(event) {
    event.preventDefault();
    navigator.credentials.create({
      publicKey: {
        challenge: new Uint8Array(32),
        rp: { name: 'Sample Service' }, // Replace with your service name
        user: { displayName: 'User' },
        pubKeyCredParams: [{ type: 'public-key', alg: -7 }]
      }
    }).then(function(credential) {
      alert("Credentials added.")
      console.log(credential);
    }).catch(function(error) {
      alert("Error occured, check console for more info");
      console.log(error);
    });
  });
  loginBtn.addEventListener('click', function(event) {
    event.preventDefault();
    navigator.credentials.get({
      publicKey: {challenge: new Uint8Array(32),allowCredentials: []}
    }).then(function(credential) {
      alert("Verification complete, check console for credentials");
      console.log(credential);
    }).catch(function(error) {
      alert("Error occured, check console for more info");
      console.log(error);
    });
  });
});