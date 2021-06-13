initSortTable();

$(document).ready(function () {
    $("#filterInput").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#deliveryTable tbody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});
let section = document.getElementById("tableSection");
//set fixed height table
section.style.height = section.offsetHeight + 'px';

function calculatePrice(url) {
    console.log(url)
    $.get(url,
        {
            from: document.getElementById("from").value,
            to: document.getElementById("to").value,
            weight: document.getElementById("weight").value,
            length: document.getElementById("length").value,
            width: document.getElementById("width").value,
            height: document.getElementById("height").value
        },
        function (data) {
            let resp = '';
            try {
                resp = JSON.parse(data);
            } catch (e) {
                alert("Invalid input data!");
                return;
            }
            console.log(resp.start)
            console.log(resp.finish)
            document.getElementById("price").innerHTML = 'Calculated price: ' + resp.price;
            let inputPrice = document.getElementById("inputPrice");
            if (inputPrice != null) {
                inputPrice.value = resp.price;
            }
            printRoute(resp.lngLat)
        });
}

function unlockEdit(id) {
    document.getElementById(id).disabled = false;
}

function lockEdit(id) {
    document.getElementById(id).disabled = true;
}

function updateStatusDelivery(status, deliveryId, url) {
    let deliveryDate = document.getElementById('deliveryDate-' + deliveryId).value;
    if (deliveryDate !== '') {
        $.post(url, {
            delivery: deliveryId,
            status: status
        }, function (data) {
            let resp = JSON.parse(data);
            console.log(resp);
            if (resp.status !== 200) {
                alert("Error update status delivery. Info: status = " + status + " deliveryId = " + deliveryId)
            }
        });
    } else {
        alert("delivery date must be posted");
        document.getElementById('deliveryStatus-' + deliveryId).value = 'CREATED';
    }
}

function updateDeliveryDate(url, deliveryId) {
    let deliveryDate = document.getElementById('deliveryDate-' + deliveryId).value;
    if (deliveryDate !== '') {
        $.post(url, {
            delivery: deliveryId,
            deliveryDate: deliveryDate
        }, function (data) {
            let resp = JSON.parse(data);
            console.log(resp);
            if (resp.status !== 200) {
                alert("Error update delivery date. Info: deliveryDate = " + deliveryDate + " deliveryId = " + deliveryId)
            }
        });
    }
}

function initSortTable() {
    const getCellValue = (tr, idx) => tr.children[idx].innerText || tr.children[idx].textContent;

    const comparer = (idx, asc) => (a, b) => ((v1, v2) =>
            v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2) ? v1 - v2 : v1.toString().localeCompare(v2)
    )(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

    document.querySelectorAll('th').forEach(th => th.addEventListener('click', (() => {
        const tbody = th.closest('table').querySelector('tbody');
        Array.from(tbody.querySelectorAll('tr'))
            .sort(comparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
            .forEach(tr => tbody.appendChild(tr));
    })));
}

// function filterTable() {
//     var input, filter, tbody, i;
//     input = document.getElementById("filterInput");
//     filter = input.value.toUpperCase();
//     tbody = document.querySelector("#deliveryTable tbody");
//     var rows = tbody.getElementsByTagName("tr");
//     for (i = 0; i < rows.length; i++) {
//         var cells = rows[i].getElementsByTagName("td");
//         var j;
//         var rowContainsFilter = false;
//         for (j = 0; j < cells.length; j++) {
//             if (cells[j]) {
//                 if (cells[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
//                     rowContainsFilter = true;
//                     break;
//                 }
//             }
//         }
//
//         if (!rowContainsFilter) {
//             rows[i].style.display = "none";
//         } else {
//             rows[i].style.display = "";
//         }
//         tbody.style.height = height;
//     }
// }