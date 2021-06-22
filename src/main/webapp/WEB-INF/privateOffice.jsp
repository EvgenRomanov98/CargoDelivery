<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <link href="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.css" rel="stylesheet">
    <link href="<c:url value="/css/delivery.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/map.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/pagination.css"/>" rel="stylesheet">
    <link rel="icon" href='<c:url value="/favicon.ico" />' type="image/x-icon">
    <title>Private Office</title>
</head>
<body>
<nav class="navbar sticky-top navbar-light bg-light">
    <div class="container-fluid row px-2">
        <div class="col-6">
            <a id="homeLocation" class="navbar-brand" href="<c:url value="/"/>">123 Delivery</a>
        </div>
        <div class="col d-flex justify-content-end">
            <div class="dropdown">
                <button class="btn btn-outline-info btn-sm dropdown-toggle" type="button" id="dropdownMenuButton1"
                        data-bs-toggle="dropdown"
                        aria-expanded="false">
                    <c:if test="${param.lang == null}">
                        en
                    </c:if>
                    <c:if test="${param.lang != null}">
                        ${param.lang}
                    </c:if>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="?lang=en">en</a></li>
                    <li><a class="dropdown-item" href="?lang=ru">ru</a></li>
                    <li><a class="dropdown-item" href="?lang=ua">ua</a></li>
                </ul>
            </div>
            <tf:auth/>
        </div>
    </div>
</nav>

<div class="container-fluid my-3">
    <div class="row">
        <div id="map" class="col"></div>
        <form class="col container-fluid" action="<c:url value="/createDelivery"/>" method="post">
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="description" class="form-control" id="description"
                           placeholder="Type of delivery">
                    <label for="description" style="left: auto"><fmt:message key="delivery.type"/></label>
                </div>
            </section>
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="from" class="form-control" id="from" placeholder="<fmt:message key="location.from"/>"
                           value="${param.from}">
                    <label for="from" style="left: auto"><fmt:message key="location.from"/></label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="to" class="form-control" id="to" placeholder="<fmt:message key="location.to"/>"
                           value="${param.to}">
                    <label for="to" style="left: auto"><fmt:message key="location.to"/></label>
                </div>
                <label><input name="fromRegionId" id="fromRegionId" value="${param.fromRegionId}" hidden></label>
                <label><input name="toRegionId" id="toRegionId" value="${param.toRegionId}" hidden></label>
            </section>
            <section class="row">
                <div class="form-floating mb-3 col">
                    <input name="fromName" class="form-control" id="fromName"
                           placeholder="<fmt:message key="address.from"/>"
                           value="${param.fromName}">
                    <label for="fromName" style="left: auto"><fmt:message key="address.from"/></label>
                </div>
                <div class="form-floating mb-3 col">
                    <input name="toName" class="form-control" id="toName" placeholder="<fmt:message key="address.to"/>"
                           value="${param.toName}">
                    <label for="toName" style="left: auto"><fmt:message key="address.to"/></label>
                </div>
            </section>
            <section class="row">
                <label class="col"><fmt:message key="display.weight"/>
                    <select name="weight" id="weight" class="form-select overflow-hidden" multiple>
                        <option value="100" <c:if test="${param.weight == 100 || param.weight == null}">selected</c:if>>
                            <100
                        </option>
                        <option value="500" <c:if test="${param.weight == 500}">selected</c:if>>100 - 500</option>
                        <option value="1000" <c:if test="${param.weight == 1000}">selected</c:if>>500 - 1000</option>
                        <option value="1500" <c:if test="${param.weight == 1500}">selected</c:if>>1000 - 1500</option>
                    </select>
                </label>
                <label class="col"><fmt:message key="display.length"/>
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
                <label class="col"><fmt:message key="display.width"/>
                    <select name="width" id="width" class="form-select overflow-hidden" multiple>
                        <option value="400" <c:if test="${param.width == 400 || param.width == null}">selected</c:if>>
                            <400
                        </option>
                        <option value="900" <c:if test="${param.width == 900}">selected</c:if>>400 - 900</option>
                        <option value="1400" <c:if test="${param.width == 1400}">selected</c:if>>900 - 1400</option>
                        <option value="1700" <c:if test="${param.width == 1700}">selected</c:if>>1400 - 1700</option>
                    </select>
                </label>
                <label class="col"><fmt:message key="display.height"/>
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
                        <fmt:message key="button.create"/>
                    </button>
                    <button class="btn btn-outline-info col-5 col-xxl-4" type="button"
                            onclick="calculatePrice('<c:url value="/calculatePrice"/>')">
                        <fmt:message key="button.calculate"/>
                    </button>
                </div>
            </section>
        </form>
    </div>
