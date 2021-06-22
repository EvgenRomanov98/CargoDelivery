window.addEventListener('load', pagination());

function pagination(colName = 'id', trigger = false, asc = true, filterObj = {fromName: '', toName: ''}) {
    let pageSize = 5;
    rootLocation = $('#homeLocation').attr('href');
    let active = document.querySelector('li.active a');
    let page = active ? active.innerText : 1;
    let url = rootLocation + 'paginationDelivery';
    $('#pagination-container').pagination({
        dataSource: url,
        locator: 'deliveries',
        triggerPagingOnInit: trigger,
        totalNumber: sessionStorage.getItem("totalNumber"),
        pageSize: pageSize,
        pageNumber: page,
        className: 'paginationjs-small d-flex justify-content-end mx-5 pb-3',
        ajax: {
            type: 'POST',
            data: {
                orderBy: colName,
                ascending: asc,
                filter: filterObj,
                userId: sessionStorage.getItem("userId")
            }
        },
        callback: function (data, pagination) {
            var html = templating(data);
            $('#data-container').html(html);
        }
    })

    function templating(data) {
        let html = '';
        console.log(data);
        $.each(data, function (index, item) {
            let info = '';
            if (item.status === 'CREATED') {
                info = 'Wait approve manager';
            } else {
                info = `<a href='` + rootLocation + `getReceipt?idDelivery=` + item.id + `'>
                          <button class="btn btn-info">` + sessionStorage.getItem('button.get.receipt') + `</button>
                        </a>`
            }
            html += `<tr>
                <td>` + item.cargo.description + `</td>
                <td>
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">` + item.fromName + `</div>
                        <div class="flex-row flex-wrap text-muted fs-6">` + item.whence + `</div>
                    </div>
                </td>
                <td>
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">` + item.toName + `</div>
                        <div class="flex-row flex-wrap text-muted fs-6">` + item.whither + `</div>
                    </div>
                </td>
                <td>` + item.createDate + `</td>
                <td>` + (item.deliveryDate ? item.deliveryDate : '') + `</td>
                <td>` + item.distance + `</td>
                <td>` + item.price + `</td>
                <td>` + item.cargo.weight + `</td>
                <td>` + item.cargo.length + `</td>
                <td>` + item.cargo.width + `</td>
                <td>` + item.cargo.height + `</td>
                <td>` + item.status + `</td>
                <td>` + info + `</td>
            </tr>`;
        });
        return html;
    }
}

initSortAndFilterTable(true);

function initSortAndFilterTable(asc) {
    let regExp = /^(fromName|toName)$/;
    let filterObj = {};
    document.querySelectorAll('th span').forEach(span => span.addEventListener('click', (() => {
        asc = !asc
        pagination(span.getAttribute('col'), true, asc, filterObj);
    })));

    document.querySelectorAll('th input').forEach(input => input.addEventListener('change', (() => {
        if (!regExp.exec(input.getAttribute('col'))) {
            return;
        }
        filterObj[input.getAttribute('col')] = input.value
        pagination(input.getAttribute('col'), true, asc, filterObj);
    })));
}