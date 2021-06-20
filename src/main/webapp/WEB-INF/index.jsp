<!DOCTYPE html>
<%@ page import="ua.epam.cargo_delivery.model.db.User" %>
<%@ page import="java.security.Permissions" %>
<%@ page import="ua.epam.cargo_delivery.model.Action" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/bootstrap-5.0.1-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.css" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/map.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/pagination.css"/>" rel="stylesheet">
    <link rel="icon" href='<c:url value="/favicon.ico" />' type="image/x-icon">
    <title>Main</title>
</head>
<body>
<nav class="navbar sticky-top navbar-light bg-light">
    <div class="container-fluid">
        <div>
            <a id="homeLocation" class="navbar-brand" href="<c:url value="/"/>">123 Delivery</a>
            <!-- Option 1: Bootstrap Bundle with Popper -->
            <!-- Button trigger modal -->
        </div>
        <div class="justify-content-end">
            <c:if test="${sessionScope.loggedUser.role == 'USER'}">
                <button type="button" class="btn btn-outline-info btn-sm text-dark" data-bs-toggle="modal"
                        data-bs-target="#staticBackdrop">
                    Authorization
                </button>
                <button type="button" class="btn btn-outline-info btn-sm text-dark" data-bs-toggle="modal"
                        data-bs-target="#registrationStaticBackdrop">
                    Registration
                </button>
            </c:if>
            <c:if test="${sessionScope.loggedUser.role != 'USER'}">
                <a href="<c:url value="/privateOffice"/>" class="btn btn-outline-info btn-sm">
                        ${loggedUser.name} ${loggedUser.surname}
                </a>
                <a href="<c:url value="/signOut"/>" class="btn btn-outline-dark btn-sm">
                    Sign out
                </a>
            </c:if>
        </div>
    </div>
</nav>
<section class="container my-5">
    <div class="row align-items-center">
        <h4 class="col">We deliver cargoes in and between areas:</h4>
        <div class="col">
            <ul class="list-group list-group-flush">
                <c:forEach items="${sessionScope.availableRegions}" var="region">
                    <li class="list-group-item text-center">${region.name}</li>
                </c:forEach>
            </ul>
        </div>
    </div>
</section>
<div class="container-fluid">
    <div class="row">
        <div id="map" class="col"></div>
        <form class="col container-fluid" action="<c:url value="/privateOffice"/>" method="post">
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="from" class="form-control" id="from" placeholder="Longitude,Latitude From">
                    <label for="from" style="left: auto">Longitude,Latitude From</label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="to" class="form-control" id="to" placeholder="Longitude,Latitude To">
                    <label for="to" style="left: auto">Longitude,Latitude To</label>
                </div>
                <label><input name="fromRegionId" id="fromRegionId" hidden></label>
                <label><input name="toRegionId" id="toRegionId" hidden></label>
            </section>
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="fromName" class="form-control" id="fromName" placeholder="City from">
                    <label for="fromName" style="left: auto">City from</label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="toName" class="form-control" id="toName" placeholder="City to">
                    <label for="toName" style="left: auto">City to</label>
                </div>
            </section>
            <section class="row">
                <label class="col">Weight, kg
                    <select name="weight" id="weight" class="form-select overflow-hidden" multiple>
                        <option value="100" selected><100</option>
                        <option value="500">100 - 500</option>
                        <option value="1000">500 - 1000</option>
                        <option value="1500">1000 - 1500</option>
                    </select>
                </label>
                <label class="col">Length, mm
                    <select name="length" id="length" class="form-select overflow-hidden" multiple>
                        <option value="1000" selected><1000</option>
                        <option value="2000">1000 - 2000</option>
                        <option value="3000">2000 - 3000</option>
                        <option value="4000">3000 - 3000</option>
                    </select>
                </label>
                <label class="col">Width, mm
                    <select name="width" id="width" class="form-select overflow-hidden" multiple>
                        <option value="400" selected><400</option>
                        <option value="900">400 - 900</option>
                        <option value="1400">900 - 1400</option>
                        <option value="1700">1400 - 1700</option>
                    </select>
                </label>
                <label class="col">Height, mm
                    <select name="height" id="height" class="form-select overflow-hidden" multiple>
                        <option value="400" selected>>400</option>
                        <option value="900">400 - 900</option>
                        <option value="1400">900 - 1400</option>
                        <option value="1750">1400 - 1750</option>
                    </select>
                </label>
            </section>
            <section class="row align-items-center pt-1">
                <label hidden><input id="inputPrice" name="price"></label>
                <h6 id="price" class="col-6 my-0"></h6>
                <div class="col-6 d-flex flex-row-reverse">
                    <c:if test="${sessionScope.loggedUser.role.checkPermission(Action.CREATE_DELIVERY)}">
                        <button type="submit"
                                class="btn btn-outline-success col-5 col-xxl-4 ms-2">
                            Create
                        </button>
                    </c:if>
                    <button class="btn btn-outline-info col-5 col-xxl-4" type="button"
                            onclick="calculatePrice('<c:url value="/calculatePrice"/>')">
                        Calculate
                    </button>
                </div>
            </section>
        </form>
    </div>
