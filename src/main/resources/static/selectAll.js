function check(divid, source) {

  var checks = document.querySelectorAll('#' + divid + ' input[type="number"]');

  for(var i=0;i<checks.length;i++){
    checks[i].disabled = !source.checked;
    checks[i].value=1;
   
    if(checks[i].disabled){
      checks[i].value=0;

    }
  }
}

var div = document.getElementById('uniqueID');
div.id = 'id_' + Math.floor(Math.random() * 9e99).toString(36);

function reset() {
	var inputs = document.getElementsByTagName('input');
	for(i = 0; i < inputs.length; i++){
		if(inputs[i] == "number"){
			inputs[i].disabled = true;		
		}    
	}
}