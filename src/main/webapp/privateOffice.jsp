<%@ page import="java.util.ArrayList" %>
<%@ page import="ua.epam.cargo_delivery.model.db.Delivery" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/bootstrap-5.0.1-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.css" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/map.css"/>" rel="stylesheet">
    <title>Private Office</title>
</head>
<body>
<nav class="navbar sticky-top navbar-light bg-light">
    <div class="container-fluid">
        <div>
            <a id="homeLocation" class="navbar-brand" href="<c:url value="/"/>">123 Delivery</a>
        </div>
        <div class="justify-content-end">
            <a href="#" class="btn btn-outline-info btn-sm">
                ${loggedUser.name} ${loggedUser.surname}
            </a>
            <a href="<c:url value="/signOut"/>" class="btn btn-outline-dark btn-sm">
                Sign out
            </a>
        </div>
    </div>
</nav>

<%--<div class="container">
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
</div>--%>

<div class="container-fluid my-3">
    <div class="row">
        <div id="map" class="col"></div>
        <form class="col container-fluid" action="<c:url value="/createDelivery"/>" method="post">
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="description" class="form-control" id="description"
                           placeholder="Type of delivery">
                    <label for="description" style="left: auto">Type of delivery</label>
                </div>
            </section>
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="from" class="form-control" id="from" placeholder="Longitude,Latitude From"
                           value="${param.from}">
                    <label for="from" style="left: auto">Longitude,Latitude From</label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="to" class="form-control" id="to" placeholder="Longitude,Latitude To"
                           value="${param.to}">
                    <label for="to" style="left: auto">Longitude,Latitude To</label>
                </div>
                <label><input name="fromRegionId" id="fromRegionId" hidden></label>
                <label><input name="toRegionId" id="toRegionId" hidden></label>
            </section>
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="fromName" class="form-control" id="fromName" placeholder="City from"
                           value="${param.fromName}">
                    <label for="fromName" style="left: auto">City from</label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="toName" class="form-control" id="toName" placeholder="City to"
                           value="${param.toName}">
                    <label for="toName" style="left: auto">City to</label>
                </div>
            </section>
            <section class="row">
                <label class="col">Weight, kg
                    <select name="weight" id="weight" class="form-select overflow-hidden" multiple>
                        <option value="100" <c:if test="${param.weight == 100 || param.weight == null}">selected</c:if>>
                            <100
                        </option>
                        <option value="500" <c:if test="${param.weight == 500}">selected</c:if>>100 - 500</option>
                        <option value="1000" <c:if test="${param.weight == 1000}">selected</c:if>>500 - 1000</option>
                        <option value="1500" <c:if test="${param.weight == 1500}">selected</c:if>>1000 - 1500</option>
                    </select>
                </label>
                <label class="col">Length, mm
                    <select name="length" id="length" class="form-select overflow-hidden" multiple>
                        <option value="1000"
                                <c:if test="${param.length == 1000 || param.length == null}">selected</c:if>>
                            <1000
                        </option>
                        <option value="2000" <c:if test="${param.length == 2000}">selected</c:if>>1000 - 2000</option>
                        <option value="3000" <c:if test="${param.length == 3000}">selected</c:if>>2000 - 3000</option>
                        <option value="4000" <c:if test="${param.length == 4000}">selected</c:if>>3000 - 3000</option>
                    </select>
                </label>
                <label class="col">Width, mm
                    <select name="width" id="width" class="form-select overflow-hidden" multiple>
                        <option value="400" <c:if test="${param.width == 400 || param.width == null}">selected</c:if>>
                            <400
                        </option>
                        <option value="900" <c:if test="${param.width == 900}">selected</c:if>>400 - 900</option>
                        <option value="1400" <c:if test="${param.width == 1400}">selected</c:if>>900 - 1400</option>
                        <option value="1700" <c:if test="${param.width == 1700}">selected</c:if>>1400 - 1700</option>
                    </select>
                </label>
                <label class="col">Height, mm
                    <select name="height" id="height" class="form-select overflow-hidden" multiple>
                        <option value="400" <c:if test="${param.height == 400 || param.height == null}">selected</c:if>>
                            >400
                        </option>
                        <option value="900" <c:if test="${param.height == 900}">selected</c:if>>400 - 900</option>
                        <option value="1400" <c:if test="${param.height == 1400}">selected</c:if>>900 - 1400</option>
                        <option value="1750" <c:if test="${param.height == 1750}">selected</c:if>>1400 - 1750</option>
                    </select>
                </label>
            </section>
            <section class="row align-items-center pt-1">
                <h6 id="price" class="col-6 my-0"><c:if
                        test="${param.price != null}">Calculated price: ${param.price}</c:if></h6>
                <div class="col-6 d-flex flex-row-reverse">
                    <button type="submit"
                            class="btn btn-outline-success col-5 col-xxl-4 ms-2">
                        Create
                    </button>
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
    <table class="table">
        <caption>Your last deliveries</caption>
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
                            <a href='<c:url value="/getReceipt?idDelivery=${delivery.id}"/>'>
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
</section>
<c:if test="${!sessionScope.deliveriesForPay.isEmpty()}">
    <p>The price for approved deliveries: ${requestScope.commonPrice}</p>
    <a href='<c:url value="/pay"/>'>
        <button class="btn btn-info">
            Pay
        </button>
    </a>
</c:if>

<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.js"></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
<script src="<c:url value="/js/map.js"/>" async></script>
</body>
</html>
