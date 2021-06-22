<!DOCTYPE html>
<%@ page import="ua.epam.cargo_delivery.model.db.DeliveryStatus" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tf" %>

<fmt:setLocale value="${param.lang == null ? 'en' : param.lang}"/>
<fmt:setBundle basename="messages"/>
<c:set scope="page" value="${param.lang == null ? 'en' : param.lang}" var="lang"/>
<html lang="${param.lang == null ? 'en' : param.lang}">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/bootstrap-5.0.1-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <link rel="icon" href='<c:url value="/favicon.ico" />' type="image/x-icon">
    <title>Manager</title>
</head>
<body>
<nav class="navbar sticky-top navbar-light bg-light">
    <div class="container-fluid">
        <div>
            <a id="homeLocation" class="navbar-brand" href="<c:url value="/"/>">123 Delivery</a>
        </div>
        <div class="justify-content-end">
            <tf:auth/>
        </div>
    </div>
</nav>
<section id="tableSection" class="table-responsive">
    <table class="table align-middle">
        <caption>List of deliveries</caption>
        <thead class="table-dark">
        <tr>
            <th scope="col">Description</th>
            <th scope="col">Whence</th>
            <th scope="col">Whither</th>
            <th scope="col">CreateDate</th>
            <th scope="col">DeliveryDate</th>
            <th scope="col">Distance</th>
            <th scope="col">Weight</th>
            <th scope="col">Length</th>
            <th scope="col">Width</th>
            <th scope="col">Height</th>
            <th scope="col">Name</th>
            <th scope="col">Surname</th>
            <th scope="col">Price</th>
            <th scope="col">Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${sessionScope.managerDeliveries}" var="delivery">
            <tr>
                <td>${delivery.cargo.description}</td>
                <td>
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap"><c:out value="${delivery.fromName}"/></div>
                        <div class="flex-row flex-wrap text-muted fs-6"><c:out value="${delivery.whence}"/></div>
                    </div>
                </td>
                <td>
                    <div class="d-flex flex-column">
                        <div class="flex-row flex-wrap"><c:out value="${delivery.toName}"/></div>
                        <div class="flex-row flex-wrap text-muted fs-6"><c:out value="${delivery.whither}"/></div>
                    </div>
                </td>
                <td>${delivery.createDate}</td>
                <td>
                    <label>
                        <input class="form-control date" type="date" id="deliveryDate-${delivery.id}"
                               value="${delivery.deliveryDate}"
                               oninput="updateDeliveryDate('<c:url value="/updateDeliveryDate"/>', ${delivery.id})">
                    </label>
                </td>
                <td>${delivery.distance}</td>
                <td>${delivery.cargo.weight}</td>
                <td>${delivery.cargo.length}</td>
                <td>${delivery.cargo.width}</td>
                <td>${delivery.cargo.height}</td>
                <td>${delivery.user.name}</td>
                <td>${delivery.user.surname}</td>
                <td>${delivery.price}</td>
                <td>
                    <div class="d-flex flex-row">
                        <label class="flex-column" ondblclick="unlockEdit('deliveryStatus-${delivery.id}')"
                               oninput="let id = 'deliveryStatus-${delivery.id}';
                                       let status = document.getElementById(id).value;
                                       updateStatusDelivery(status,${delivery.id},'<c:url value="/changeStatus"/>');
                                       lockEdit(id)">

                            <select class="form-control status-select" id="deliveryStatus-${delivery.id}" disabled>
                                <c:forEach items="<%=DeliveryStatus.values()%>" var="status">
                                    <c:if test="${status != 'DELETED'}">
                                        <option ${delivery.status == status ? 'selected' : ''}>${status}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </label>
                        <div class="d-flex justify-content-center align-items-center px-1">
                            <a class="flex-column"
                               href="<c:url value="/changeStatus?delivery=${delivery.id}&status=DELETED"/>">
                                <img src="<c:url value="/icons/trash.svg"/>" alt="delete"></a>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>
<form class="container-fluid my-3" action="<c:url value="/getReport"/>" method="get">
    <div class="row">
        <div class="col form-floating">
            <select class="form-select" name="fromRegion" id="fromRegion">
                <option selected></option>
                <c:forEach items="${sessionScope.availableRegions}" var="city">
                    <option value="${city.id}">${city.region}</option>
                </c:forEach>
            </select>
            <label for="fromRegion" style="left: auto">Select from region</label>
        </div>
        <div class="col form-floating">
            <select class="form-select" name="toRegion" id="toRegion">
                <option selected></option>
                <c:forEach items="${sessionScope.availableRegions}" var="city">
                    <option value="${city.id}">${city.region}</option>
                </c:forEach>
            </select>
            <label for="toRegion" style="left: auto">Select to region</label>
        </div>
        <div class="col form-floating">
            <input type="date" class="form-control" name="createDate" id="createDate">
            <label for="createDate" style="left: auto">Select create date</label>
        </div>
        <div class="col form-floating">
            <input type="date" class="form-control" name="deliveryDate" id="deliveryDate">
            <label for="deliveryDate" style="left: auto">Select delivery date</label>
        </div>
        <button class="col btn btn-outline-dark" type="submit">Create Report</button>
    </div>
</form>
<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
</body>
</html>
