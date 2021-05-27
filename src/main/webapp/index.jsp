<%@ page import="ua.epam.cargo_delivery.model.dao.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="bootstrap-5.0.1-dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <title>Hello, world!</title>
</head>
<body>
<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: Bootstrap Bundle with Popper -->
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
    Authorization
</button>

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

<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#registrationStaticBackdrop">
    Registration
</button>

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
${loggedUser.name} ${loggedUser.surname}
<form class="container">
    <section class="row">
        <div class="col">
            <input name="from" placeholder="from">
        </div>
        <div class="col">
            <input name="to" placeholder="to">
        </div>
        <div class="col">
            <p>Weight, kg</p>
            <select name="weight" class="form-select" multiple aria-label="multiple select example">
                <option value="100" selected><100</option>
                <option value="500">100 - 500</option>
                <option value="1000">500 - 1000</option>
                <option value="1500">1000 - 1500</option>
            </select>
        </div>
        <div class="col">
            <p>Length, mm</p>
            <select name="length" class="form-select" multiple aria-label="multiple select example">
                <option value="1000" selected><1000</option>
                <option value="2000">1000 - 2000</option>
                <option value="3000">2000 - 3000</option>
                <option value="4000">3000 - 3000</option>
            </select>
        </div>
        <div class="col">
            <p>Width, mm</p>
            <select name="width" class="form-select" multiple aria-label="multiple select example">
                <option value="400" selected><400</option>
                <option value="900">400 - 900</option>
                <option value="1400">900 - 1400</option>
                <option value="1700">1400 - 1700</option>
            </select>
        </div>
        <div class="col">
            <p>Height, mm</p>
            <select name="length" class="form-select" multiple aria-label="multiple select example">
                <option value="400" selected>>400</option>
                <option value="900">400 - 900</option>
                <option value="1400">900 - 1400</option>
                <option value="1750">1400 - 1750</option>
            </select>
        </div>
        <h3 class="col">Calculated price: <span id="price">100</span></h3>
    </section>
</form>
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
    <c:forEach items="${applicationScope.deliveries}" var="delivery">
        <tr>
            <td><c:out value="${delivery.from}"/></td>
            <td><c:out value="${delivery.to}"/></td>
            <td><c:out value="${delivery.distance}"/></td>
            <td><c:out value="${delivery.price}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script src="bootstrap-5.0.1-dist/js/bootstrap.bundle.min.js"
        integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4"
        crossorigin="anonymous"></script>
</body>
</html>