</div>
<section id="tableSection">
    <table id="deliveryTable" class="table caption-top">
        <caption>List of deliveries</caption>
        <thead class="table-dark">
        <tr class="align-middle">
            <th scope="col">
                <div class="container-fluid row m-0 p-0 align-items-center">
                    <span class="col-7" col="fromName">From</span>
                    <div class="col">
                        <div class="input-group input-group-sm">
                            <label for="filterWhence" class="input-group-text">
                                <img src="<c:url value="/icons/filter.svg"/>" alt="Filter">
                            </label>
                            <input id="filterWhence" col="fromName" type="text" class="form-control"
                                   placeholder="From filter by">
                        </div>
                    </div>
                </div>
            </th>
            <th scope="col">
                <div class="container-fluid row m-0 p-0 align-items-center">
                    <span class="col-7" col="toName">To</span>
                    <div class="col">
                        <div class="input-group input-group-sm">
                            <label for="filterWhither" class="input-group-text">
                                <img src="<c:url value="/icons/filter.svg"/>" alt="Filter">
                            </label>
                            <input id="filterWhither" col="toName" type="text" class="form-control"
                                   placeholder="To filter by">
                        </div>
                    </div>
                </div>
            </th>
            <th scope="col"><span col="distance" class="d-flex">Distance, m</span></th>
            <th scope="col"><span col="price" class="d-flex">Price, UAH</span></th>
        </tr>
        </thead>
        <tbody id="data-container" class="container-fluid">
        <c:forEach items="${sessionScope.deliveries}" var="delivery">
            <tr>
                <td class="table-tb-width-40">
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">${delivery.fromName}</div>
                        <div class="flex-row flex-wrap text-muted fs-6">${delivery.whence}</div>
                    </div>
                </td>
                <td class="table-tb-width-40">
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">${delivery.toName}</div>
                        <div class="flex-row flex-wrap text-muted fs-6">${delivery.whither}</div>
                    </div>
                </td>
                <td>${delivery.distance}</td>
                <td>${delivery.price}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div id="pagination-container"></div>
</section>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabel">Authorization</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="authorization" method="POST" class="modal-body">

                <div class="form-floating">
                    <input name="email" type="email" class="form-control" id="authEmail"
                           placeholder="name@example.com" pattern="^\S+@\S+\.\S+$" required>
                    <label for="authEmail">Email address</label>
                </div>
                <div class="form-floating">
                    <input name="password" type="password" class="form-control" id="authPassword"
                           placeholder="Password" required>
                    <label for="authPassword">Password</label>
                </div>

                <div class="modal-footer">
                    <button class="w-100 btn btn-lg btn-primary" type="submit">Log in</button>
                </div>

            </form>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="registrationStaticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="registrationStaticBackdropLabel">Authorization</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="registration" method="POST" class="modal-body">

                <div class="form-floating">
                    <input name="name" class="form-control" id="name"
                           placeholder="Name" required>
                    <label for="name">Name</label>
                </div>
                <div class="form-floating">
                    <input name="surname" class="form-control" id="surname"
                           placeholder="Surname" required>
                    <label for="surname">Surname</label>
                </div>
                <div class="form-floating">
                    <input name="phone" type="tel" pattern="(\+38)?(0\d{9})" required class="form-control" id="phone"
                           placeholder="+380961111111 or 0661111111" title="For example:+380961111111 or 0661111111"
                           onchange="checkUniquePhone('<c:url value="/validate"/>')">
                    <label for="phone">Phone number</label>
                </div>
                <div class="form-floating">
                    <input name="email" type="email" required class="form-control" id="email"
                           placeholder="name@example.com"
                           pattern="^\S+@\S+\.\S+$" onchange="checkUniqueEmail('<c:url value="/validate"/>')">
                    <label for="email">Email address</label>
                </div>
                <div class="form-floating">
                    <input name="password" type="password" class="form-control" id="registerPassword"
                           placeholder="Password" required>
                    <label for="registerPassword">Password</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" id="passwordCheck"
                           placeholder="Password" required>
                    <label for="passwordCheck">Repeat password</label>
                </div>

                <div class="modal-footer">
                    <button class="w-100 btn btn-lg btn-success" type="submit"
                            onclick="return validateForm();">Sign up
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.js"></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
<script src="<c:url value="/js/map.js"/>" async></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/index.js"/>" async></script>
<script>
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
            totalNumber: ${sessionScope.totalNumber},
            pageSize: pageSize,
            pageNumber: page,
            className: 'paginationjs-small d-flex justify-content-end mx-5 pb-3',
            ajax: {
                type: 'POST',
                data: {
                    orderBy: colName,
                    ascending: asc,
                    filter: filterObj
                }
            },
            callback: function (data, pagination) {
                var html = templating(data);
                $('#data-container').html(html);
            }
        })

        function templating(data) {
            let html = '';
            $.each(data, function (index, item) {
                html += `<tr>
                <td class="table-tb-width-40">
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">` + item.fromName + `</div>
                        <div class="flex-row flex-wrap text-muted fs-6">` + item.whence + `</div>
                    </div>
                </td>
                <td class="table-tb-width-40">
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap">` + item.toName + `</div>
                        <div class="flex-row flex-wrap text-muted fs-6">` + item.whither + `</div>
                    </div>
                </td>
                <td>` + item.distance + `</td>
                <td>` + item.price + `</td>
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
</script>
</body>
</html>