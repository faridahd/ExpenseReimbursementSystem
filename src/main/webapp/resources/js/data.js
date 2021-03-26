window.onload = function(){
data();
function data(){

	fetch(`http://localhost:8080/ReimbursementProject/data`).then(
		function(response) {
			return response.json();
		}, function() {
			console.log('Error');
		}
	).then(function(myJSON){
		console.log(myJSON);
		DomManip(myJSON);
	})
}

function DomManip(mydata){
	let table = document.getElementById("tbody");
	for(let i=0; i<mydata.length; i++){
		let td1 = document.createElement("td");;
		let td2 = document.createElement("td");
		let td3 = document.createElement("td");
		let td4 = document.createElement("td");
		let td5 = document.createElement("td");
		let td6 = document.createElement("td");
		let td7 = document.createElement("td");
		let td8 = document.createElement("td");
		let td9 = document.createElement("td");
		let tr = document.createElement("tr")
		td1.innerText = mydata[i].reimb_id;
		tr.appendChild(td1);
		td2.innerText = mydata[i].amount;
		tr.appendChild(td2);
		td3.innerText = mydata[i].type;
		tr.appendChild(td3);
		td4.innerText = mydata[i].dateSubmitted;
		tr.appendChild(td4);
		td5.innerText = mydata[i].dateResolved;
		tr.appendChild(td5);
		td6.innerText = mydata[i].description;
		tr.appendChild(td6);
		td7.innerText = mydata[i].author.username;
		tr.appendChild(td7);
		if(mydata[i].resolver != null){
			td8.innerText = mydata[i].resolver.username;
		}		
		tr.appendChild(td8);
		td9.innerText = mydata[i].status;
		tr.appendChild(td9);
		table.appendChild(tr);
		console.log(mydata[i].reimb_id);
	}
}
}