// The root URL for the RESTful services
var rootURL = "http://localhost:8080/JagexCodingExercise/rest/packages";

var currentPackage;

// Retrieve package list when application starts
findAll();

// Nothing to delete in initial application state
$('#btnDelete').hide();

// Register listeners
$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

// Trigger search when pressing 'Return' on search key input field
$('#searchKey').keypress(function(e) {
	if (e.which == 13) {
		search($('#searchKey').val());
		e.preventDefault();
		return false;
	}
});

$('#btnAdd').click(function() {
	newPackage();
	return false;
});

$('#btnSave').click(function() {
	if ($('#packageId').val() == '')
		addPackage();
	else
		updatePackage();
	return false;
});

$('#btnDelete').click(function() {
	deletePackage();
	return false;
});

$('#packageList a').live('click', function() {
	findById($(this).data('identity'));
});

$('.cb').on('change', function() { // on change of state
	UpdateCost();
});

function updateCost() {
	var sum = 0;
	var gn, elem;
	$('.cb:checked').each(function() {
		sum += Number($(this).val());
	})

	$('#totalcost').val(sum.toFixed(2));
}

function getProducts() {
	var products = $("input[name='products']:checked").map(function() {
		return this.value;
	}).get().join(', ');
}

function search(searchKey) {
	if (searchKey == '')
		findAll();
	else
		findByName(searchKey);
}

function newPackage() {
	$('#btnDelete').hide();
	currentPackage = {};
	renderDetails(currentPackage); // Display empty form
}

function findAll() {
	console.log('findAll');
	$.ajax({
		type : 'GET',
		url : rootURL,
		dataType : "json", // data type of response
		success : renderList
	});
}

function findByName(searchKey) {
	console.log('findByName: ' + searchKey);
	$.ajax({
		type : 'GET',
		url : rootURL + '/search/' + searchKey,
		dataType : "json",
		success : renderList
	});
}

function findById(id) {
	console.log('findById: ' + id);
	$.ajax({
		type : 'GET',
		url : rootURL + '/' + id,
		dataType : "json",
		success : function(data) {
			$('#btnDelete').show();
			console.log('findById success: ' + data.name);
			currentPackage = data;
			renderDetails(currentPackage);
		}
	});
}

// Create a new package

function addPackage() {
	console.log('addPackage');
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : rootURL,
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Package created successfully');
			$('#btnDelete').show();
			$('#packageId').val(data.id);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('addPackage error: ' + textStatus);
		}
	});
}

// Update a package

function updatePackage() {
	console.log('updatePackage');
	$.ajax({
		type : 'PUT',
		contentType : 'application/json',
		url : rootURL + '/' + $('#packageId').val(),
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Package updated successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('updatePackage error: ' + textStatus);
		}
	});
}

// Delete a package

function deletePackage() {
	console.log('deletePackage');
	$.ajax({
		type : 'DELETE',
		url : rootURL + '/' + $('#packageId').val(),
		success : function(data, textStatus, jqXHR) {
			alert('Package deleted successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('deletePackage error');
		}
	});
}

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an
	// object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$('#packageList li').remove();
	$.each(list, function(index, pkg) {
		$('#packageList').append(
				'<li><a href="#" data-identity="' + pkg.id + '">' + pkg.name
						+ '</a></li>');
	});
}

function renderDetails(pkg) {
	$('#packageId').val(pkg.id);
	$('#name').val(pkg.name);
	$('#products').val(pkg.products);

	$('#totalcost').val(pkg.totalcost);

	$('#description').val(pkg.description);
}

// Helper function to serialize all the form fields into a JSON string

function formToJSON() {
	var packageId = $('#packageId').val();
	var array = [];
	$("input:checkbox[name=products]:checked").each(function() {
		array.push($(this).val());
	});
	return JSON.stringify({
		"id" : packageId == "" ? null : packageId,
		"name" : $('#name').val(),
		"products" : array,
		"totalcost" : $('#totalcost').val(),
		"description" : $('#description').val()
	});
}
