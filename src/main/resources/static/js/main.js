function addEmployeeForm() {
    $('#edit-employee-modal').modal();
}

function editEmployeeForm(id) {
    $.get('/employee/getEmployee?id=' + id, function (employee) {
        $('#save-form #id').val(employee.id);
        $('#save-form #firstName').val(employee.firstName);
        $('#save-form #lastName').val(employee.lastName);
        $('#save-form #email').val(employee.email);
    });

    $('#edit-employee-modal').modal();
}

function deleteEmployeeForm(id) {
    $('#delete-form #deleteId').val(id);
    $('#confirm-delete-modal').modal();
}