</div>

<table class="table caption-top">
    <caption><fmt:message key="table.caption.private"/></caption>
    <thead class="table-dark">
    <tr class="align-middle">
        <th scope="col"><span col="c.description" class="d-flex"><fmt:message key="table.description"/></span></th>
        <th scope="col">
            <div class="container-fluid row m-0 p-0 align-items-center">
                <span class="col-5" col="fromName"><fmt:message key="table.from"/></span>
                <div class="col">
                    <div class="input-group input-group-sm">
                        <label for="filterWhence" class="input-group-text">
                            <img src="<c:url value="/icons/filter.svg"/>" alt="Filter">
                        </label>
                        <input id="filterWhence" col="fromName" type="text" class="form-control"
                               placeholder="<fmt:message key="filter.from.description"/>">
                    </div>
                </div>
            </div>
        </th>
        <th scope="col">
            <div class="container-fluid row m-0 p-0 align-items-center">
                <span class="col-5" col="toName"><fmt:message key="table.to"/></span>
                <div class="col">
                    <div class="input-group input-group-sm">
                        <label for="filterWhither" class="input-group-text">
                            <img src="<c:url value="/icons/filter.svg"/>" alt="Filter">
                        </label>
                        <input id="filterWhither" col="toName" type="text" class="form-control"
                               placeholder="<fmt:message key="filter.to.description"/>">
                    </div>
                </div>
            </div>
        </th>
        <th scope="col"><span col="d.create_date" class="d-flex"><fmt:message key="table.createDate"/></span></th>
        <th scope="col"><span col="d.delivery_date" class="d-flex"><fmt:message key="table.deliveryDate"/></span></th>
        <th scope="col"><span col="d.distance" class="d-flex"><fmt:message key="table.distance"/></span></th>
        <th scope="col"><span col="d.price" class="d-flex"><fmt:message key="table.price"/></span></th>
        <th scope="col"><span col="c.weight" class="d-flex"><fmt:message key="display.weight"/></span></th>
        <th scope="col"><span col="c.length" class="d-flex"><fmt:message key="display.length"/></span></th>
        <th scope="col"><span col="c.width" class="d-flex"><fmt:message key="display.width"/></span></th>
        <th scope="col"><span col="c.height" class="d-flex"><fmt:message key="display.height"/></span></th>
        <th scope="col"><span col="d.status" class="d-flex"><fmt:message key="table.status"/></span></th>
        <th scope="col"><span class="d-flex"><fmt:message key="table.info"/></span></th>
    </tr>
    </thead>
    <tbody id="data-container" class="container-fluid">
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
                                <fmt:message key="button.get.receipt"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="pagination-container"></div>
<c:if test="${!sessionScope.deliveriesForPay.isEmpty()}">
    <div class="d-flex align-items-center">
        <span class="px-2"><fmt:message key="price.description"/> ${requestScope.commonPrice}</span>
        <a href='<c:url value="/pay"/>'>
            <button class="btn btn-info m-3">
                <fmt:message key="button.pay"/>
            </button>
        </a>
    </div>
</c:if>

<script src="<c:url value="/bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"/>" async></script>
<script src="https://api.mapbox.com/mapbox-gl-js/v2.3.0/mapbox-gl.js"></script>
<script src="<c:url value="/js/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="/js/delivery.js"/>" async></script>
<script src="<c:url value="/js/map.js"/>" async></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/privateOffice.js"/>" async></script>
<script>
    sessionStorage.setItem("totalNumber", ${sessionScope.totalNumberForUser});
    sessionStorage.setItem("userId", ${sessionScope.loggedUser.id});
    sessionStorage.setItem('button.get.receipt', '<fmt:message key="button.get.receipt"/>');
</script>
</body>
</html>
