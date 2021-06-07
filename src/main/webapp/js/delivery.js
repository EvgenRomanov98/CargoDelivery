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
            let resp = JSON.parse(data);
            document.getElementById("price").innerHTML = 'Calculated price: ' + resp.price
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
        document.getElementById('deliveryStatus-'+deliveryId).value = 'CREATED';
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