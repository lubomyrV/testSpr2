$(document).ready(function () {
	console.log("document: ready");
	getRoles();
	
});

function getRoles(){
	$.ajax({
    	type : "GET",
		url : "/getRoles",
		success : function(result) {
			if(result.status == "Done"){
				localStorage.setItem("roles", result.data);
			}
		},
		error : function(e) {
			console.log("getRoles ERROR: ", e);
		}
    });
}

function updateUser(){
	console.log("updateUser");
	let oldUserInfo = localStorage.getItem("userForUpdate");
	console.log("oldUserInfo "+oldUserInfo);
	
	let roles = JSON.parse(localStorage.getItem("roles"));
	let roleMap = {};
	for (var i = 0; i < roles.length; i++) {
		let isRoleChecked = document.getElementById('role'+roles[i]).checked;
		roleMap[roles[i]] = isRoleChecked;
	}
	
	
	let userId = $("#userId").val();
	let isEnabled = document.getElementById('enabled'+userId).checked;
    let user = {}
    user["id"] = userId;
    user["enabled"] = isEnabled;
    user["roleMap"] = roleMap;
    localStorage.removeItem("userForUpdate");
    document.getElementById("myModal").style.display = "none";
    console.log("user="+JSON.stringify(user));
    
    
    $.ajax({
    	type : "POST",
		contentType : "application/json",
		url : "/updateUser",
		data : JSON.stringify(user),
		dataType : 'json',
		success : function(result) {
			if(result.status == "Done"){
				console.log("response userEnabled="+result.data);
			}
		},
		error : function(e) {
			console.log("updateUser ERROR: ", e);
		}
    });
    
}

function editUser (userId){
    console.log("editUser="+userId);
    
    $.ajax({
  		type : "POST",
		contentType : "text/plain",
		url : "/getUserInfo",
		data : JSON.stringify(userId),
		dataType : "json",
		success : function(result) {
			// open modal window
			document.getElementById("myModal").style.display = "block";
			if(result.status == "Done"){
			  	console.log("data="+result.data);
			  	let user = JSON.parse(result.data);
			  	$("#editUser").empty();
			  	let roles = JSON.parse(localStorage.getItem("roles"));
			  	renderUser(user, roles);
			}
		},
		error : function(e) {
		  console.log("editUser ERROR: ", e);
		}
    });
}

function renderUser(data, roles) {
	
	localStorage.setItem("userForUpdate", data);
	
	let thisUser = $("#thisUser").val();
	$('<input type="hidden" id="userId" name="userId" value="'+data[0].id+'" /> ').appendTo("#editUser");
	$('<label for="username">Username:</label>').appendTo("#editUser");
	$('<input value="'+data[0].username+'" type="text" name="username" readonly /> <br/>').appendTo("#editUser");
	$('<label for="enabled">Enable:</label>').appendTo("#editUser");
	if(data[0].username != thisUser){
		$('<input name="enabled" value="'+data[0].enabled+'" type="checkbox" '+ (data[0].enabled ? "checked" : "")+' id="enabled'+data[0].id+'" /> <br/>').appendTo("#editUser");	    	    	
	} else {
		$('<input disabled checked name="enabled" value="'+data[0].enabled+'" type="checkbox" id="enabled'+data[0].id+'" /> <br/><br/>').appendTo("#editUser");	    	    	
	}
	$('<label>Choose roles:</label> <br/>').appendTo("#editUser");
	let userRoles = [];
	for (let i = 1; i < data.length; i++){
		userRoles.push(data[i].name);
	}
	for (let i = 0; i < roles.length; i++){
		if(data[0].username != thisUser){
			$('<label for="role">'+roles[i]+':</label>').appendTo("#editUser");
			$('<input name="role" value="'+roles[i]+'" type="checkbox" '+ (userRoles.includes(roles[i]) ? "checked" : "")+' id="role'+roles[i]+'" /> <br/>').appendTo("#editUser");		
		} else {
			if(roles[i] == "ROLE_ADMIN"){
				$('<label for="role">'+roles[i]+':</label>').appendTo("#editUser");
				$('<input name="role" disabled checked value="'+roles[i]+'" type="checkbox" id="role'+roles[i]+'" /> <br/>').appendTo("#editUser");			
			} else {
				$('<label for="role">'+roles[i]+':</label>').appendTo("#editUser");
				$('<input name="role" value="'+roles[i]+'" type="checkbox" '+ (userRoles.includes(roles[i]) ? "checked" : "")+' id="role'+roles[i]+'" /> <br/>').appendTo("#editUser");			
			}
		}
	}
   
}

function search(){
      //get data
	let usernameOrEmail = $("#searchUser").val();
    //console.log("usernameOrEmail="+usernameOrEmail);
    if(usernameOrEmail.length == 0){
    	$("#showAll").empty();
    }
    
    if(usernameOrEmail.length > 2){
    	// send request
		$.ajax({
      		type : "POST",
			contentType : "text/plain",
			url : "/search",
			data : usernameOrEmail,
			dataType : "json",
			success : function(result) {
			  if(result.status == "Done"){
			  	//console.log("data="+result.data);
			  	let objList = JSON.parse(result.data);
			  	$("#showAll").empty();
			  	render(objList);
			  }
			},
			error : function(e) {
			  console.log("search ERROR: ", e);
			}
	    });
    }
}

function render(data) {
	
	$('<table>').appendTo("#showAll");
		$('<tr>').appendTo("#showAll");
			$('<th>Username</th>').appendTo("#showAll");
			$('<th>Email</th>').appendTo("#showAll");
			$('<th>Edit</th>').appendTo("#showAll");
		$('</tr>').appendTo("#showAll");
	  
    for (let i = 0; i < data.length; i++){
    	$('<tr>').appendTo("#showAll");
    	    $('<td>'+data[i].username+'</td>').appendTo("#showAll");
    	    $('<td>'+data[i].email+'</td>').appendTo("#showAll");
	    	$('<td><input value="edit" type="button" onclick="editUser('+data[i].id+')"/></td>').appendTo("#showAll");	    	    	
        $('</tr>').appendTo("#showAll");
    }
    $('</table>').appendTo("#showAll");
}

function callFunc (recordId){
	//Get
	let isEnabled = document.getElementById('enabled'+recordId).checked;
    console.log("recordId="+recordId+', isEnabled='+isEnabled);
    let user = {}
    user["id"] = recordId;
    user["enabled"] = isEnabled;
    console.log("send userEnabled="+user);
    $.ajax({
  		type : "POST",
		contentType : "application/json",
		url : "/userEnabled",
		data : JSON.stringify(user),
		dataType : 'json',
		success : function(result) {
		  if(result.status == "Done"){
		  	console.log("response userEnabled="+result.data);
		  }
		},
		error : function(e) {
		  console.log("callFunc ERROR: ", e);
		}
    });
}