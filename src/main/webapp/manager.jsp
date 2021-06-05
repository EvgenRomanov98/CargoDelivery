<%@ page import="ua.epam.cargo_delivery.model.db.DeliveryStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/bootstrap-5.0.1-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <title>Manager</title>
</head>
<body>
<nav class="nav nav-fill">
    <c:if test="${loggedUser.role != 'USER'}">
        <a href="<%=request.getContextPath()%>/signOut" class="nav-item">
            <button class="text-white btn bg-dark">
                Sign out
            </button>
        </a>
    </c:if>
</nav>
<table class="table">
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
            <td>${delivery.whence}</td>
            <td>${delivery.whither}</td>
            <td>${delivery.createDate}</td>
            <td>
                <label onfocusout="updateDeliveryDate('<c:url value="/updateDeliveryDate"/>', ${delivery.id})">
                    <input class="form-control date" type="date" id="deliveryDate-${delivery.id}"
                           value="${delivery.deliveryDate}">
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
                <%--            <td>${delivery.status}</td>--%>
            <td class="flex-row">
                <label class="flex-column" ondblclick="unlockEdit('deliveryStatus-${delivery.id}')"
                       oninput="let id = 'deliveryStatus-${delivery.id}';
                               let status = document.getElementById(id).value;
                               updateStatusDelivery(status,${delivery.id},'<c:url value="/changeStatus"/>');
                               lockEdit(id)">

                    <select class="form-control" id="deliveryStatus-${delivery.id}" disabled>
                        <c:forEach items="<%=DeliveryStatus.values()%>" var="status">
                            <c:if test="${status != 'DELETED'}">
                                <option ${delivery.status == status ? 'selected' : ''}>${status}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </label>
                <a class="flex-column" href="<c:url value="/changeStatus?delivery=${delivery.id}&status=DELETED"/>">
                    <img src="<c:url value="/icons/trash.svg"/>" alt="delete"></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>" async></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
</body>
</html>
