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
    <link href="<%=request.getContextPath()%>/bootstrap-5.0.1-dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.css" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/map.css"/>" rel="stylesheet">
    <title>Index</title>
</head>
<body>
<!-- Optional JavaScript; choose one of the two! -->
<nav class="navbar sticky-top navbar-light bg-light">
    <div class="container-fluid">
        <div>
            <a class="navbar-brand" href="#">123 Delivery</a>
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
<div class="container-fluid pt-1">
    <div class="row">
        <div id="map" class="col"></div>
        <%--            <pre id="info"></pre>--%>
        <form class="col container-fluid" action="<c:url value="/privateOffice"/>" method="post">
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="from" class="form-control" id="from" placeholder="20;20">
                    <label for="from" style="left: auto">Longitude,Latitude From</label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="to" class="form-control" id="to" placeholder="20;20">
                    <label for="to" style="left: auto">Longitude,Latitude To</label>
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
            <section class="row align-items-center">
                <h6 id="price" class="col-6 pt-1"></h6>
                <div class="col">
                    <div class="row justify-content-end gx-1 pt-1">
                        <button class="btn btn-outline-info col-4 me-2" type="button"
                                onclick="calculatePrice('<c:url value="/calculatePrice"/>')">
                            Calculate
                        </button>
                        <c:if test="${sessionScope.loggedUser.role.checkPermission(Action.CREATE_DELIVERY)}">
                            <button type="submit" class="btn btn-outline-success col-4">
                                Create Delivery
                            </button>
                        </c:if>
                    </div>
                </div>
            </section>
        </form>
    </div>
</div>
<table id="test" class="table">
    <caption>List of deliveries</caption>
    <thead class="table-dark">
    <tr>
        <th scope="col">From</th>
        <th scope="col">To</th>
        <th scope="col">Distance</th>
        <th scope="col">Price</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${deliveries}" var="delivery">
        <tr>
            <td><c:out value="${delivery.whence}"/></td>
            <td><c:out value="${delivery.whither}"/></td>
            <td><c:out value="${delivery.distance}"/></td>
            <td><c:out value="${delivery.price}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>


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
                    <input name="email" type="email" class="form-control" id="floatingInput"
                           placeholder="name@example.com">
                    <label for="floatingInput">Email address</label>
                </div>
                <div class="form-floating">
                    <input name="password" type="password" class="form-control" id="floatingPassword"
                           placeholder="Password">
                    <label for="floatingPassword">Password</label>
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
                           placeholder="Name">
                    <label for="name">Name</label>
                </div>
                <div class="form-floating">
                    <input name="surname" class="form-control" id="surname"
                           placeholder="Surname">
                    <label for="surname">Surname</label>
                </div>
                <div class="form-floating">
                    <input name="phone" type="tel" pattern="(\+38)?(0\d{9})" required class="form-control" id="phone"
                           placeholder="+380961111111 or 0661111111">
                    <label for="phone">Phone number</label>
                </div>
                <div class="form-floating">
                    <input name="email" type="email" required class="form-control" id="email"
                           placeholder="name@example.com">
                    <label for="email">Email address</label>
                </div>
                <div class="form-floating">
                    <input name="password" type="password" class="form-control" id="password"
                           placeholder="Password">
                    <label for="password">Password</label>
                </div>

                <div class="modal-footer">
                    <button class="w-100 btn btn-lg btn-success" type="submit">Sign up</button>
                </div>

            </form>
        </div>
    </div>
</div>


<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>" async></script>
<script src="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.js"></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
<script src="<c:url value="/js/map.js"/>" async></script>
</body>
</html>