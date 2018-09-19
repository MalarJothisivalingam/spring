<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Connection</title>

<style type="text/css">
#source {
	float: left;
	border: 2px solid black;
}

#destination {
	float: right;
	border: 2px solid black;
}
span.error {
	color: red;
	margin-left: 10px;
}

form label {
  display: inline-block;
  width: 100px;
}
 
form div {
  margin-bottom: 10px;
}
 
.error {
  color: red;
  margin-left: 5px;
}
 
label.error {
  display: inline;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	  $('#sourceTable').submit(function(e) {
	    e.preventDefault();
	    var count=0;
	    var driverName = $('#driverName').val();
	    var connectionString = $('#connectionString').val();
	    var userName = $('#userName').val();
	    var password = $('#password').val();

	    $(".error").remove();

	    if (driverName.length < 1) {
	      $('#driverName').after('<span class="error">This field is required</span>');
	      count=1;
	    }
	    if (connectionString.length < 1) {
	      $('#connectionString').after('<span class="error">This field is required</span>');
	      count=1;
	    }
	    if (userName.length < 1) {
	      $('#userName').after('<span class="error">This field is required</span>');
	      count=1;
	    }
	    if (password !="") {
	      $('#password').after('<span class="error">Password must be empty</span>');
	      count=1;
	    }
	    if(count==0)
	    	{
	    	sourceFunction();
	    	destinationfunction();
	    	}
	  });
	  $('#destinationTable').submit(function(e) {
		    e.preventDefault();
		    var count=0;
		    var driverName = $('#DdriverName').val();
		    var connectionString = $('#DconnectionString').val();
		    var userName = $('#DuserName').val();
		    var password = $('#Dpassword').val();

		    $(".error").remove();

		    if (driverName.length < 1) {
		      $('#DdriverName').after('<span class="error">This field is required</span>');
		      count=1;
		    }
		    if (connectionString.length < 1) {
		      $('#DconnectionString').after('<span class="error">This field is required</span>');
		      count=1;
		    }
		    if (userName.length < 1) {
		      $('#DuserName').after('<span class="error">This field is required</span>');
		      count=1;
		    }
		    if (password !="") {
		      $('#Dpassword').after('<span class="error">Password must be empty</span>');
		      count=1;
		    }
		    if(count==0)
		    	{
		    	DestinationFunction();
		    	}
		  });

	});
	function sourceFunction() {
		$.ajax({
			type : 'post',
			url : 'loginSource',
			data : $('form[name="sourceconnection"]').serialize(),
			success : function(response) {
				if (response.validated) {
					//Set response				
					$('#resultContainer pre code').text(
							JSON.stringify(response.source));
					$('#resultContainer').show();
				}
			}
		});

	};
	function DestinationFunction() {
		$.ajax({
			type : 'post',
			url : 'loginDestination',
			data : $('form[name="destinationconnection"]').serialize(),
			success : function(response) {
				if (response.validated) {
					//Set response				
					$('#resultContainer1 pre code').text(
							JSON.stringify(response.destination));
					$('#resultContainer1').show();
				}
			}
		});

	};
</script>
</head>
<body>
	<hr />
	<form id="sourceTable" name="sourceconnection">
		<div class="container" id="source">
			<h1 align="center">Source</h1>
			<div id="resultContainer" style="display: none;">

				<h4 style="color: green;">Connected Successfully</h4>
				<pre style="color: green;">
    <code></code>
   </pre>
				<hr />
			</div>
			<div class="form-group">
				<label for="driverName">DriverName:</label> <input type="text"
					name="driverName" id="driverName" value="org.h2.Driver" />
			</div>
			<div class="form-group">
				<label for="connectionString">connectionString:</label> <input
					type="text" name="connectionString" id="connectionString"
					value="jdbc:h2:tcp://localhost/~/test" />
			</div>
			<div class="form-group">
				<label for="userName">userName:</label> <input type="text"
					name="userName" id="userName" value="sa" />
			</div>
			<div class="form-group">
				<label for="password">password:</label> <input type="password"
					name="password" id="password" value="" />
			</div>
			<button type="submit"
				class="btn btn-success">Connect</button>

		</div>
	</form>
	<form id="destinationTable" name="destinationconnection">
		<div class="container" id="destination">
			<h1 align="center">Destination</h1>
			<div id="resultContainer1" style="display: none;">

				<h4 style="color: green;">Connected Successfully</h4>
				<pre style="color: green;">
    <code></code>
   </pre>
				<hr />
			</div>
			<div class="form-group">
				<label for="DdriverName">DriverName:</label> <input type="text"
					name="DdriverName" id="DdriverName" value="org.h2.Driver" />
			</div>
			<div class="form-group">
				<label for="DconnectionString">connectionString:</label> <input
					type="text" name="DconnectionString" id="DconnectionString"
					value="jdbc:h2:tcp://localhost/~/test" />
			</div>
			<div class="form-group">
				<label for="DuserName">userName:</label> <input type="text"
					name="DuserName" id="DuserName" value="sa" />
			</div>
			<div class="form-group">
				<label for="Dpassword">password:</label> <input type="password"
					name="Dpassword" id="Dpassword" value="" />
			</div>
			<button type="submit"
				class="btn btn-success">Connect</button>

		</div>
	</form>
	<form action="table" id="table">
	<div class="container" align="center" id="table">
	<button type="submit" class="btn btn-success">Table</button>
	</div>
	</form>
	
</body>
</html>