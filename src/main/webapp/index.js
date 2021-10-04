let currentFilter = '1';
let priorities = new Map();
let categories = new Map();
let items = new Map();
let user = null;

$(document).ready(function () {
    $('#exit').attr("href", cPath + 'logout.do');
    getUser();
    getFilters();
    getPriorities();
    getCategories();
});

function getUser() {
    $.ajax({
        type: 'GET',
        url: cPath + 'user.do',
        dataType: 'json'
    }).done(function (data) {
        user = data;
        $('#username').text(user.username);
        getToDoList();
    }).fail(function (err) {
        console.log(err);
    });
}

function getFilters() {
    $("#selectFilter").empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'filters.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            $('#selectFilter').append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function getPriorities() {
    priorities.clear();
    $("#selectPriority").empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'priorities.do',
        dataType: 'json'
    }).done(function (data) {
        for (let priority of data) {
            priorities.set(priority.id, priority);
            $('#selectPriority').append($('<option>', {
                value: priority.id,
                text: priority.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function getCategories() {
    categories.clear();
    $('#selectCategories').empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'categories.do',
        dataType: 'json'
    }).done(function (data) {
        for (let category of data) {
            categories.set(category.id, category);
            $('#selectCategories').append($('<option>', {
                value: category.id,
                text: category.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function getToDoList() {
    items.clear();
    $("#tbody").empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'items.do?filter_id=' + currentFilter + '&user_id=' + user.id,
        dataType: 'json'
    }).done(function (data) {
        for (let item of data) {
            items.set(item.id, item);
            addRow(item);
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function changeFilter() {
    let filter = $('#selectFilter').find(":selected").val();
    if (currentFilter !==  filter) {
        currentFilter = filter;
        getToDoList();
    }
}

function addItem() {
    $('#notification').text('');
    let lDescription = $('#description').val();
    let priorityId = parseInt($('#selectPriority').find(":selected").val());
    let lCategories = $('#selectCategories').find(":selected").map(function () {
        return $(this).val();
    }).get().map(function (key) {
        return categories.get(parseInt(key));
    });
    $.ajax({
        type: 'POST',
        url: cPath + 'items.do',
        data: JSON.stringify({
            description: lDescription,
            created: new Date().toISOString(),
            priority: priorities.get(priorityId),
            user: user,
            categories: lCategories
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data !== '200 OK') {
            $('#notification').text('Failed to add task!');
            console.log(data);
        } else {
            getToDoList();
        }
    }).fail(function(err) {
        console.log(err);
    });
    $('#description').val('');
}

function changeCheckBox(itemId, checkbox) {
    $('#notification').text('');
    let checked = checkbox.checked;
    let item = items.get(itemId);
    item.done = checked;
    $.ajax({
        type: 'POST',
        url: cPath + 'items.do',
        data: JSON.stringify(item),
        dataType: 'text'
    }).done(function(data) {
        if (data !== '200 OK') {
            item.done = !checked;
            checkbox.checked = !checked;
            $('#notification').text('Failed to change task!');
            console.log(data);
        } else {
            if (checked) {
                $("#item" + itemId).addClass('del');
            } else {
                $("#item" + itemId).removeClass('del');
            }
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function deleteItem(itemId) {
    $('#notification').text('');
    let item = items.get(itemId);
    $.ajax({
        type: 'POST',
        url: cPath + 'deleteItem.do',
        data: JSON.stringify(item),
        dataType: 'text'
    }).done(function(data) {
        if (data !== '200 OK') {
            $('#notification').text('Failed to delete task!');
            console.log(data);
        } else {
            $("#item" + itemId).remove();
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function addRow(item) {
    const formattedDate = getFormattedDate(new Date(item.created));
    const priorityColor = getPriorityColor(item.priority.value);
    const lCategories = item.categories.map(function (category) {
        return category['name'];
    }).join(', ');
    let row = `<tr class="fw-normal${item.done ? " del" : ""}" id="${"item" + item.id}">`
        + `<th><input class="form-check-input me-2" type="checkbox" value="" aria-label="..." onchange="changeCheckBox(${item.id}, this)" ${item.done ? "checked" : ""}/></th>`
        + `<td><span class="ms-2">${formattedDate}</span></td>`
        + `<td class="align-middle">${item.description}</td>`
        + `<td class="align-middle">${lCategories}</td>`
        + `<td class="align-middle"><h6 class="mb-0"><span class="badge ${priorityColor}">${item.priority.name}</span></h6></td>`
        + `<td class="align-middle"><a href=""><i onclick="deleteItem(${item.id}); return false;" class="fa fa-trash mr-3 text-danger"></i></a></td>`
        + `</tr>`;
    $('#table tbody').append(row);
}

function getFormattedDate(date) {
    return date.getDate() + ' '
        + getMonth(date.getMonth()) + ' '
        + date.getFullYear() + ' '
        + ((date.getHours() < 10) ? '0' : '') + date.getHours() + ':'
        + ((date.getMinutes() < 10) ? '0' : '') + date.getMinutes();
}

function getMonth(num) {
    switch (num) {
        case 0 :
            return 'Jan';
        case 1 :
            return 'Feb';
        case 2 :
            return 'Mar';
        case 3 :
            return 'Apr';
        case 4 :
            return 'May';
        case 5 :
            return 'Jun';
        case 6 :
            return 'Jul';
        case 7 :
            return 'Aug';
        case 8 :
            return 'Sep';
        case 9 :
            return 'Oct';
        case 10 :
            return 'Nov';
        case 11 :
            return 'Dec';
        default :
            return ''
    }
}

function getPriorityColor(value) {
    switch (value) {
        case 1 :
            return 'bg-danger';
        case 2 :
            return 'bg-warning';
        case 3 :
            return 'bg-success';
        default :
            return 'bg-white';
    }
}
