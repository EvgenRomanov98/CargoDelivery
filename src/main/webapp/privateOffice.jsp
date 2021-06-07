<%@ page import="java.util.ArrayList" %>
<%@ page import="ua.epam.cargo_delivery.model.db.Delivery" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/bootstrap-5.0.1-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <title>Private Office</title>
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
<div class="container">
    <form action="<c:url value="/createDelivery"/>" method="post">
        <section class="row">
            <label class="col">
                Description
                <input name="description" id="description" placeholder="Description">
            </label>
            <label class="col">
                <input name="from" id="from" placeholder="from" value="${param.from}">
            </label>
            <label class="col">
                <input name="to" id="to" placeholder="to" value="${param.to}">
            </label>
            <div class="col">
                <p>Weight, kg</p>
                <select name="weight" id="weight" class="form-select" multiple aria-label="multiple select example">
                    <option value="100" <c:if test="${param.weight == 100 || param.weight == null}">selected</c:if>>
                        <100
                    </option>
                    <option value="500" <c:if test="${param.weight == 500}">selected</c:if>>100 - 500</option>
                    <option value="1000" <c:if test="${param.weight == 1000}">selected</c:if>>500 - 1000</option>
                    <option value="1500" <c:if test="${param.weight == 1500}">selected</c:if>>1000 - 1500</option>
                </select>
            </div>
            <div class="col">
                <p>Length, mm</p>
                <select name="length" id="length" class="form-select" multiple aria-label="multiple select example">
                    <option value="1000" <c:if test="${param.length == 1000 || param.length == null}">selected</c:if>>
                        <1000
                    </option>
                    <option value="2000" <c:if test="${param.length == 2000}">selected</c:if>>1000 - 2000</option>
                    <option value="3000" <c:if test="${param.length == 3000}">selected</c:if>>2000 - 3000</option>
                    <option value="4000" <c:if test="${param.length == 4000}">selected</c:if>>3000 - 3000</option>
                </select>
            </div>
            <div class="col">
                <p>Width, mm</p>
                <select name="width" id="width" class="form-select" multiple aria-label="multiple select example">
                    <option value="400" <c:if test="${param.width == 400 || param.width == null}">selected</c:if>><400
                    </option>
                    <option value="900" <c:if test="${param.width == 900}">selected</c:if>>400 - 900</option>
                    <option value="1400" <c:if test="${param.width == 1400}">selected</c:if>>900 - 1400</option>
                    <option value="1700" <c:if test="${param.width == 1700}">selected</c:if>>1400 - 1700</option>
                </select>
            </div>
            <div class="col">
                <p>Height, mm</p>
                <select name="height" id="height" class="form-select" multiple aria-label="multiple select example">
                    <option value="400" <c:if test="${param.height == 400 || param.height == null}">selected</c:if>>
                        >400
                    </option>
                    <option value="900" <c:if test="${param.height == 900}">selected</c:if>>400 - 900</option>
                    <option value="1400" <c:if test="${param.height == 1400}">selected</c:if>>900 - 1400</option>
                    <option value="1750" <c:if test="${param.height == 1750}">selected</c:if>>1400 - 1750</option>
                </select>
            </div>
            <h5 class="col">Calculated price: <span id="price">${sessionScope.price}</span></h5>
            <button type="submit" class="btn btn-primary">
                Create Delivery
            </button>
        </section>
    </form>
    <button class="btn btn-primary" onclick="calculatePrice('<c:url value="/calculatePrice"/>')">
        Calculate
    </button>
</div>

Your deliveries:
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
        <th scope="col">Price</th>
        <th scope="col">Weight</th>
        <th scope="col">Length</th>
        <th scope="col">Width</th>
        <th scope="col">Height</th>
        <th scope="col">Status</th>
        <th scope="col">Info</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.userDeliveries}" var="delivery">
        <tr>
            <td><c:out value="${delivery.cargo.description}"/></td>
            <td><c:out value="${delivery.whence}"/></td>
            <td><c:out value="${delivery.whither}"/></td>
            <td><c:out value="${delivery.createDate}"/></td>
            <td><c:out value="${delivery.deliveryDate}"/></td>
            <td><c:out value="${delivery.distance}"/></td>
            <td><c:out value="${delivery.price}"/></td>
            <td><c:out value="${delivery.cargo.weight}"/></td>
            <td><c:out value="${delivery.cargo.length}"/></td>
            <td><c:out value="${delivery.cargo.width}"/></td>
            <td><c:out value="${delivery.cargo.height}"/></td>
            <td><c:out value="${delivery.status}"/></td>
            <td>
                <c:choose>
                    <c:when test="${delivery.status == 'CREATED'}">
                        <p>Wait approve manager</p>
                    </c:when>
                    <c:otherwise>
                        <a href='<c:url value="/getReceipt"/>'>
                            <button class="btn btn-info">
                                Get receipt
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${!sessionScope.deliveriesForPay.isEmpty()}">
    <p>The price for approved deliveries: ${requestScope.commonPrice}</p>
    <a href='<c:url value="/pay"/>'>
        <button class="btn btn-info">
            Pay
        </button>
    </a>
</c:if>

<script src="<%=request.getContextPath()%>/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>" async></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
</body>
</html>
