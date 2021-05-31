<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<%=request.getContextPath()%>/bootstrap-5.0.1-dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Private Office</title>
</head>
<body>
<div class="container">
    <form action="createDelivery" method="post">
        <section class="row">
            <label class="col">
                Description
                <input name="decryption" placeholder="Description">
            </label>
            <label class="col">
                <input name="from" placeholder="from" value="${param.from}">
            </label>
            <label class="col">
                <input name="to" placeholder="to" value="${param.to}">
            </label>
            <div class="col">
                <p>Weight, kg</p>
                <select name="weight" class="form-select" multiple aria-label="multiple select example">
                    <option value="100" <c:if test="${param.weight == 100}">selected</c:if>><100</option>
                    <option value="500" <c:if test="${param.weight == 500}">selected</c:if>>100 - 500</option>
                    <option value="1000" <c:if test="${param.weight == 1000}">selected</c:if>>500 - 1000</option>
                    <option value="1500" <c:if test="${param.weight == 1500}">selected</c:if>>1000 - 1500</option>
                </select>
            </div>
            <div class="col">
                <p>Length, mm</p>
                <select name="length" class="form-select" multiple aria-label="multiple select example">
                    <option value="1000" <c:if test="${param.length == 1000}">selected</c:if>><1000</option>
                    <option value="2000" <c:if test="${param.length == 2000}">selected</c:if>>1000 - 2000</option>
                    <option value="3000" <c:if test="${param.length == 3000}">selected</c:if>>2000 - 3000</option>
                    <option value="4000" <c:if test="${param.length == 4000}">selected</c:if>>3000 - 3000</option>
                </select>
            </div>
            <div class="col">
                <p>Width, mm</p>
                <select name="width" class="form-select" multiple aria-label="multiple select example">
                    <option value="400" <c:if test="${param.width == 400}">selected</c:if>><400</option>
                    <option value="900" <c:if test="${param.width == 900}">selected</c:if>>400 - 900</option>
                    <option value="1400" <c:if test="${param.width == 1400}">selected</c:if>>900 - 1400</option>
                    <option value="1700" <c:if test="${param.width == 1700}">selected</c:if>>1400 - 1700</option>
                </select>
            </div>
            <div class="col">
                <p>Height, mm</p>
                <select name="height" class="form-select" multiple aria-label="multiple select example">
                    <option value="400" <c:if test="${param.height == 400}">selected</c:if>>>400</option>
                    <option value="900" <c:if test="${param.height == 900}">selected</c:if>>400 - 900</option>
                    <option value="1400" <c:if test="${param.height == 1400}">selected</c:if>>900 - 1400</option>
                    <option value="1750" <c:if test="${param.height == 1750}">selected</c:if>>1400 - 1750</option>
                </select>
            </div>
            <h3 class="col">Calculated price: <span id="price">${requestScope.price}</span></h3>
            <button type="submit" class="btn btn-primary" data-bs-toggle="modal"
                    data-bs-target="#registrationStaticBackdrop">
                Create delivery
            </button>
        </section>
    </form>
</div>

Your deliveries:

<table id="test" class="table">
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
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${userDeliveries}" var="delivery">
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
        </tr>
    </c:forEach>
    </tbody>
</table>

<script src="<%=request.getContextPath()%>/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
